package edu.thu.thss.twe.model.runtime;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.Participant;
import edu.thu.thss.twe.util.DateUtil;

/**
 * Task is a instance of activity
 * 
 * @author Devin
 * 
 */
@Entity
@Table(name = "tasks")
public class Task {
	long id;
	private Activity activity;
	private Token token;
	private ProcessInstance processInstance;
	private Participant performer;
	private Date createTime;
	private Date startTime;
	private Date finishTime;
	private TaskState state = TaskState.Created;

	public enum TaskState {
		Created, Started, Finished, Canceled
	}

	// ////////////////
	// Getters and Setters
	// ////////////////
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToOne
	@JoinColumn(name = "activity_id")
	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * @return
	 */
	@OneToOne
	@JoinColumn(name = "token_id")
	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@ManyToOne
	@JoinColumn(name = "process_instance_id")
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	@OneToOne
	@JoinColumn(name = "participant_id")
	public Participant getPerformer() {
		return performer;
	}

	public void setPerformer(Participant performer) {
		this.performer = performer;
	}

	@Basic
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Basic
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Basic
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Basic
	public TaskState getState() {
		return state;
	}

	public void setState(TaskState state) {
		this.state = state;
	};

	// //////////////////////
	// Model Methods
	// //////////////////////

	public void start() {
		if (this.getState() != TaskState.Created) {
			throw new TweException("task " + this + " cannot be start again!");
		}
		this.setStartTime(DateUtil.currentTime());
		this.setState(TaskState.Started);
	}

	public void finish() {
		if (this.getState() != TaskState.Started) {
			throw new TweException("task " + this
					+ " cannot be finish, it has finished or not been started!");
		}
		this.setFinishTime(DateUtil.currentTime());
		this.setState(TaskState.Finished);
		this.getToken().signal();
	}
}
