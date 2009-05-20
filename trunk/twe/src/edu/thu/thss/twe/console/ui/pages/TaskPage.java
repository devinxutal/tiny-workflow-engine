package edu.thu.thss.twe.console.ui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.thu.thss.twe.TweContext;
import edu.thu.thss.twe.console.model.TaskTableModel;
import edu.thu.thss.twe.console.ui.ButtonPanel;
import edu.thu.thss.twe.console.ui.TweConsoleFrame;
import edu.thu.thss.twe.console.ui.UIUtil;
import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.runtime.Task;

public class TaskPage extends ConsolePage implements ListSelectionListener,
		ActionListener {

	private TaskTableModel taskTableModel;
	private JTable taskTable;

	private JButton acceptButton;
	private JButton finishButton;
	private JButton detailButton;

	private ButtonPanel buttonPane;

	public TaskPage(String name, TweConsoleFrame frame, TweContext context) {
		super(name, frame, context);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.red);
		initComponents();
	}

	public TaskPage(String name, TweConsoleFrame frame) {
		super(name, frame, null);
		this.setLayout(new BorderLayout());
		initComponents();
	}

	private void initComponents() {
		// Table
		taskTableModel = new TaskTableModel();
		taskTable = UIUtil.createTable();
		taskTable.setModel(taskTableModel);
		taskTable.getSelectionModel().addListSelectionListener(this);
		this.add("Center", new JScrollPane(taskTable));
		// Button
		buttonPane = new ButtonPanel();
		this.add("South", buttonPane);
		acceptButton = new JButton("Accept");
		finishButton = new JButton("Finish");
		detailButton = new JButton("Detail");
		acceptButton.addActionListener(this);
		finishButton.addActionListener(this);
		detailButton.addActionListener(this);
		buttonPane.addButton(acceptButton);
		buttonPane.addButton(finishButton);
		buttonPane.addButton(detailButton);
		updateContent();
	}

	public void updateContent() {
		updateUI();
		if (tweContext == null)
			return;
		taskTableModel.setItems(tweContext.getTaskList(false));
		taskTableModel.fireTableDataChanged();
		updateUI();

	}

	public void updateUI() {
		if (taskTable == null)
			return;
		if (taskTable.getSelectedRow() < 0) {
			acceptButton.setEnabled(false);
			finishButton.setEnabled(false);
			detailButton.setEnabled(false);
		} else {
			Task task = taskTableModel.getItems().get(
					taskTable.getSelectedRow());
			if (task.getState() == Task.TaskState.Created) {
				acceptButton.setEnabled(true);
				finishButton.setEnabled(false);
			} else if (task.getState() == Task.TaskState.Started) {
				acceptButton.setEnabled(false);
				finishButton.setEnabled(true);
			} else if (task.getState() == Task.TaskState.Finished) {
				acceptButton.setEnabled(false);
				finishButton.setEnabled(false);
			}
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		updateUI();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == acceptButton) {
			acceptTask();
		} else if (e.getSource() == finishButton) {
			finishTask();
		} else if (e.getSource() == detailButton) {
			taskDetail();
		}

	}

	private void taskDetail() {

	}

	private void acceptTask() {
		if (taskTable.getSelectedRow() < 0) {
			showErrorMessage("Operation failed, no task is selected.");
			return;
		}
		Task task = taskTableModel.getItems().get(taskTable.getSelectedRow());
		try {
			task.start();
			tweContext.save(task.getProcessInstance());
			showMessage("Task is started.");
		} catch (TweException e) {
			showErrorMessage("Operation failed, error occured while writing the database.");
		}
		consoleFrame.notifyUpdate();
	}

	private void finishTask() {
		if (taskTable.getSelectedRow() < 0) {
			showErrorMessage("Operation failed, no task is selected.");
			return;
		}
		Task task = taskTableModel.getItems().get(taskTable.getSelectedRow());
		try {
			task.finish();
			tweContext.save(task.getProcessInstance());
			
			showMessage("Task is finished.");
		} catch (TweException e) {
			showErrorMessage("Operation failed, error occured while writing the database.");
		}
		consoleFrame.notifyUpdate();
	}
}