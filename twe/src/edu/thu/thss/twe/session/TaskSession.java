package edu.thu.thss.twe.session;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.graph.Participant;
import edu.thu.thss.twe.model.runtime.ProcessInstance;
import edu.thu.thss.twe.model.runtime.Task;
import edu.thu.thss.twe.util.CollectionUtil;

public class TaskSession {
	private Session session;

	private static String order_by_string = " order by process_instance_id asc, state desc";
	private static String find_all_tasks = "from Task" + order_by_string;
	private static String find_tasks_by_process_instance_id = "from Task where process_instance_id=:iid"
			+ order_by_string;
	private static String find_tasks_by_performer_id = "from Task where participant_id=:pid"
			+ order_by_string;
	private static String find_tasks_by_process_instance_id_and_performer_id = "from Task where process_instance_id=:iid and participant_id=:pid"
			+ order_by_string;

	public TaskSession(Session s) {
		session = s;
	}

	/**
	 * save a Task
	 * 
	 * @param task
	 *            the Task to be saved;
	 */
	public void saveTask(Task task) {
		save(task);
	}

	/**
	 * delete a task
	 * 
	 * @param id
	 *            ID of the Task to be deleted;
	 */
	public void deleteTask(long id) {
		deleteTask(loadTask(id));
	}

	/**
	 * delete a task
	 * 
	 * @param task
	 *            the Task to be deleted
	 */
	public void deleteTask(Task task) {
		if (task == null) {
			throw new IllegalArgumentException("Task cannot be null");
		}

		try {
			session.beginTransaction();
			session.delete(task);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw new TweException("could not delete " + task, e);
		}
	}

	/**
	 * loads a task from the database by the identifier.
	 * 
	 * @return the referenced task or null in case it doesn't exist.
	 */
	public Task loadTask(long id) {
		try {
			return (Task) session.get(Task.class, new Long(id));
		} catch (Exception e) {

			throw new TweException("could not get task" + id, e);
		}
	}

	/**
	 * queries the database for all the tasks. Tasks are distinct by its id.
	 */
	public List<Task> findAllTasks() {
		try {
			List<?> taskProcesss = session.createQuery(find_all_tasks).list();
			return CollectionUtil.checkList(taskProcesss, Task.class);
		} catch (Exception e) {

			throw new TweException("could not find all tasks", e);
		}
	}

	/**
	 * queries the database for a group of tasks by their ids.
	 * 
	 * @param taskIds
	 * @return
	 */
	public List<Task> findTasks(Collection<Long> taskIds) {
		List<?> taskProcesss = session.createCriteria(Task.class).add(
				Restrictions.in("id", taskIds)).list();
		return CollectionUtil.checkList(taskProcesss, Task.class);
	}

	/**
	 * queries the database for tasks of a certain performer
	 * 
	 * @param performerId
	 *            the unique element id of the performer
	 */
	public List<Task> findTasks(String performerId) {
		try {
			List<Task> tsks = new LinkedList<Task>();
			for (Participant p : findParticipants(performerId)) {
				Query query = session.createQuery(find_tasks_by_performer_id);
				query.setLong("pid", p.getId());
				List<?> tasks = query.list();
				tsks.addAll(CollectionUtil.checkList(tasks, Task.class));
			}
			return tsks;
		} catch (Exception e) {

			throw new TweException("could not find tasks", e);
		}
	}

	public List<Task> findTasks(Participant performer) {
		if (performer == null) {
			throw new TweException("could not find tasks with a null performer");
		}
		try {
			Query query = session.createQuery(find_tasks_by_performer_id);
			query.setLong("pid", performer.getId());
			List<?> tasks = query.list();
			return CollectionUtil.checkList(tasks, Task.class);
		} catch (Exception e) {

			throw new TweException("could not find tasks", e);
		}
	}

	public List<Task> findTasks(ProcessInstance instance) {
		if (instance == null) {
			throw new TweException(
					"could not find tasks with a null process instance");
		}
		try {
			Query query = session
					.createQuery(find_tasks_by_process_instance_id);
			query.setLong("iid", instance.getId());
			List<?> tasks = query.list();
			return CollectionUtil.checkList(tasks, Task.class);
		} catch (Exception e) {
			throw new TweException("could not find tasks", e);
		}
	}

	public List<Task> findTasks(ProcessInstance instance, Participant performer) {
		if (instance == null) {
			throw new TweException(
					"could not find tasks with a null process instance");
		}
		if (performer == null) {
			throw new TweException("could not find tasks with a null performer");
		}
		try {
			Query query = session
					.createQuery(find_tasks_by_process_instance_id_and_performer_id);
			query.setLong("iid", instance.getId());
			query.setLong(1, performer.getId());
			List<?> tasks = query.list();
			return CollectionUtil.checkList(tasks, Task.class);
		} catch (Exception e) {
			throw new TweException("could not find tasks", e);
		}
	}

	/**
	 * load a participant by its element id
	 * 
	 * @param elementId
	 * @return the first participant with the element id
	 */
	public Participant loadParticipant(String elementId) {
		try {
			String queryStr = "from Participant where elementId=:id";
			Query query = session.createQuery(queryStr);
			query.setString("id", elementId);
			List<?> participants = query.list();
			// if (participants.size() > 1) {
			// throw new TweException("duplicate participants with elementId="
			// + elementId);
			// }
			if (participants.size() == 0) {
				return null;
			}
			return (Participant) participants.get(0);
		} catch (Exception e) {

			throw new TweException("cannot find participant with elementId="
					+ elementId, e);
		}
	}

	/**
	 * load all participant with this element id
	 * 
	 * @param elementId
	 * @return all participant with this element id
	 */
	public List<Participant> findParticipants(String elementId) {
		try {
			String queryStr = "from Participant where elementId=:id";
			Query query = session.createQuery(queryStr);
			query.setString("id", elementId);
			List<?> participants = query.list();
			return CollectionUtil.checkList(participants, Participant.class);
		} catch (Exception e) {

			throw new TweException("cannot find participant with elementId="
					+ elementId, e);
		}
	}

	/**
	 * load a participant by its id.
	 * 
	 * @param id
	 * @return
	 */
	public Participant loadParticipant(long id) {
		try {
			return (Participant) session.get(Participant.class, new Long(id));
		} catch (Exception e) {

			throw new TweException("could not get participant" + id, e);
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
