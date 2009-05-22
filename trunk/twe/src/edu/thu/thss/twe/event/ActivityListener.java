package edu.thu.thss.twe.event;

/**
 * the interface for listeners which listen the transition status in a process
 * instance.
 * 
 * @author Devin
 * 
 */
public interface ActivityListener {
	/**
	 * fired before entering a activity.
	 * 
	 * @param event
	 */
	void beforeActivityEntered(ProcessInstanceEvent event);

	/**
	 * fired after entering a activity.
	 * 
	 * @param event
	 */
	void afterActivityEntered(ProcessInstanceEvent event);

	/**
	 * fired before executing a activity.
	 * 
	 * @param event
	 */
	void beforeActivityExecuted(ProcessInstanceEvent event);

	/**
	 * fired after executing a activity.
	 * 
	 * @param event
	 */
	void afterActivityExecuted(ProcessInstanceEvent event);

	/**
	 * fired before leaving a activity.
	 * 
	 * @param event
	 */
	void beforeActivityLeaved(ProcessInstanceEvent event);

	/**
	 * fired after leaving a activity.
	 * 
	 * @param event
	 */
	void afterActivityLeaved(ProcessInstanceEvent event);
}
