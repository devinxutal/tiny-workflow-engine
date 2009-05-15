package edu.thu.thss.twe.model.runtime;

import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.Transition;

public class ExecutionContext {
	private ProcessInstance processInstance;
	private Token token;
	private Activity sourceActivity;
	private Transition transition;

	private ExecutionContext(Token token) {
		this.token = token;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public Activity getSourceActivity() {
		return sourceActivity;
	}

	public void setSourceActivity(Activity sourceActivity) {
		this.sourceActivity = sourceActivity;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}

}
