package edu.thu.thss.twe.model.runtime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.thu.thss.twe.event.EventManager;
import edu.thu.thss.twe.event.ProcessInstanceEvent;
import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.DataField;
import edu.thu.thss.twe.model.graph.Participant;
import edu.thu.thss.twe.model.graph.Submission;
import edu.thu.thss.twe.util.DataTypeUtil;
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
		// fire event
		EventManager.getEventManager().fireTaskStarted(
				new ProcessInstanceEvent(this.getProcessInstance(), this, this
						.getToken()));
	}

	public void finish() {
		if (this.getState() != TaskState.Started) {
			throw new TweException("task " + this
					+ " cannot be finish, it has finished or not been started!");
		}
		if (!canFinish()) {
			throw new TweException("task " + this
					+ " cannot be finish! submission needed");
		}
		this.setFinishTime(DateUtil.currentTime());
		this.setState(TaskState.Finished);
		// fire event
		EventManager.getEventManager().fireTaskFinished(
				new ProcessInstanceEvent(this.getProcessInstance(), this, this
						.getToken()));

		//
		this.getToken().signal();
	}

	public void submitVariables(Map<Submission, String> submissions) {
		if (this.getActivity().getSubmissions() == null) {
			if (submissions.size() != 0) {
				throw new TweException(
						"cannot submit variables, no submission needed");
			}
			return;
		}
		// prepare datafield -> variable map
		Map<DataField, Variable> variableMap = new HashMap<DataField, Variable>();
		for (Variable v : getProcessInstance().getVariables()) {
			variableMap.put(v.getDataField(), v);
		}
		// submit variables
		for (Map.Entry<Submission, String> entry : submissions.entrySet()) {
			Submission submission = entry.getKey();
			if (!this.getActivity().getSubmissions().contains(submission)) {
				throw new TweException("cannot submit variable "
						+ submission.getDataField().getName()
						+ ", it's not is the submission list");
			}
			DataField df = entry.getKey().getDataField();
			String value = entry.getValue();
			Variable v = variableMap.get(df);
			if (v == null) {
				throw new TweException("cannot submit variables, variable "
						+ df.getName() + ", it's not in the submission list");
			}
			if (DataTypeUtil.isValidValue(df.getDataType(), value)) {
				v.setValue(value);
			} else {
				throw new TweException("cannot submit variables, variable "
						+ df.getName() + ", invalid value");
			}

		}
	}

	public boolean canFinish() {
		return allVariablesSubmitted();
	}

	/**
	 * determine whether this task need to submit some variable, and these
	 * submissions are enforced.
	 * 
	 * @return
	 */
	@Transient
	public boolean isSubmissionEnforced() {
		if (this.getActivity().getSubmissions() == null)
			return false;
		for (Submission s : this.getActivity().getSubmissions()) {
			if (s.isForce()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * determine whether this task need to submit some variable.
	 * 
	 * @return
	 */
	@Transient
	public boolean isSubmissionNeeded() {

		return (this.getActivity().getSubmissions() != null && this.getActivity().getSubmissions().size()>0);
	}

	private boolean allVariablesSubmitted() {
		// TODO
		return true;
	}
}
