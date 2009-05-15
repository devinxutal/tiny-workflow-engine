package edu.thu.thss.twe.db;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.model.runtime.ProcessInstance;
import edu.thu.thss.twe.model.runtime.Token;
import edu.thu.thss.twe.util.CollectionUtil;

public class WorkflowProcessDBHelper {
	private Session session;

	private static String find_workflow_process_by_name_and_version = "from WorkflowProcess where name=:name and version=:version";
	private static String find_workflow_process_by_name = "from WorkflowProcess where name=:name order by name asc, version desc";
	private static String find_process_instance_ids_by_workflow_process_id = "select id from ProcessInstance where workflow_process_id=:id";
	private static String find_process_instances_by_workflow_process_id = "from ProcessInstance where workflow_process_id=:id";
	private static String find_process_instance_by_workflow_process_id_and_key = "from ProcessInstance where workflow_process_id=:id and key=:key";

	public WorkflowProcessDBHelper(Session s) {
		session = s;
	}

	/**
	 * deploy a WorkflowProcess, if this WorkflowProcess is a new one(with a new
	 * process name) its version will be set to 1, if not, the version will be
	 * set to the latest version +1.
	 * 
	 * @param process
	 *            the WorkflowProcess to be deployed
	 */
	public void deployWorkflowProcess(WorkflowProcess process) {
		String processName = process.getName();
		if (processName != null) {
			WorkflowProcess latestProcess = findLatestWorkflowProcess(processName);
			// if there is a current latest workflow process
			if (latestProcess != null) {
				// take the next version number
				process.setVersion(latestProcess.getVersion() + 1);
			} else {
				process.setVersion(1);
			}
			save(process);
		} else {
			throw new TweException("the workflow process does not have a name");
		}
	}

	/**
	 * save a WorkflowProcess
	 * 
	 * @param process
	 *            the WorkflowProcess to be saved;
	 */
	public void saveWorkflowProcess(WorkflowProcess process) {
		save(process);
	}

	public void deleteWorkflowProcess(long id) {
		deleteWorkflowProcess(loadWorkflowProcess(id));
	}

	public void deleteWorkflowProcess(WorkflowProcess process) {
		if (process == null) {
			throw new IllegalArgumentException("WorkflowProcess cannot be null");
		}

		try {
			session.beginTransaction();
			// delete all the process instances of this definition
			Query q = session
					.createQuery(find_process_instance_ids_by_workflow_process_id);
			q.setLong("id", process.getId());
			List<?> processInstanceIds = q.list();
			for (Long processInstanceId : CollectionUtil.checkList(
					processInstanceIds, Long.class)) {
				ProcessInstance processInstance = loadProcessInstance(processInstanceId);
				if (processInstance != null) {
					deleteProcessInstance(processInstance);
				} else {
					// TODO log it
				}
			}
			// then delete the workflow process
			session.delete(process);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw new TweException("could not delete " + process, e);
		}
	}

	/**
	 * loads a workflow process from the database by the identifier.
	 * 
	 * @return the referenced workflow process or null in case it doesn't exist.
	 */
	public WorkflowProcess loadWorkflowProcess(long id) {
		try {
			return (WorkflowProcess) session.get(WorkflowProcess.class,
					new Long(id));
		} catch (Exception e) {

			throw new TweException("could not get workflow process " + id, e);
		}
	}

