package edu.thu.thss.twe.model.graph;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Devin
 * 
 */

@Entity
@Table(name = "conditions")
public class Condition {
	long id;
	private int conditionType;
	private String expression;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Basic
	public int getConditionType() {
		return conditionType;
	}

	public void setConditionType(int conditionType) {
		this.conditionType = conditionType;
	}

	@Basic
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
