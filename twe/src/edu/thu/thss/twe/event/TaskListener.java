package edu.thu.thss.twe.event;

/**
 * the interface for listeners which listen the status change of task.
 * 
 * @author Devin
 * 
 */
public interface TaskListener {
	/**
	 * fired after a task is created.
	 * 
	 * @param event
	 */
	void taskCreated(ProcessInstanceEvent event);

	/**
	 * fired after a task is started.
	 * 
	 * @param event
	 */
	void taskStarted(ProcessInstanceEvent event);

	/**
	 * fired after a task is finished.
	 * 
	 * @param event
	 */
	void taskFinished(ProcessInstanceEvent event);

	/**
	 * fired after a task is canceled.
	 * 
	 * @param event
	 */
	void taskCanceled(ProcessInstanceEvent event);
}
