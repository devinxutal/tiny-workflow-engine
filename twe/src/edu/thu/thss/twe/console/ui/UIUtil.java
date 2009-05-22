package edu.thu.thss.twe.console.ui;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class UIUtil {
	public static JTable createTable() {
		JTable table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(24);
		table.getTableHeader().setReorderingAllowed(false);
		return table;
	}
}