	/**
	 * queries the database for a workflow process with the given name and
	 * version.
	 */
	public WorkflowProcess findWorkflowProcess(String name, int version) {
		try {
			Query q = session
					.createQuery(find_workflow_process_by_name_and_version);
			q.setParameter("name", name);
			q.setParameter("version", version);
			List<?> result = q.list();
			if (result.size() > 0) {
				return (WorkflowProcess) result.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {

			throw new TweException("could not find workflow process '" + name
					+ "' at version " + version, e);
		}
	}

	/**
	 * queries the database for the latest version of a workflow process with
	 * the given name.
	 */
	public WorkflowProcess findLatestWorkflowProcess(String name) {
		try {
			Query q = session.createQuery(find_workflow_process_by_name);
			q.setParameter("name", name);
			List<?> result = q.list();
			if (result.size() > 0) {
				return (WorkflowProcess) result.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {

			throw new TweException("could not find workflow process '" + name,
					e);
		}
	}

	/**
	 * queries the database for the latest version of each workflow process.
	 * Process definitions are distinct by name.
	 */
	public List<WorkflowProcess> findLatestWorkflowProcesses() {
		List<WorkflowProcess> all = findAllWorkflowProcesses();
		Set<String> names = new HashSet<String>();
		List<WorkflowProcess> processes = new LinkedList<WorkflowProcess>();
		for (WorkflowProcess p : all) {
			if (!names.contains(p.getName())) {
				names.add(p.getName());
				processes.add(p);
			}
		}
		return processes;
	}

	public List<WorkflowProcess> findWorkflowProcesss(
			Collection<Long> workflowProcessIds) {
		List<?> workflowProcesss = session
				.createCriteria(WorkflowProcess.class).add(
						Restrictions.in("id", workflowProcessIds)).list();
		return CollectionUtil
				.checkList(workflowProcesss, WorkflowProcess.class);
	}

	/**
	 * queries the database for all workflow processs, ordered by name
	 * (ascending), then by version (descending).
	 */
	public List<WorkflowProcess> findAllWorkflowProcesses() {
		try {
			List<?> workflowProcesss = session.createQuery(
					"from WorkflowProcess order by name asc, version desc")
					.list();
			return CollectionUtil.checkList(workflowProcesss,
					WorkflowProcess.class);
		} catch (Exception e) {

			throw new TweException("could not find all workflow processs", e);
		}
	}

	/**
	 * queries the database for all versions of workflow processs with the given
	 * name, ordered by version (descending).
	 */
	public List<WorkflowProcess> findAllWorkflowProcessVersions(String name) {
		try {
			Query q = session.createQuery(find_workflow_process_by_name);
			q.setParameter("name", name);
			List<?> result = q.list();

			return CollectionUtil.checkList(result, WorkflowProcess.class);
		} catch (Exception e) {

			throw new TweException("could not find workflow process '" + name,
					e);
		}
	}

	/**
	 * loads a process instance from the database by the identifier. This method
	 * returns null in case the given process instance doesn't exist.
	 * 
	 * @return the referenced process instance or null in case it doesn't exist.
	 */
	public ProcessInstance loadProcessInstance(long processInstanceId) {
		try {
			return (ProcessInstance) session.get(ProcessInstance.class,
					new Long(processInstanceId));
		} catch (Exception e) {

			throw new TweException("could not get process instance "
					+ processInstanceId, e);
		}
	}

	/**
	 * loads a process instance from the database with the given workflow
	 * process and key . This method returns null in case the given process
	 * instance doesn't exist.
	 * 
	 * @param process
	 * @param key
	 * @return the referenced process instance, or null in case the given
	 *         process instance doesn't exist
	 */
	public ProcessInstance findProcessInstance(WorkflowProcess process,
			String key) {
		try {
			Query q = session
					.createQuery(find_process_instance_by_workflow_process_id_and_key);
			q.setLong("id", process.getId());
			q.setString("key", key);
			List<?> result = q.list();
			if (result.size() == 0) {
				return null;
			} else {
				return (ProcessInstance) result.get(0);
			}
		} catch (Exception e) {

			throw new TweException("could not get process instance of"
					+ process + " with key=" + key, e);
		}
	}

	/**
	 * fetches all processInstances for the given workflow process from the
	 * database. The returned list of process instances is sorted start date,
	 * youngest first.
	 */
	public List<ProcessInstance> findProcessInstances(long workflowProcessId) {
		try {
			Query q = session
					.createQuery(find_process_instances_by_workflow_process_id);
			q.setLong("id", workflowProcessId);
			List<?> processInstances = q.list();
			return CollectionUtil.checkList(processInstances,
					ProcessInstance.class);
		} catch (Exception e) {

			throw new TweException(
					"could not find process instances for workflow process "
							+ workflowProcessId, e);
		}
	}

	public void saveProcessInstance(ProcessInstance processInstance) {
		save(processInstance);
	}

	public void deleteProcessInstance(long id) {
		deleteProcessInstance(loadProcessInstance(id));
	}

	public void deleteProcessInstance(ProcessInstance instance) {
		try {
			session.beginTransaction();
			session.delete(instance);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw new TweException("process instance " + instance
					+ " cannot be deleted.", e);
		}
	}

	/**
	 * loads a token from the database by the identifier.
	 * 
	 * @return the token.
	 * @return the token or null in case the token doesn't exist.
	 */
	public Token loadToken(long tokenId) {
		try {
			return (Token) session.get(Token.class, new Long(tokenId));
		} catch (Exception e) {

			throw new TweException("could not get token " + tokenId, e);
		}
	}


	// ////////////////////////////
	/**
	 * save a object to the database
	 */
	private void save(Object o) {
		try {
			session.beginTransaction();
			session.save(o);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw new TweException("object cannot be saved.", e);
		}
	}

}
