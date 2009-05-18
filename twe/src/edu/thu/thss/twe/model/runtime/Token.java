package edu.thu.thss.twe.model.runtime;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.Transition;

@Entity
@Table(name = "tokens")
public class Token {
	long id;

	private String name;
	private Activity currentActivity;
	private ProcessInstance processInstance;
	private Token parent = null;
	private List<Token> children;
	private Map<String, Token> childrenMap;
	private TokenState state;

	/**
	 * used to indicate the current status of the token Active: active state
	 * Inactive: in active state, a parent token is usually inactive when its
	 * child tokens are active. Obsolete: indicate that this token has finish
	 * its job and been abandoned.
	 * 
	 * @author Devin
	 * 
	 */
	public enum TokenState {
		Active, Inactive, Obsolete
	};

	public Token(ProcessInstance processInstance) {
		this.processInstance = processInstance;
		this.parent = null;
		this.currentActivity = processInstance.getWorkflowProcess()
				.getStartActivity();
	}

	public Token(Token parentToken, String tokenName) {
		this.processInstance = parentToken.getProcessInstance();
		this.parent = parentToken;
		this.name = tokenName;
		parentToken.addChild(this);
	}

	public void addChild(Token token) {
		if (children == null) {
			children = new LinkedList<Token>();

		}
		children.add(token);
		getChildrenMap().put(token.getName(), token);
	}

	@Transient
	private Map<String, Token> getChildrenMap() {
		if (childrenMap == null) {
			childrenMap = new HashMap<String, Token>();
		}
		if (children != null) {
			for (Token token : children) {
				childrenMap.put(token.getName(), token);
			}
		}
		return childrenMap;
	}

	// ////////////////////
	// Geters and Setters
	// ///////////////////
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Basic
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "current_activity_id")
	public Activity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(Activity currentActivity) {
		this.currentActivity = currentActivity;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "process_instance_id")
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "parent_id")
	public Token getParent() {
		return parent;
	}

	public void setParent(Token parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent")
	public List<Token> getChildren() {
		return children;
	}

	public void setChildren(List<Token> children) {
		this.children = children;
	}

	@Basic
	public TokenState getState() {
		return state;
	}

	public void setState(TokenState state) {
		this.state = state;
	}

	// //////////////////////
	// Model Methods
	// //////////////////////

	public void signal() {
		if (currentActivity == null) {
			throw new TweException(
					"token cannot be signalled, current activity is null");
		}
		ExecutionContext context = new ExecutionContext(this);
		signal(currentActivity.getLeavingTransition(context), context);
	}

	public void signal(String transitionElementId) {
		if (currentActivity == null) {
			throw new TweException(
					"token cannot be signalled, current activity is null");
		}
		signal(currentActivity.getLeavingTransition(transitionElementId),
				new ExecutionContext(this));
	}

	public void signal(Transition transition) {
		if (currentActivity == null) {
			throw new TweException(
					"token cannot be signalled, current activity is null");
		}
		signal(transition, new ExecutionContext(this));
	}

	private void signal(Transition transition, ExecutionContext context) {
		if (transition == null) {
			throw new TweException("cannot signal, transition is null");
		}
		if (currentActivity == null) {
			throw new TweException("cannot signal, current activity is null");
		}
		if (currentActivity.getLeavingTransitions() == null
				|| !currentActivity.getLeavingTransitions()
						.contains(transition)) {
			throw new TweException(
					"cannot signal, current activity dosen't contrain the transition");
		}
		currentActivity.leave(context, transition);
	}

	@Transient
	public boolean isRoot() {
		return (parent == null);
	}

}
