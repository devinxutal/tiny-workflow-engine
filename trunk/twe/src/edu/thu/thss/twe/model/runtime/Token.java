package edu.thu.thss.twe.model.runtime;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.Transition;

@Entity
@Table(name = "tokens")
public class Token {
	long id;

	private Activity currentActivity;
	private ProcessInstance processInstance;
	private Token parent = null;
	private List<Token> children;

	public void signal() {

	}

	public void signal(String transitionName) {

	}

	public void signal(Transition transition) {

	}

	@Transient
	public boolean isRoot() {
		return (parent == null);
	}

	// Geters and Setters
	@Id
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

}
