package edu.thu.thss.twe.model.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "activities")
public class Activity extends WorkflowElement {

	private String performer;
	private WorkflowProcess workflowProcess;

	private List<Transition> leavingTransitions = null;
	private Map<String, Transition> leavingTransitionMap = null;
	private Set<Transition> arrivingTransitions = null;

	private TransitionRestriction transitionRestriction;

	@Basic
	public String getPerformer() {
		return performer;
	}

	public void setPerformer(String performer) {
		this.performer = performer;
	}

	@OneToMany(mappedBy = "sourceActivity")
	public List<Transition> getLeavingTransitions() {
		return leavingTransitions;
	}

	public void setLeavingTransitions(List<Transition> leavingTransitions) {
		this.leavingTransitions = leavingTransitions;
	}

	@Transient
	public Map<String, Transition> getLeavingTransitionMap() {
		return leavingTransitionMap;
	}

	@OneToMany(mappedBy = "targetActivity")
	public Set<Transition> getArrivingTransitions() {
		return arrivingTransitions;
	}

	public void setArrivingTransitions(Set<Transition> arrivingTransitions) {
		this.arrivingTransitions = arrivingTransitions;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "transition_restriction_id")
	public TransitionRestriction getTransitionRestriction() {
		return transitionRestriction;
	}

	public void setTransitionRestriction(
			TransitionRestriction transitionRestriction) {
		this.transitionRestriction = transitionRestriction;
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
