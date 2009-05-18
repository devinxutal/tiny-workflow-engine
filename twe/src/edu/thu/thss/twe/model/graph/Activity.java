package edu.thu.thss.twe.model.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.runtime.ExecutionContext;
import edu.thu.thss.twe.model.runtime.Token;

@Entity
@Table(name = "activities")
public class Activity extends WorkflowElement {

	private String performer;
	private WorkflowProcess workflowProcess;

	private List<Transition> leavingTransitions = null;
	private Map<String, Transition> leavingTransitionMap = null;
	private List<Transition> arrivingTransitions = null;

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

	@OneToMany(mappedBy = "targetActivity")
	public List<Transition> getArrivingTransitions() {
		return arrivingTransitions;
	}

	public void setArrivingTransitions(List<Transition> arrivingTransitions) {
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

	// /////////////
	// Model Methods
	// /////////////

	public boolean hasJoin() {
		return ((transitionRestriction != null) && (transitionRestriction
				.getJoin() != null));
	}

	public boolean hasSplit() {
		return ((transitionRestriction != null) && (transitionRestriction
				.getSplit() != null));
	}

	public void addArrivingTransition(Transition t) {
		if (arrivingTransitions == null) {
			arrivingTransitions = new LinkedList<Transition>();
		}
		arrivingTransitions.add(t);
		t.setTargetActivity(this);
	}

	public void addLeavingTransition(Transition t) {
		if (leavingTransitions == null) {
			leavingTransitions = new LinkedList<Transition>();
		}
		leavingTransitions.add(t);
		t.setSourceActivity(this);
		// update the leavingTransitionMap
		if (leavingTransitionMap != null) {
			leavingTransitionMap.put(t.getElementId(), t);
		}
	}

	public void enter(ExecutionContext context) {
		context.getToken().setCurrentActivity(this);
		context.setTransition(null);
		context.setSourceActivity(null);
		// if this activity has a join
		if (hasJoin()) {
			Token joinedToken = handleJoin(context);
			if (joinedToken != null) {
				ExecutionContext newContext = new ExecutionContext(joinedToken);
				execute(newContext);
			}
		} else {
			// execute the activity
			execute(context);
		}

	}

	public void execute(ExecutionContext context) {
		// empty method, because we do not support automatic activity.

		// determine whether to leave
		if (isRoute()) {
			leave(context);
		}
	}

	public void leave(ExecutionContext context) {
		if (hasJoin()) {
			handleSplit(context);
		} else {
			leave(context, getLeavingTransition(context));
		}
	}

	public void leave(ExecutionContext context, String transitionElementId) {
		leave(context, getLeavingTransition(transitionElementId));
	}

	public void leave(ExecutionContext context, Transition transition) {
		if (transition == null) {
			throw new TweException(
					"cannot leave this activity: the leaving transition is null");
		}
		if (leavingTransitions == null
				|| !leavingTransitions.contains(transition)) {
			throw new TweException(
					"cannot leave this activity: the transition is not a leaving transition of this activity");
		}
		context.getToken().setCurrentActivity(this);
		context.setTransition(transition);
		context.setSourceActivity(this);
		transition.tranfer(context);
	}

	/**
	 * Gets the map of all leaving transitions, key is the element id of the
	 * transition
	 * 
	 * @return
	 */
	@Transient
	public Map<String, Transition> getLeavingTransitionMap() {
		if (leavingTransitionMap == null)
			leavingTransitionMap = new HashMap<String, Transition>();
		if (leavingTransitions != null) {
			for (Transition t : leavingTransitions) {
				leavingTransitionMap.put(t.getElementId(), t);
			}
		}
		return leavingTransitionMap;
	}

	/**
	 * Gets the default leaving transition, a default leaving transition is the
	 * first leaving transition without a condition
	 * 
	 * @return
	 */
	@Transient
	public Transition getDefaultLeavingTransition() {
		Transition defaultTransition = null;
		if (leavingTransitions != null) {
			for (Transition t : leavingTransitions) {
				if (t.getCondition() == null) {
					defaultTransition = t;
				}
			}
		}
		return defaultTransition;
	}

	/**
	 * gets the leaving transition according to the current execution context.
	 * 
	 * @param context
	 *            the current execution context
	 * @return the leaving transition
	 */
	@Transient
	public Transition getLeavingTransition(ExecutionContext context) {
		return determineLeavingTransition(context);
	}

	@Transient
	public Transition getLeavingTransition(String transitionElementId) {
		return getLeavingTransitionMap().get(transitionElementId);
	}

	/**
	 * get available leaving transitions, if this activity has a split element,
	 * there may be more than one available leaving transition.
	 * 
	 * @param context
	 * @return
	 */
	@Transient
	public List<Transition> getAvailableLeavingTransitions(
			ExecutionContext context) {
		List<Transition> list = new LinkedList<Transition>();
		// TODO get available leaving transitions
		return list;
	}

	/**
	 * determine whether it is a route activity or concrete activity.
	 * 
	 * @return true if it is just a route activity
	 */
	@Transient
	public boolean isRoute() {
		return (this.getPerformer() == null);
	}

	/**
	 * determines the leaving transition according to the current execution
	 * context. if there is default leaving transition, then the default one
	 * will be returned. otherwise, the leaving transition will be determined by
	 * the condition of the transitions.
	 * 
	 * @param context
	 *            the current execution context
	 * @return the leaving transition
	 */
	private Transition determineLeavingTransition(ExecutionContext context) {
		Transition leavingTransition = null;
		leavingTransition = getDefaultLeavingTransition();
		if (leavingTransition == null) {
			leavingTransition = determineLeavingTransitionByCondition(context);
		}
		return leavingTransition;
	}

	/**
	 * determines the leaving transition according to the current execution
	 * context. the leaving transition will be determined by the condition of
	 * the transitions.
	 * 
	 * @param context
	 *            the current execution context
	 * @return the leaving transition
	 */
	private Transition determineLeavingTransitionByCondition(
			ExecutionContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * handle the join action, if join is successfully performed. the joined
	 * token will be returned. notice that the joined token may be either the
	 * parent token or a new child token.
	 * 
	 * @param context
	 * @return
	 */
	private Token handleJoin(ExecutionContext context) {
		if (!hasJoin()) {
			throw new TweException(
					"cannot perform join: this activity dosen't have a join");
		}
		Token token = context.getToken();
		Token parentToken = token.getParent();
		if (transitionRestriction.getJoin().getJoinType() == Constants.JOIN_TYPE_XOR) {
			// finish the join
			if (reactivateParentTokenNeeded(context)) {
				obsoleteChildren(parentToken);
				parentToken.getChildren().clear();
				parentToken.setChildren(null);
				parentToken.setState(Token.TokenState.Active);
				return parentToken;
			}else{
				token.setState(Token.TokenState.Obsolete);
				parentToken.getChildren().remove(token);
				Token newChildToken = new Token(parentToken, this.getElementId());
				newChildToken.setCurrentActivity(this);
				return newChildToken;
			}
		} else if (transitionRestriction.getJoin().getJoinType() == Constants.JOIN_TYPE_AND) {
			// if all the join tokens arrive , finish the join
			if (allJoinTokensArrived(context)) {
				/**
				 * 如果到达的transition数目等于父Token的子token数，
				 * 说明父Token所有的分支已经到达。可以不用子token了，
				 * 否则，说明其一部分子token汇聚，可用一个新的子token来 替代这些token。
				 */
				if (reactivateParentTokenNeeded(context)) {
					// TODO better way to delete child tokens
					parentToken.getChildren().clear();
					parentToken.setChildren(null);
					// TODO delete the tokens from database?
					return parentToken;
				} else {
					// delete all the arrived tokens
					List<Token> tokensToDelete = new LinkedList<Token>();
					for (Token childToken : parentToken.getChildren()) {
						if (childToken.getCurrentActivity().equals(this)) {
							tokensToDelete.add(childToken);
							childToken.setState(Token.TokenState.Obsolete);
							// TODO delete the token from database?
						}
					}
					parentToken.getChildren().removeAll(tokensToDelete);
					// create a new child token to substitute the arrived tokens
					Token newChildToken = new Token(parentToken, this
							.getElementId());
					newChildToken.setCurrentActivity(this);
					return newChildToken;
				}
			} else {// otherwise, wait for other tokens.

			}
		}
		return null;
	}

	private void obsoleteChildren(Token parentToken) {
		if(parentToken == null || parentToken.getChildren() ==  null){
			return;
		}
		for(Token t: parentToken.getChildren()){
			t.setState(Token.TokenState.Obsolete);
		}
	}

	private void handleSplit(ExecutionContext context) {
		if (!hasSplit()) {
			throw new TweException(
					"cannot perform split: this activity dosen't have a join");
		}
		Token token = context.getToken();
		// get all available leaving transitions
		List<Transition> alts = this.getAvailableLeavingTransitions(context);

		// create a token for each transition and launch the token.
		for (Transition transition : alts) {
			Token childToken = new Token(token, transition.getElementId());
			ExecutionContext childContext = new ExecutionContext(childToken);
			leave(childContext, transition);
		}
		token.setState(Token.TokenState.Inactive);
	}

	private boolean allJoinTokensArrived(ExecutionContext context) {
		Token token = context.getToken();
		Token parent = token.getParent();
		int arrivedNum = 0;
		for (Token childToken : parent.getChildren()) {
			if (childToken.getCurrentActivity().equals(this)) {
				arrivedNum++;
			}
		}
		if (arrivedNum == this.getArrivingTransitions().size()) {
			return true;
		}
		return false;
	}

	private boolean reactivateParentTokenNeeded(ExecutionContext context) {
		/**
		 * 如果到达的transition数目等于父Token的子token数， 说明父Token所有的分支已经到达。可以不用子token了，
		 * 否则，说明其一部分子token汇聚，可用一个新的子token来 替代这些token。
		 */
		return (context.getToken().getParent().getChildren().size() == this
				.getArrivingTransitions().size());
	}
}
