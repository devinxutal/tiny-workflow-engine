package edu.thu.thss.twe.console.model;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.thu.thss.twe.model.runtime.Task;

public class TaskTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Task> items;

	public TaskTableModel() {
		this.items = new LinkedList<Task>();
	}

	public TaskTableModel(List<Task> items) {
		this.items = items;
	}

	public List<Task> getItems() {
		return items;
	}

	public void setItems(List<Task> items) {
		this.items = items;
	}

	public int getColumnCount() {
		return 7;
	}

	public int getRowCount() {
		return items.size();
	}

	public Object getValueAt(int row, int col) {
		if (row >= getRowCount() || col >= getColumnCount() || row < 0
				|| col < 0) {
			return null;
		}
		Task task = items.get(row);
		switch (col) {
		case 0:
			return task.getActivity().getName();
		case 1:
			return task.getProcessInstance().getInstanceName();
		case 2:
			return task.getPerformer().getName();
		case 3:
			return task.getState().name();
		case 4:
			return task.getCreateTime() == null ? "" : task.getCreateTime()
					.toGMTString();
		case 5:
			return task.getStartTime() == null ? "" : task.getStartTime()
					.toGMTString();
		case 6:
			return task.getFinishTime() == null ? "" : task.getFinishTime()
					.toGMTString();
		default:
			return null;
		}
	}

	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Task Name";
		case 1:
			return "Beloing Instance";
		case 2:
			return "Performer";
		case 3:
			return "Status";
		case 4:
			return "Create Time";
		case 5:
			return "Start Time";
		case 6:
			return "Finish Time";
		default:
			return "";
		}
	}

	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

}
