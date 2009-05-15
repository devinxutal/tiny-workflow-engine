package edu.thu.thss.twe.model.runtime;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.thu.thss.twe.model.graph.Transition;
import edu.thu.thss.twe.model.graph.WorkflowProcess;

@Entity
@Table(name = "process_instances")
public class ProcessInstance {
	long id;

	private String key;
	private WorkflowProcess workflowProcess;
	private Token rootToken;
	private Date startTime;
	private Date endTime;

	@Id
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Basic
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "workflow_process_id")
	public WorkflowProcess getWorkflowProcess() {
		return workflowProcess;
	}

	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "root_token_id")
	public Token getRootToken() {
		return rootToken;
	}

	public void setRootToken(Token rootToken) {
		this.rootToken = rootToken;
	}

	@Basic
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Basic
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
