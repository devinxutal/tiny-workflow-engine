package edu.thu.thss.twe.model.graph;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.thu.thss.twe.eval.Evaluator;
import edu.thu.thss.twe.eval.evaluator.BasicEvaluator;
import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.runtime.ExecutionContext;

@Entity
@Table(name = "transitions")
public class Transition extends WorkflowElement {

	private WorkflowProcess workflowProcess;
	private Activity sourceActivity;
	private Activity targetActivity;
	private Condition condition;

	// ///////////////////
	// Getters and Setters
	// ///////////////////

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "source_activity_id")
	public Activity getSourceActivity() {
		return sourceActivity;
	}

	public void setSourceActivity(Activity sourceActivity) {
		this.sourceActivity = sourceActivity;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "target_activity_id")
	public Activity getTargetActivity() {
		return targetActivity;
	}

	public void setTargetActivity(Activity targetActivity) {
		this.targetActivity = targetActivity;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "condition_id")
	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "workflow_process_id")
	public WorkflowProcess getWorkflowProcess() {
		return workflowProcess;
	}

	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}

	// ///////////////////
	// Model Methods
	// ///////////////////

	public void tranfer(ExecutionContext context) {
		context.getToken().setCurrentActivity(null);
		if (getCondition() != null) {
			Evaluator evaluator = new BasicEvaluator();
			Object result = evaluator.evaluate(getCondition(), context
					.getProcessInstance());
			if ((result instanceof Boolean)
					&& ((Boolean) result).booleanValue()) {
				targetActivity.enter(context);
			} else {
				throw new TweException(
						"cannot transfer from this transition, condition not satisfied");
			}
		} else {
			targetActivity.enter(context);
		}

	}
}
