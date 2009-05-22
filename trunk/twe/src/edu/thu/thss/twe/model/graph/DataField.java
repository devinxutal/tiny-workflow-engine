package edu.thu.thss.twe.model.graph;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.thu.thss.twe.exception.TweException;

@Entity
@Table(name = "data_fields")
public class DataField extends WorkflowElement {

	public enum DataType {
		BOOLEAN, DATE, FLOAT, INTEGER, PERFORMER, REFERENCE, STRING
	}

	private WorkflowProcess workflowProcess;
	private DataType dataType;
	private String initialValue;

	@Basic
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	@Basic
	public String getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "workflow_process_id")
	public WorkflowProcess getWorkflowProcess() {
		return workflowProcess;
	}

	public void setWorkflowProcess(WorkflowProcess workflowProcess) {
		this.workflowProcess = workflowProcess;
	}

	// /////////////
	// Model Methods
	// /////////////
	public static Object getTyppedValue(String value, DataType type) {
		try {
			switch (type) {
			case STRING:
				return value;
			case INTEGER:
				return Integer.parseInt(value);
			case FLOAT:
				return Double.parseDouble(value);
			case BOOLEAN:
				return Boolean.parseBoolean(value);
			default:
				throw new TweException(
						"cannot get value of the value string: the type "
								+ type.name() + " is not supported right now.");

			}
		} catch (NumberFormatException e) {
			throw new TweException("cannot get value of the value string", e);
		}
	}
}
