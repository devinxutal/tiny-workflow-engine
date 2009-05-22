package edu.thu.thss.twe.event;

import java.util.LinkedList;
import java.util.List;

public class EventManager {
	private static EventManager eventmanager = new EventManager();

	private List<ProcessInstanceListener> processInstanceListeners;
	private List<ActivityListener> activityListeners;
	private List<TaskListener> taskListeners;

	public static EventManager getEventManager() {
		return eventmanager;
	}

	private EventManager() {
	}

	/**
	 * Add a ProcessInstanceListener which listen the status change of process
	 * instance
	 * 
	 * @param listener
	 */
	public void addProcessInstanceListener(ProcessInstanceListener listener) {
		if (processInstanceListeners == null) {
			processInstanceListeners = new LinkedList<ProcessInstanceListener>();
		}
		processInstanceListeners.add(listener);
	}

	public void removeProcessInstanceListener(ProcessInstanceListener listener) {
		if (processInstanceListeners != null) {
			processInstanceListeners.remove(listener);
		}
	}

	/**
	 * Add a ActivityListener which listen the status change of a
	 * 
	 * @param listener
	 */
	public void addActivityListener(ActivityListener listener) {
		if (activityListeners == null) {
			activityListeners = new LinkedList<ActivityListener>();
		}
		activityListeners.add(listener);
	}

	public void removeActivityListener(ActivityListener listener) {
		if (activityListeners != null) {
			activityListeners.remove(listener);
		}
	}

	/**
	 * Add a TaskListener which listen the status change of task
	 * 
	 * @param listener
	 */
	public void addTaskListener(TaskListener listener) {
		if (taskListeners == null) {
			taskListeners = new LinkedList<TaskListener>();
		}
		taskListeners.add(listener);
	}

	public void removeTaskListener(TaskListener listener) {
		if (taskListeners != null) {
			taskListeners.remove(listener);
		}
	}

	// event fire methods

	// process instance related

	public void fireProcessInstanceCreated(ProcessInstanceEvent event) {
		if (processInstanceListeners != null) {
			for (ProcessInstanceListener l : processInstanceListeners) {
				l.processInstanceCreated(event);
			}
		}
	}

	public void fireProcessInstanceStarted(ProcessInstanceEvent event) {
		if (processInstanceListeners != null) {
			for (ProcessInstanceListener l : processInstanceListeners) {
				l.processInstanceStarted(event);
			}
		}
	}

	public void fireProcessInstanceFinished(ProcessInstanceEvent event) {
		if (processInstanceListeners != null) {
			for (ProcessInstanceListener l : processInstanceListeners) {
				l.processInstanceFinished(event);
			}
		}
	}

	public void fireProcessInstanceDeleted(ProcessInstanceEvent event) {
		if (processInstanceListeners != null) {
			for (ProcessInstanceListener l : processInstanceListeners) {
				l.processInstanceDeleted(event);
			}
		}
	}

	// activity related
	public void fireBeforeActivityEntered(ProcessInstanceEvent event) {
		if (activityListeners != null) {
			for (ActivityListener l : activityListeners) {
				l.beforeActivityEntered(event);
			}
		}
	}

	public void fireAfterActivityEntered(ProcessInstanceEvent event) {
		if (activityListeners != null) {
			for (ActivityListener l : activityListeners) {
				l.afterActivityEntered(event);
			}
		}
	}

	public void fireBeforeActivityExecuted(ProcessInstanceEvent event) {
		if (activityListeners != null) {
			for (ActivityListener l : activityListeners) {
				l.beforeActivityExecuted(event);
			}
		}
	}

	public void fireAfterActivityExecuted(ProcessInstanceEvent event) {
		if (activityListeners != null) {
			for (ActivityListener l : activityListeners) {
				l.afterActivityExecuted(event);
			}
		}
	}

	public void fireBeforeActivityLeaved(ProcessInstanceEvent event) {
		if (activityListeners != null) {
			for (ActivityListener l : activityListeners) {
				l.beforeActivityLeaved(event);
			}
		}
	}

	public void fireAfterActivityLeaved(ProcessInstanceEvent event) {
		if (activityListeners != null) {
			for (ActivityListener l : activityListeners) {
				l.afterActivityLeaved(event);
			}
		}
	}

	// task related
	public void fireTaskCreated(ProcessInstanceEvent event) {
		if (taskListeners != null) {
			for (TaskListener l : taskListeners) {
				l.taskCreated(event);
			}
		}
	}

	public void fireTaskStarted(ProcessInstanceEvent event) {
		if (taskListeners != null) {
			for (TaskListener l : taskListeners) {
				l.taskStarted(event);
			}
		}
	}

	public void fireTaskFinished(ProcessInstanceEvent event) {
		if (taskListeners != null) {
			for (TaskListener l : taskListeners) {
				l.taskFinished(event);
			}
		}
	}

	public void fireTaskCanceled(ProcessInstanceEvent event) {
		if (taskListeners != null) {
			for (TaskListener l : taskListeners) {
				l.taskCanceled(event);
			}
		}
	}

}
