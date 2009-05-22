package edu.thu.thss.twe.eval.expression;

import edu.thu.thss.twe.eval.util.OperatorResolver;

public class OneOperandExpression implements Expression {
	private Expression operand;
	private OperatorType operator;

	public OneOperandExpression(Expression operand, OperatorType operator) {
		this.operand = operand;
		this.operator = operator;
	}

	public Object evaluate() {
		// TODO
		return null;
	}

	public Expression getOperand() {
		return operand;
	}

	public void setOperand(Expression operand) {
		this.operand = operand;
	}

	public OperatorType getOperator() {
		return operator;
	}

	public void setOperator(OperatorType operator) {
		this.operator = operator;
	}

	public String toString() {
		return "( " + OperatorResolver.symbolOf(operator) + " "
				+ operand.toString() + ")";
	}
}
