package edu.thu.thss.twe.eval.expression;

import edu.thu.thss.twe.model.runtime.Variable;

public class VariableExpression implements Expression {
	private Variable variable;

	public VariableExpression(Variable variable) {
		super();
		this.variable = variable;
	}

	public Object evaluate() {
		return variable.getTypedValue();
	}

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	public String toString() {
		return variable.getDataField().getName();
	}
}
