package edu.thu.thss.twe.eval.evaluator;

import edu.thu.thss.twe.eval.Evaluator;
import edu.thu.thss.twe.eval.expression.Expression;
import edu.thu.thss.twe.eval.util.ExpressionParser;
import edu.thu.thss.twe.eval.util.VariableResolver;
import edu.thu.thss.twe.model.graph.Condition;
import edu.thu.thss.twe.model.runtime.ProcessInstance;

public class BasicEvaluator implements Evaluator {
	public Object evaluate(Condition condition, ProcessInstance instance) {
		VariableResolver resolver = new VariableResolver(instance);
		Expression expression = ExpressionParser.parse(condition
				.getExpression(), resolver);
		System.out.println(expression);
		return expression.evaluate();
	}
}
