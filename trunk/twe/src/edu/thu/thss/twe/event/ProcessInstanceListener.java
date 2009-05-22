package edu.thu.thss.twe.event;

/**
 * the interface for listeners which listen the status change of process
 * instance
 * 
 * @author Devin
 * 
 */
public interface ProcessInstanceListener {

	/**
	 * fired after a process instance is created.
	 * 
	 * @param event
	 */
	void processInstanceCreated(ProcessInstanceEvent event);

	/**
	 * fired after a process instance is started.
	 * 
	 * @param event
	 */
	void processInstanceStarted(ProcessInstanceEvent event);

	/**
	 * fired after a process instance is finished.
	 * 
	 * @param event
	 */
	void processInstanceFinished(ProcessInstanceEvent event);

	/**
	 * fired after a process instance is deleted.
	 * 
	 * @param event
	 */
	void processInstanceDeleted(ProcessInstanceEvent event);

}
