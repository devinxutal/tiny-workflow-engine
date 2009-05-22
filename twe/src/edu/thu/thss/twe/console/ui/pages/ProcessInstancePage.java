package edu.thu.thss.twe.console.ui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.thu.thss.twe.TweContext;
import edu.thu.thss.twe.console.model.ProcessInstanceTableModel;
import edu.thu.thss.twe.console.ui.TweConsoleFrame;
import edu.thu.thss.twe.console.ui.UIUtil;
import edu.thu.thss.twe.console.ui.dialogs.ImageDialog;
import edu.thu.thss.twe.console.ui.panels.ButtonPanel;
import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.runtime.ProcessInstance;
import edu.thu.thss.twe.util.ModelVisualizer;

public class ProcessInstancePage extends ConsolePage implements
		ListSelectionListener, ActionListener {

	private ProcessInstanceTableModel instanceTableModel;
	private JTable instanceTable;

	private JButton deleteButton;
	private JButton startButton;
	private JButton visualizeButton;

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
		visualizeButton = new JButton("Visualize");
		deleteButton.addActionListener(this);
		startButton.addActionListener(this);
		visualizeButton.addActionListener(this);
		buttonPane.addButton(startButton);
		buttonPane.addButton(deleteButton);
		buttonPane.addButton(visualizeButton);
		updateContent();
	}

	public void updateContent() {
		updateControls();
		if (tweContext == null || tweContext.getPerformerId() == null) {
			instanceTableModel.setItems(new LinkedList<ProcessInstance>());
		} else if (tweContext.isSuperMode()) {
			instanceTableModel.setItems(tweContext.getModelSession()
					.findAllProcessInstances());
		} else {
			instanceTableModel.setItems(new LinkedList<ProcessInstance>());
		}

		instanceTableModel.fireTableDataChanged();
		updateControls();
	}

	public void updateControls() {
		if (tweContext == null) {
			this.buttonPane.setEnabled(false);
			return;
		}
		if (!tweContext.isSuperMode()) {
			this.buttonPane.setEnabled(false);
			return;
		}
		this.buttonPane.setEnabled(true);

		if (instanceTable.getSelectedRow() < 0) {
			startButton.setEnabled(false);
			deleteButton.setEnabled(false);
			visualizeButton.setEnabled(false);
		} else {
			ProcessInstance instance = instanceTableModel.getItems().get(
					instanceTable.getSelectedRow());
			if (instance.getState() == ProcessInstance.InstanceState.Created) {
				startButton.setEnabled(true);
			} else {
				startButton.setEnabled(false);
			}
			deleteButton.setEnabled(true);
			visualizeButton.setEnabled(true);
		}

	}

	public void valueChanged(ListSelectionEvent e) {
		updateControls();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == deleteButton) {
			deleteInstance();
		} else if (e.getSource() == visualizeButton) {
			visualizeInstance();
		} else if (e.getSource() == startButton) {
			startInstance();
		}
	}

	private void visualizeInstance() {
		if (instanceTable.getSelectedRow() < 0) {
			showErrorMessage("Cannot visualize process instance, no process instance is selected.");
			return;
		}
		ProcessInstance process = instanceTableModel.getItems().get(
				instanceTable.getSelectedRow());
		ModelVisualizer visualizer = new ModelVisualizer();
		ImageDialog dialog = new ImageDialog(consoleFrame);
		Image img = visualizer.visualizeProcessInstance(process);
		img = Toolkit.getDefaultToolkit().getImage("resources/flower.jpg");
		dialog.setImage(img);
		dialog.setVisible(true);
		dialog.dispose();
	}

	private void deleteInstance() {
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

	private void startInstance() {
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
