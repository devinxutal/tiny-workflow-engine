package edu.thu.thss.twe.model.graph;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.thu.thss.twe.exception.TweException;

@Entity
@Table(name = "workflow_processes")
public class WorkflowProcess extends WorkflowElement {

	private int version;

	private List<Activity> activities = new LinkedList<Activity>();
	private List<Transition> transitions = new LinkedList<Transition>();
	private List<Participant> participants = new LinkedList<Participant>();
	private List<DataField> dataFileds = new LinkedList<DataField>();

	// /////////////////
	// Getters and Setters
	// /////////////////
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

	// ///////////////////
	// Model Methods
	// ///////////////////

	@Transient
	public Activity getStartActivity() {
		return determineStartActivity();
	}

	@Transient
	public Activity getEndActivity() {
		return determineEndActivity();
	}
	
	
	// ///////////////////
	// Private Methods
	// ///////////////////
	private Activity determineStartActivity() {
		if (activities == null || activities.size() == 0) {
			throw new TweException(
					"couldn't determine start activity: no activity exists");
		}
		Activity start = null;
		for (Activity act : activities) {
			if (act.getArrivingTransitions() == null
					|| act.getArrivingTransitions().size() == 0) {
				// must be the start activity
				if (start != null) {
					// multiple start activity, error
					throw new TweException(
							"couldn't determine start activity: multiple start activity exists");
				}
				start = act;
			}
		}
		if (start == null) {
			throw new TweException("start activity not found");
		}
		return start;
	}

	private Activity determineEndActivity() {
		if (activities == null || activities.size() == 0) {
			throw new TweException(
					"couldn't determine end activity: no activity exists");
		}
		Activity end = null;
		for (Activity act : activities) {
			if (act.getLeavingTransitions() == null
					|| act.getLeavingTransitions().size() == 0) {
				// must be the end activity
				if (end != null) {
					// multiple end activity, error
					throw new TweException(
							"couldn't determine end activity: multiple start activity exists");
				}
				end = act;
			}
		}
		if (end == null) {
			throw new TweException("end activity not found");
		}
		return end;
	}
}
