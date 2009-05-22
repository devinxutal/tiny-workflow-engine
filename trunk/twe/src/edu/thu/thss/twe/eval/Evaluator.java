package edu.thu.thss.twe.eval;

import edu.thu.thss.twe.model.graph.Condition;
import edu.thu.thss.twe.model.runtime.ProcessInstance;

public interface Evaluator {
	Object evaluate(Condition condition, ProcessInstance instance);
}
