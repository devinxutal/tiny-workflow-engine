package edu.thu.thss.twe.model.runtime;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.thu.thss.twe.model.graph.DataField;

/**
 * Instance of DataField
 * 
 * @author Devin
 * 
 */
@Entity
@Table(name = "variables")
public class Variable {
	long id;
	private DataField dataField;
	private String value;
	private ProcessInstance processInstance;

	public Variable() {
	}

	public Variable(DataField field) {
		this.dataField = field;
	}

	// ////////////////////
	// Getters and Setters
	// ////////////////////

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToOne
	@JoinColumn(name = "data_field_id")
	public DataField getDataField() {
		return dataField;
	}

	public void setDataField(DataField dataField) {
		this.dataField = dataField;
	}

	@Basic
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "process_instance_id")
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	// //////////////
	// Model Methods
	// /////////////
	@Transient
	public Object getTypedValue() {
		return DataField.getTyppedValue(getValue(), getDataField()
				.getDataType());
	}
}
