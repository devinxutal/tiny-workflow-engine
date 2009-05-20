package edu.thu.thss.twe;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;

import edu.thu.thss.twe.model.graph.Participant;
import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.model.runtime.ProcessInstance;
import edu.thu.thss.twe.model.runtime.Task;
import edu.thu.thss.twe.model.runtime.Token;
import edu.thu.thss.twe.session.ModelSession;
import edu.thu.thss.twe.session.TaskSession;
import edu.thu.thss.twe.util.HibernateUtil;

public class TweContext {

	private Session session;
	private ModelSession modelSession;
	private TaskSession taskSession;
	private String performerId = null;
	private List<ProcessInstance> managedProcessInstances;

	public TweContext(Session session) {
		this.session = session;
		modelSession = new ModelSession(this.session);
		taskSession = new TaskSession(this.session);
	}

	public ModelSession getModelSession() {
		return modelSession;
	}

	public TaskSession getTaskSession() {
		return taskSession;
	}

	public void close() {
		autosave();
		HibernateUtil.currentSession().flush();
	}

	/**
	 * gets all tasks
	 * 
	 * @param autosave
	 *            if autosave is true, the corresponding process instance will
	 *            be managed and auto saved.
	 * @return
	 */
	public List<Task> getTaskList(boolean autosave) {
		List<Task> tasks = taskSession.findAllTasks();
		if (autosave) {
			for (Task task : tasks) {
				addManagedProcessInstance(task.getProcessInstance());
			}
		}
		return tasks;
	}

	/**
	 * gets all tasks for a certain performer(Participant)
	 * 
	 * @param performerId
	 *            the element id of the performer
	 * @param autosave
	 * @return
	 */
	public List<Task> getTaskList(String performerId, boolean autosave) {
		List<Task> tasks = taskSession.findTasks(performerId);
		if (autosave) {
			for (Task task : tasks) {
				addManagedProcessInstance(task.getProcessInstance());
			}
		}
		return tasks;
	}

	/**
	 * gets all tasks for a certain performer(Participant) and process instance
	 * 
	 * @param instance
	 *            the process instance
	 * @param performerId
	 *            the element id of the performer
	 * @param autosave
	 * @return
	 */
	public List<Task> getTaskList(ProcessInstance instance, String performerId,
			boolean autosave) {
		List<Task> tasks = taskSession.findTasks(instance, taskSession
				.loadParticipant(performerId));
		if (autosave) {
			for (Task task : tasks) {
				addManagedProcessInstance(task.getProcessInstance());
			}
		}
		return tasks;
	}

	/**
	 * gets all tasks of a certain process instance
	 * 
	 * @param instance
	 *            the process instance
	 * @param autosave
	 * @return
	 */
	public List<Task> getTaskList(ProcessInstance instance, boolean autosave) {
		List<Task> tasks = taskSession.findTasks(instance);
		if (autosave) {
			for (Task task : tasks) {
				addManagedProcessInstance(task.getProcessInstance());
			}
		}
		return tasks;
	}

	public String getPerformerId() {
		return performerId;
	}

	public void setPerformerId(String id) {
		this.performerId = id;
	}

	/**
	 * validate the performer's id,
	 * 
	 * @param id
	 * @return true if the it's a valid id in the database.
	 */
	public boolean validatePerformerId(String id) {
		try {
			Participant p = taskSession.loadParticipant(id);
			if (p != null) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public void deployWorkflowProcess(WorkflowProcess process) {
		getModelSession().deployWorkflowProcess(process);
	}

	/**
	 * Loads a workflow process specified by its id.
	 * 
	 * @param id
	 *            ID of the WorkflowProcess
	 * @param autosave
	 *            registers it for auto-save, in case it will be saved when the
	 *            method close() is called
	 * @return the loaded WorkflowProcess, or null if it dosen't exist.
	 */
	public WorkflowProcess loadWorkflowProcess(long id, boolean autosave) {
		// WorkflowProcess process =
		return null;
	}

	/**
	 * Loads a ProcessInstance specified by its id.
	 * 
	 * @param id
	 *            ID of the ProcessInstance
	 * @param autosave
	 *            registers it for auto-save, in case it will be saved when the
	 *            method close() is called
	 * @return the loaded ProcessInstance, or null if it dosen't exist.
	 */
	public ProcessInstance loadProcessInstance(long id, boolean autosave) {
		return null;
	}

	/**
	 * Loads a Token specified by its id.
	 * 
	 * @param id
	 *            ID of the Token
	 * @param autosave
	 *            registers it for auto-save, in case it will be saved when the
	 *            method close() is called
	 * @return the loaded Token, or null if it dosen't exist.
	 */
	public Token loadToken(long id, boolean autosave) {
		return null;
	}

	public void save(ProcessInstance instance) {
		getModelSession().saveProcessInstance(instance);
	}

	public void save(Token token) {
		getModelSession().saveProcessInstance(token.getProcessInstance());
	}

	private void autosave() {
		if (managedProcessInstances != null) {
			for (ProcessInstance i : managedProcessInstances) {
				System.out.println("get one instance to save" + i.getId());
				getModelSession().saveProcessInstance(i);
			}
		}
	}

	private void addManagedProcessInstance(ProcessInstance instance) {
		if (managedProcessInstances == null) {
			managedProcessInstances = new LinkedList<ProcessInstance>();
		}
		managedProcessInstances.add(instance);
	}
}
