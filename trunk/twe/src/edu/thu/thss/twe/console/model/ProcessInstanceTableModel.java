package edu.thu.thss.twe.console.model;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.thu.thss.twe.model.runtime.ProcessInstance;

public class ProcessInstanceTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ProcessInstance> items;

	public ProcessInstanceTableModel() {
		this.items = new LinkedList<ProcessInstance>();
	}

	public ProcessInstanceTableModel(List<ProcessInstance> items) {
		this.items = items;
	}

	public List<ProcessInstance> getItems() {
		return items;
	}

	public void setItems(List<ProcessInstance> items) {
		this.items = items;
	}

	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		return items.size();
	}

	public Object getValueAt(int row, int col) {
		if (row >= getRowCount() || col >= getColumnCount() || row < 0
				|| col < 0) {
			return null;
		}
		ProcessInstance process = items.get(row);
		switch (col) {
		case 0:
			return process.getInstanceName();
		case 1:
			return process.getWorkflowProcess().getName();
		case 2:
			return process.getStartTime();
		default:
			return null;
		}
	}

	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Instance Name";
		case 1:
			return "Process Name";
		case 2:
			return "Start Time";
		default:
			return "";
		}
	}

	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

}
