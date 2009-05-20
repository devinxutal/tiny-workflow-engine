package edu.thu.thss.twe.console.ui.pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.thu.thss.twe.TweContext;
import edu.thu.thss.twe.console.model.WorkflowProcessTableModel;
import edu.thu.thss.twe.console.ui.ButtonPanel;
import edu.thu.thss.twe.console.ui.TweConsoleFrame;
import edu.thu.thss.twe.console.ui.UIUtil;
import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.model.runtime.ProcessInstance;
import edu.thu.thss.twe.parser.Parser;
import edu.thu.thss.twe.parser.ParserFactory;

public class WorkflowProcessPage extends ConsolePage implements
		ListSelectionListener, ActionListener {

	private WorkflowProcessTableModel processTableModel;
	private JTable processTable;

	private JButton deleteButton;
	private JButton instanceButton;
	private JButton deployButton;

	private ButtonPanel buttonPane;

	public WorkflowProcessPage(String name, TweConsoleFrame frame,
			TweContext context) {
		super(name, frame, context);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.red);
		initComponents();
	}

	public WorkflowProcessPage(String name, TweConsoleFrame frame) {
		super(name, frame, null);
		this.setLayout(new BorderLayout());
		this.setBackground(Color.red);
		initComponents();
	}

	private void initComponents() {
		// Table
		processTableModel = new WorkflowProcessTableModel();
		processTable = UIUtil.createTable();
		processTable.setModel(processTableModel);
		processTable.getSelectionModel().addListSelectionListener(this);
		this.add("Center", new JScrollPane(processTable));
		// Button
		buttonPane = new ButtonPanel();
		this.add("South", buttonPane);
		deleteButton = new JButton("Delete");
		instanceButton = new JButton("Create Instance");
		deployButton = new JButton("Deploy");
		deleteButton.addActionListener(this);
		instanceButton.addActionListener(this);
		deployButton.addActionListener(this);
		buttonPane.addButton(deleteButton);
		buttonPane.addButton(instanceButton);
		buttonPane.addButton(deployButton);
		updateContent();
	}

	public void updateContent() {
		updateUI();
		if (tweContext == null)
			return;
		processTableModel.setItems(tweContext.getModelSession()
				.findAllWorkflowProcesses());
		processTableModel.fireTableDataChanged();
		updateUI();

	}

	public void updateUI() {
		if (processTable == null)
			return;
		if (processTable.getSelectedRow() < 0) {
			deleteButton.setEnabled(false);
			instanceButton.setEnabled(false);
		} else {

			deleteButton.setEnabled(true);
			instanceButton.setEnabled(true);
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		updateUI();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == deleteButton) {
			deleteProcess();
		} else if (e.getSource() == instanceButton) {
			createInstance();
		} else if (e.getSource() == deployButton) {
			deployProcess();
		}

	}

	private void deployProcess() {
		JFileChooser dlg = new JFileChooser();
		dlg.setDialogTitle("Open Process Definition File");
		int result = dlg.showOpenDialog(this); // 打开"打开文件"对话框
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = dlg.getSelectedFile();
			Parser parser = ParserFactory.getXmlParser();
			WorkflowProcess process = null;
			try {
				process = parser.parse(new FileInputStream(file));
			} catch (IOException e) {
				showErrorMessage("Deploy failed, cannot open file.");
			} catch (TweException e) {
				showErrorMessage("Deploy failed, the file dosen't contain a valid process definition.");
			}
			if (process == null)
				return;
			try {
				this.tweContext.deployWorkflowProcess(process);
			} catch (TweException e) {
				showErrorMessage("Deploy failed, error occured while writing the database.");

				return;
			}
			showMessage("Deploy succeeded.");
			this.updateContent();
		}
	}

	private void deleteProcess() {
		if (processTable.getSelectedRow() < 0) {
			showErrorMessage("Cannot delete process, no process is selected.");
			return;
		}
		WorkflowProcess process = processTableModel.getItems().get(
				processTable.getSelectedRow());
		int code = showConfirm("This operation will delete the process and all the instances of this process, sure to delete?");
		if (code == JOptionPane.OK_OPTION) {
			try {
				tweContext.getModelSession().deleteWorkflowProcess(process);
				showMessage("Delete succeed");
			} catch (TweException e) {
				showErrorMessage("Delete failed, error occured while writing the database.");
			}
		}
		consoleFrame.notifyUpdate();
	}

	private void createInstance() {
		if (processTable.getSelectedRow() < 0) {
			showMessage("No process is selected.");
			return;
		}
		WorkflowProcess process = processTableModel.getItems().get(
				processTable.getSelectedRow());
		int code = showConfirm("Sure to create a new process instance?");
		if (code == JOptionPane.OK_OPTION) {
			try {
				ProcessInstance instance = new ProcessInstance(process, process
						.getName()
						+ "_instance");
				tweContext.getModelSession().saveProcessInstance(instance);
				showMessage("Create instance succeed");
				
			} catch (TweException e) {
				showErrorMessage("Create failed, error occured while writing the database.");
			}
		}
		consoleFrame.notifyUpdate();
	}
}
