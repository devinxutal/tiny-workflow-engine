package edu.thu.thss.twe.console.ui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
import edu.thu.thss.twe.console.ui.dialogs.SubmissionDialog;
import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.graph.Submission;
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
		updateControls();
		if (tweContext == null ||tweContext.getPerformerId() == null) {
			taskTableModel.setItems(new LinkedList<Task>());
		} else if (tweContext.isSuperMode()) {
			taskTableModel.setItems(tweContext.getTaskList(false));
		} else {
			taskTableModel.setItems(tweContext.getTaskList(tweContext
					.getPerformerId(), false));
		}

		taskTableModel.fireTableDataChanged();
		updateControls();

	}

	public void updateControls() {
		if (tweContext == null) {
			this.buttonPane.setEnabled(false);
			return;
		} else {
			this.buttonPane.setEnabled(true);
		}
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
		updateControls();
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
			// task.finish(); // TODO delete this;
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
			if (task.isSubmissionNeeded()) {
				SubmissionDialog dialog = new SubmissionDialog(
						this.consoleFrame);
				Map<Submission, String> submissionMap = new HashMap<Submission, String>();
				for (Submission sub : task.getActivity().getSubmissions()) {
					dialog.setSubmission(sub);
					dialog.setVisible(true);
					if (dialog.cancelled()) {
						showMessage("The finish operation is canceled.");
						return;
					}
					submissionMap.put(sub, dialog.getSubmissionValue());
				}
				task.submitVariables(submissionMap);
			}
			task.finish();
			tweContext.save(task.getProcessInstance());

			showMessage("Task is finished.");
		} catch (TweException e) {
			e.printStackTrace();
			showErrorMessage("Operation failed, error occured while writing the database.");
		}
		consoleFrame.notifyUpdate();
	}
}
