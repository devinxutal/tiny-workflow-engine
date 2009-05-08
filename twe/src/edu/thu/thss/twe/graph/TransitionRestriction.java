package edu.thu.thss.twe.graph;

public class TransitionRestriction {
	long id;
	
	private Join join;
	private Split split;

	public TransitionRestriction() {
	}

	public TransitionRestriction(Join join, Split split) {
		this.join = join;
		this.split = split;
	}

	public Join getJoin() {
		return join;
	}

	public void setJoin(Join join) {
		this.join = join;
	}

	public Split getSplit() {
		return split;
	}

	public void setSplit(Split split) {
		this.split = split;
	}
}
