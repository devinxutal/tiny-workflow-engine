package edu.thu.thss.twe.runtime;

import java.util.Date;

import edu.thu.thss.twe.graph.Transition;
import edu.thu.thss.twe.graph.WorkflowProcess;

public class ProcessInstance {
	long id;

	private WorkflowProcess workflowProcess;
	private Token rootToken;
	private Date startTime;
	private Date endTime;

	public ProcessInstance() {

	}

	public ProcessInstance(WorkflowProcess process) {
		workflowProcess = process;
	}

	public void signal() {
		rootToken.signal();
	}

	public void signal(String transitionName) {
		rootToken.signal(transitionName);
	}

	public void signal(Transition transition) {
		rootToken.signal(transition);
	}
}
