package edu.thu.thss.twe.eval.expression;

public class SingleValueExpression implements Expression {
	private Object value;

	public SingleValueExpression(Object value) {
		super();
		this.value = value;
	}

	public Object evaluate() {
		return value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String toString() {
		return value.toString();
	}
}
