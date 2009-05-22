package edu.thu.thss.twe.console.ui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.thu.thss.twe.TweContext;
import edu.thu.thss.twe.console.model.ProcessInstanceTableModel;
import edu.thu.thss.twe.console.ui.ButtonPanel;
import edu.thu.thss.twe.console.ui.TweConsoleFrame;
import edu.thu.thss.twe.console.ui.UIUtil;
import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.runtime.ProcessInstance;

public class ProcessInstancePage extends ConsolePage implements
		ListSelectionListener, ActionListener {

	private ProcessInstanceTableModel instanceTableModel;
	private JTable instanceTable;

	private JButton deleteButton;
	private JButton startButton;
	private JButton detailButton;

	private ButtonPanel buttonPane;

	public ProcessInstancePage(String name, TweConsoleFrame frame,
			TweContext context) {
		super(name, frame, context);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.red);
		initComponents();
	}

	public ProcessInstancePage(String name, TweConsoleFrame frame) {
		super(name, frame, null);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.red);
		initComponents();
	}

	private void initComponents() {
		// Table
		instanceTableModel = new ProcessInstanceTableModel();
		instanceTable = UIUtil.createTable();
		instanceTable.setModel(instanceTableModel);
		instanceTable.getSelectionModel().addListSelectionListener(this);
		this.add("Center", new JScrollPane(instanceTable));
		// Button
		buttonPane = new ButtonPanel();
		this.add("South", buttonPane);
		deleteButton = new JButton("Delete");
		startButton = new JButton("Start");
		detailButton = new JButton("Detail");
		deleteButton.addActionListener(this);
		startButton.addActionListener(this);
		detailButton.addActionListener(this);
		buttonPane.addButton(startButton);
		buttonPane.addButton(deleteButton);
		buttonPane.addButton(detailButton);
		updateContent();
	}

	public void updateContent() {
		updateUI();
		if (tweContext == null)
			return;
		instanceTableModel.setItems(tweContext.getModelSession()
				.findAllProcessInstances());
		instanceTableModel.fireTableDataChanged();
		updateUI();

	}

	public void updateUI() {
		if (instanceTable == null)
			return;
		if (instanceTable.getSelectedRow() < 0) {
			startButton.setEnabled(false);
			deleteButton.setEnabled(false);
			detailButton.setEnabled(false);
		} else {
			ProcessInstance instance = instanceTableModel.getItems().get(
					instanceTable.getSelectedRow());
			if (instance.getState() == ProcessInstance.InstanceState.Created) {
				startButton.setEnabled(true);
			} else {
				startButton.setEnabled(false);
			}
			deleteButton.setEnabled(true);
			detailButton.setEnabled(true);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		updateUI();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == deleteButton) {
			deleteProcess();
		} else if (e.getSource() == detailButton) {
			showDetail();
		} else if (e.getSource() == startButton) {
			startProcess();
		}
	}

	private void showDetail() {

	}

	private void deleteProcess() {
		if (instanceTable.getSelectedRow() < 0) {
			showErrorMessage("Cannot delete process instance, no process instance is selected.");
			return;
		}
		ProcessInstance process = instanceTableModel.getItems().get(
				instanceTable.getSelectedRow());
		int code = showConfirm("This operation will delete the process instance and all the associated tasks, sure?");
		if (code == JOptionPane.OK_OPTION) {
			try {
				tweContext.getModelSession().deleteProcessInstance(process);
				showMessage("Delete succeed");
			} catch (TweException e) {
				showErrorMessage("Delete failed, error occured while writing the database.");
			}
		}
		consoleFrame.notifyUpdate();
	}

	private void startProcess() {
		if (instanceTable.getSelectedRow() < 0) {
			showErrorMessage("Cannot start process instance, no process instance is selected.");
			return;
		}
		ProcessInstance process = instanceTableModel.getItems().get(
				instanceTable.getSelectedRow());

		try {
			process.start();
			tweContext.save(process);
			showMessage("Process is started.");
		} catch (TweException e) {
			showErrorMessage("Start failed, error occured while writing the database.");
		}

		consoleFrame.notifyUpdate();
	}

}
