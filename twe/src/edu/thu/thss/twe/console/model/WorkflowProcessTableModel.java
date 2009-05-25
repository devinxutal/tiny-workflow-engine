package edu.thu.thss.twe.console.model;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.thu.thss.twe.model.graph.WorkflowProcess;

public class WorkflowProcessTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<WorkflowProcess> items;

	public WorkflowProcessTableModel(List<WorkflowProcess> items) {
		this.items = items;
	}

	public WorkflowProcessTableModel() {
		this.items = new LinkedList<WorkflowProcess>();
	}

	public List<WorkflowProcess> getItems() {
		return items;
	}

	public void setItems(List<WorkflowProcess> items) {
		this.items = items;
	}

	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return items.size();
	}

	public Object getValueAt(int row, int col) {
		if (row >= getRowCount() || col >= getColumnCount() || row < 0
				|| col < 0) {
			return null;
		}
		WorkflowProcess process = items.get(row);
		switch (col) {
		case 0:
			return process.getElementId();
		case 1:
			return process.getName();
		case 2:
			return process.getDescription();
		case 3:
			return process.getVersion();
		default:
			return null;
		}
	}

	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Element ID";
		case 1:
			return "Name";
		case 2:
			return "Description";
		case 3:
			return "Version";
		default:
			return "";
		}
	}

	public boolean isCellEditable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return super.isCellEditable(arg0, arg1);
	}

}
