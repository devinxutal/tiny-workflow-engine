package edu.thu.thss.twe.model.graph;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="participants")
public class Participant extends WorkflowElement {


	private WorkflowProcess workflowProcess;
	private int participantType;
	
	@Basic
	public int getParticipantType() {
		return participantType;
	}

	public void setParticipantType(int participantType) {
		this.participantType = participantType;
	}
	

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "workflow_process_id")
	public WorkflowProcess getWorkflowProcess() {
		return workflowProcess;
	}

	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}

}
