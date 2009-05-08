package edu.thu.thss.twe.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Activity extends WorkflowElemnt {
	
	private String performer;
	
	
	private List<Transition> leavingTransitions = null;
	private Map<String, Transition> leavingTransitionMap = null;
	private Set<Transition> arrivingTransitions = null;
}
