package edu.thu.thss.twe.session;

import java.util.Collection;
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

	private static String find_all_tasks = "from Task";
	private static String find_tasks_by_process_instance_id = "from Task where process_instance_id=:iid";
	private static String find_tasks_by_performer_id = "from Task where participant_id=:pid";
	private static String find_tasks_by_process_instance_id_and_performer_id = "from Task where process_instance_id=:iid and participant_id=:pid";

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
			return findTasks(loadParticipant(performerId));
		} catch (Exception e) {

			throw new TweException("could not find all task tasks", e);
		}
	}

	public List<Task> findTasks(Participant performer) {
		if (performer == null) {
			throw new TweException("could not find tasks with a null performer");
		}
		try {
			Query query = session.createQuery(find_tasks_by_performer_id);
			query.setLong(0, performer.getId());
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
			query.setLong(0, instance.getId());
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
			query.setLong(0, instance.getId());
			query.setLong(1, performer.getId());
			List<?> tasks = query.list();
			return CollectionUtil.checkList(tasks, Task.class);
		} catch (Exception e) {
			throw new TweException("could not find tasks", e);
		}
	}

	public Participant loadParticipant(String elementId) {
		try {
			String queryStr = "from Participant where elementId=:id";
			Query query = session.createQuery(queryStr);
			query.setString("id", elementId);
			List<?> participants = query.list();
			if (participants.size() > 1) {
				throw new TweException("duplicate participants with elementId="
						+ elementId);
			}
			if (participants.size() == 0) {
				return null;
			}
			return (Participant) participants.get(0);
		} catch (Exception e) {

			throw new TweException("cannot find participant with elementId="
					+ elementId, e);
		}
	}

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
