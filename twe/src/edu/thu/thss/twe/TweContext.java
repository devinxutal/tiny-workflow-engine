package edu.thu.thss.twe;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;

import edu.thu.thss.twe.config.Configuration;
import edu.thu.thss.twe.event.ActivityListener;
import edu.thu.thss.twe.event.EventManager;
import edu.thu.thss.twe.event.ProcessInstanceListener;
import edu.thu.thss.twe.event.TaskListener;
import edu.thu.thss.twe.model.graph.Participant;
import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.model.runtime.ProcessInstance;
import edu.thu.thss.twe.model.runtime.Task;
import edu.thu.thss.twe.model.runtime.Token;
import edu.thu.thss.twe.session.ModelSession;
import edu.thu.thss.twe.session.TaskSession;
import edu.thu.thss.twe.util.HibernateUtility;

public class TweContext {

	private Session session;
	private ModelSession modelSession;
	private TaskSession taskSession;
	private String performerId = null;
	private List<ProcessInstance> managedProcessInstances;
	private Configuration configuration;
	private boolean superMode = false;

	public TweContext(Configuration config, Session session) {
		this.configuration = config;
		this.session = session;
		modelSession = new ModelSession(this.session);
		taskSession = new TaskSession(this.session);
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public ModelSession getModelSession() {
		return modelSession;
	}

	public TaskSession getTaskSession() {
		return taskSession;
	}

	public void close() {
		autosave();
		HibernateUtility.currentSession().flush();
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

	public boolean isSuperMode() {
		return superMode;
	}

	public void setSuperMode(boolean superMode) {
		this.superMode = superMode;
	}

	public boolean authenticate(String username, String password) {
		String adminUserName = this.getConfiguration().getProperty(
				"admin.username");
		String adminPassword = this.getConfiguration().getProperty(
				"admin.password");
		if (username.equals(adminUserName)) {
			if (password.equals(adminPassword)) {
				this.setPerformerId(username);
				this.setSuperMode(true);
				return true;
			} else {
				this.setSuperMode(false);
				this.setPerformerId(null);
				return false;
			}
		} else {
			this.setSuperMode(false);
			boolean tag = validatePerformerId(username);
			if (tag == true) {
				this.setPerformerId(username);
			} else {
				this.setPerformerId(null);
			}
			return tag;
		}
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
		WorkflowProcess process = modelSession.loadWorkflowProcess(id);
		if (process != null && autosave) {
			// TODO
		}
		return process;
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
		ProcessInstance process = modelSession.loadProcessInstance(id);
		if (process != null && autosave) {
			addManagedProcessInstance(process);
		}
		return process;
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
		Token token = modelSession.loadToken(id);
		if (token != null && autosave) {
			addManagedProcessInstance(token.getProcessInstance());
		}
		return token;
	}

	public void save(ProcessInstance instance) {
		getModelSession().saveProcessInstance(instance);
	}

	public void save(Token token) {
		getModelSession().saveProcessInstance(token.getProcessInstance());
	}

	/**
	 * Add a ProcessInstanceListener which listen the status change of process
	 * instance
	 * 
	 * @param listener
	 */
	public void addProcessInstanceListener(ProcessInstanceListener listener) {
		EventManager.getEventManager().addProcessInstanceListener(listener);
	}

	public void removeProcessInstanceListener(ProcessInstanceListener listener) {
		EventManager.getEventManager().removeProcessInstanceListener(listener);
	}

	/**
	 * Add a ActivityListener which listen the status change of activity
	 * 
	 * @param listener
	 */
	public void addActivityListener(ActivityListener listener) {
		EventManager.getEventManager().addActivityListener(listener);
	}

	public void removeActivityListener(ActivityListener listener) {
		EventManager.getEventManager().removeActivityListener(listener);
	}

	/**
	 * Add a TaskListener which listen the status change of task
	 * 
	 * @param listener
	 */
	public void addTaskListener(TaskListener listener) {
		EventManager.getEventManager().addTaskListener(listener);
	}

	public void removeTaskListener(TaskListener listener) {
		EventManager.getEventManager().removeTaskListener(listener);
	}

	// ///////////////
	// private methods
	// ///////////////
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
