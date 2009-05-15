package edu.thu.thss.twe.model.graph;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "workflow_processes")
public class WorkflowProcess extends WorkflowElement {

	private int version;

	private List<Activity> activities;
	private List<Transition> transitions;
	private List<Participant> participants;
	private List<DataField> dataFileds;

	@Basic
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@OneToMany(mappedBy = "workflowProcess")
	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	@OneToMany(mappedBy = "workflowProcess")
	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	@OneToMany(mappedBy = "workflowProcess")
	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	@OneToMany(mappedBy = "workflowProcess")
	public List<DataField> getDataFileds() {
		return dataFileds;
	}

	public void setDataFileds(List<DataField> dataFileds) {
		this.dataFileds = dataFileds;
	}
}
