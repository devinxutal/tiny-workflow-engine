package edu.thu.thss.twe.eval.expression;

import edu.thu.thss.twe.eval.util.OperatorResolver;
import edu.thu.thss.twe.exception.EvaluationException;

public class TwoOperandExpression implements Expression {
	private Expression leftOperand;
	private Expression rightOperand;
	private OperatorType operator;

	public Expression getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(Expression leftOperand) {
		this.leftOperand = leftOperand;
	}

	public Expression getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(Expression rightOperand) {
		this.rightOperand = rightOperand;
	}

	public OperatorType getOperator() {
		return operator;
	}

	public void setOperator(OperatorType operator) {
		this.operator = operator;
	}

	public TwoOperandExpression(Expression leftOperand,
			Expression rightOperand, OperatorType operator) {
		this.leftOperand = leftOperand;
		this.rightOperand = rightOperand;
		this.operator = operator;
	}

	public Object evaluate() {
		if (operator == OperatorType.ASSIGN) {
			return evaluateAssign();
		} else if (OperatorResolver.isBasicOperation(operator)) {
			return evaluateBasicOperation();
		} else if (OperatorResolver.isComparisonOperation(operator)) {
			return evaluateComparisonOperation();
		} else {
			return null;
		}
	}

	private Object evaluateAssign() {
		if (operator != OperatorType.ASSIGN) {
			return null;
		}
		if (leftOperand instanceof VariableExpression) {
			VariableExpression exp = (VariableExpression) leftOperand;
			Object value = rightOperand.evaluate();
			exp.getVariable().setValue(value.toString());
			return value;
		} else {
			throw new EvaluationException(
					"cannot assign: the left operand is not a variable");
		}
	}

	private Object evaluateBasicOperation() {
		if (!OperatorResolver.isBasicOperation(operator)) {
			return null;
		}
		Object left = leftOperand.evaluate();
		Object right = rightOperand.evaluate();
		if (left instanceof String || right instanceof String) {
			if (operator == OperatorType.ADD) {
				return left.toString() + right.toString();
			} else {
				throw new EvaluationException("cannot perform "
						+ operator.name() + " on a String");
			}
		} else if (left instanceof Number && right instanceof Number) {
			double l = ((Number) left).doubleValue();
			double r = ((Number) right).doubleValue();
			double result = 0;
			if (operator == OperatorType.ADD) {
				result = l + r;
			} else if (operator == OperatorType.MINUS) {
				result = l - r;
			} else if (operator == OperatorType.TIMES) {
				result = l * r;
			} else if (operator == OperatorType.DEVIDE) {
				result = l / r;
			}
			return new Double(result);
		} else {
			throw new EvaluationException("cannot perform" + operator.name()
					+ ", type mismatch");
		}

	}

	private Object evaluateComparisonOperation() {
		if (!OperatorResolver.isComparisonOperation(operator)) {
			return null;
		}
		Object left = leftOperand.evaluate();
		Object right = rightOperand.evaluate();
		if (left instanceof String && right instanceof String) {
			String l = (String) left;
			String r = (String) right;
			boolean result = false;
			if (operator == OperatorType.EQUALS) {
				result = l.equals(r);
			} else if (operator == OperatorType.BIGGER_THAN) {
				result = (l.compareTo(r) > 0);
			} else if (operator == OperatorType.SMALLER_THAN) {
				result = (l.compareTo(r) < 0);
			} else if (operator == OperatorType.NO_SMALLER_THAN) {
				result = (l.compareTo(r) >= 0);
			} else if (operator == OperatorType.NO_BIGGER_THAN) {
				result = (l.compareTo(r) <= 0);
			}
			return new Boolean(result);
		} else if (left instanceof Number && right instanceof Number) {
			double l = ((Number) left).doubleValue();
			double r = ((Number) right).doubleValue();
			boolean result = false;
			if (operator == OperatorType.EQUALS) {
				result = (l == r);
			} else if (operator == OperatorType.BIGGER_THAN) {
				result = (l > r);
			} else if (operator == OperatorType.SMALLER_THAN) {
				result = (l < r);
			} else if (operator == OperatorType.NO_SMALLER_THAN) {
				result = (l >= r);
			} else if (operator == OperatorType.NO_BIGGER_THAN) {
				result = (l <= r);
			}
			return new Boolean(result);
		} else if (left instanceof Boolean && right instanceof Boolean) {
			if (operator == OperatorType.EQUALS) {
				return left.equals(right);
			} else {
				throw new EvaluationException("cannot perform "
						+ operator.name() + " between to boolean values");
			}
		} else {
			throw new EvaluationException("cannot perform" + operator.name()
					+ ", type mismatch");
		}
	}

	public String toString() {
		return "( " +leftOperand.toString()+" "+ OperatorResolver.symbolOf(operator) + " "
				+ rightOperand.toString() + ")";
	}
}
