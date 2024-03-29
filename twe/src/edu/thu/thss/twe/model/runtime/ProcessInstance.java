package edu.thu.thss.twe.model.runtime;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.thu.thss.twe.event.EventManager;
import edu.thu.thss.twe.event.ProcessInstanceEvent;
import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.DataField;
import edu.thu.thss.twe.model.graph.Transition;
import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.util.DateUtil;

@Entity
@Table(name = "process_instances")
public class ProcessInstance {
	long id;

	private String instanceName;
	private WorkflowProcess workflowProcess;
	private Token rootToken;
	private Date startTime;
	private Date endTime;
	private List<Task> tasks;
	private List<Variable> variables;
	private InstanceState state = InstanceState.Created;

	public enum InstanceState {
		Created, Started, Finished
	};

	public ProcessInstance(WorkflowProcess process, String instanceName) {
		if (process == null)
			throw new TweException(
					"error while creating process instance: invalid workflow process.");

		workflowProcess = process;
		this.instanceName = instanceName;
		this.rootToken = new Token(this);
		prepareVariables();
		// startProcess(rootToken.getCurrentActivity());
		// fire event
		EventManager.getEventManager().fireProcessInstanceCreated(
				new ProcessInstanceEvent(this));
	}

	// //////////////////////////
	// Getters and Setters
	// //////////////////////////
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Basic
	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	@OneToOne
	@JoinColumn(name = "workflow_process_id")
	public WorkflowProcess getWorkflowProcess() {
		return workflowProcess;
	}

	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "root_token_id")
	public Token getRootToken() {
		return rootToken;
	}

	public void setRootToken(Token rootToken) {
		this.rootToken = rootToken;
	}

	@Basic
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Basic
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@OneToMany(mappedBy = "processInstance", cascade = CascadeType.ALL)
	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	@OneToMany(mappedBy = "processInstance", cascade = CascadeType.ALL)
	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	@Basic
	public InstanceState getState() {
		return state;
	}

	public void setState(InstanceState state) {
		this.state = state;
	}

	// ////////////////////////
	// Model Methods
	// ////////////////////////

	public ProcessInstance() {

	}

	/**
	 * signal the root token to leave the current activity and move forward.
	 */
	public void signal() {
		rootToken.signal();
	}

	/**
	 * signal the root token to leave the current activity and move forward
	 * along the specified transition.
	 * 
	 * @deprecated
	 */
	public void signal(String transitionName) {
		rootToken.signal(transitionName);
	}

	/**
	 * signal the root token to leave the current activity and move forward
	 * along the specified transition.
	 * 
	 * @deprecated
	 */
	public void signal(Transition transition) {
		rootToken.signal(transition);
	}

	/**
	 * add a task to this instance
	 * 
	 * @param task
	 *            the task to be added��
	 */
	public void addTask(Task task) {
		if (this.tasks == null) {
			tasks = new LinkedList<Task>();
		}
		tasks.add(task);
		task.setProcessInstance(this);
	}

	/**
	 * remove a task from this instance
	 * 
	 * @param task
	 *            the task to be removed.
	 */
	public void removeTask(Task task) {
		task.setProcessInstance(null);
		if (this.tasks == null) {
			return;
		}
		this.tasks.remove(task);
	}

	/**
	 * add a variable to the process instance.
	 * 
	 * @param v
	 *            the variable to be added.
	 */
	public void addVariable(Variable v) {
		if (this.variables == null) {
			variables = new LinkedList<Variable>();
		}
		this.variables.add(v);
		v.setProcessInstance(this);
	}

	/**
	 * remove a variable
	 * 
	 * @param v
	 *            the variable to be removed
	 */
	public void removeVariable(Variable v) {
		v.setProcessInstance(null);
		if (this.variables == null) {
			return;
		}
		variables.remove(v);
	}

	/**
	 * start this process. this will make the root token to move forward to the
	 * first activity.
	 */
	public void start() {
		startProcess(this.getWorkflowProcess().getStartActivity());
		this.state = InstanceState.Started;
		// fire event
		EventManager.getEventManager().fireProcessInstanceCreated(
				new ProcessInstanceEvent(this));
	}

	/**
	 * end this instance;
	 */
	public void end() {
		if (this.getRootToken().getCurrentActivity() != this
				.getWorkflowProcess().getEndActivity()) {
			throw new TweException(
					"cannot end this process instance, token hasn't arrived end activity yet");
		}
		// check tasks
		if (this.getTasks() != null) {
			for (Task task : getTasks()) {
				if (task.getState() != Task.TaskState.Finished) {
					throw new TweException(
							"cannot end this process instance, task not finished.");
				}
			}
		}
		// end
		this.endTime = DateUtil.currentTime();
		this.state = InstanceState.Finished;
		// fire event
		EventManager.getEventManager().fireProcessInstanceCreated(
				new ProcessInstanceEvent(this));
	}

	// /////////////////
	// Private Methods
	// /////////////////
	private void prepareVariables() {
		if (workflowProcess == null || workflowProcess.getDataFileds() == null) {
			return;
		}
		for (DataField d : workflowProcess.getDataFileds()) {
			Variable v = new Variable(d);
			v.setValue(d.getInitialValue());
			this.addVariable(v);
		}
	}

	private void startProcess(Activity startActivity) {
		this.startTime = DateUtil.currentTime();
		if (startActivity != null) {
			ExecutionContext context = new ExecutionContext(rootToken);
			rootToken.setCurrentActivity(startActivity);
			startActivity.execute(context);
		} else {
			throw new TweException(
					"cannot start instance, couldn't get start activity");
		}
	}

}
