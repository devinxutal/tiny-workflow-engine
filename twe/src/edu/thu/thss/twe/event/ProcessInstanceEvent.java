package edu.thu.thss.twe.event;

import edu.thu.thss.twe.model.runtime.ProcessInstance;
import edu.thu.thss.twe.model.runtime.Task;
import edu.thu.thss.twe.model.runtime.Token;

public class ProcessInstanceEvent {
	private ProcessInstance processInstance;
	private Task task;
	private Token token;

	public ProcessInstanceEvent() {

	}

	public ProcessInstanceEvent(ProcessInstance processInstance) {
		super();
		this.processInstance = processInstance;
	}

	public ProcessInstanceEvent(ProcessInstance processInstance, Token token) {
		super();
		this.processInstance = processInstance;
		this.token = token;
	}

	public ProcessInstanceEvent(ProcessInstance processInstance, Task task,
			Token token) {
		super();
		this.processInstance = processInstance;
		this.task = task;
		this.token = token;
	}

	public ProcessInstanceEvent(ProcessInstance processInstance, Task task) {
		super();
		this.processInstance = processInstance;
		this.task = task;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

}
