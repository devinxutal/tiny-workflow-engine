package edu.thu.thss.twe.console.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.thu.thss.twe.console.ui.ButtonPanel;
import edu.thu.thss.twe.model.graph.Submission;
import edu.thu.thss.twe.util.DataTypeUtil;

public class SubmissionDialog extends JDialog implements WindowListener,
		ActionListener {

	private static int HEIGHT = 250;
	private static int WIDTH = 300;

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			finishSubmission();
		} else {
			cancelSubmission();
		}
	}

	private JTextField nameField;
	private JTextField typeField;
	private JTextArea valueArea;
	private ButtonPanel buttonPane;

	private JButton okButton;
	private JButton cancelButton;

	private Submission submission = null;
	private String submissionValue = "";

	public SubmissionDialog(Frame frame) throws HeadlessException {
		super(frame, "Variable Submission", true);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((int) (dimension.getWidth() - WIDTH) / 2,
				(int) (dimension.getHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
		this.setResizable(false);
		initComponents();
		this.addWindowListener(this);
	}

	private void initComponents() {
		nameField = new JTextField(20);
		typeField = new JTextField(20);
		valueArea = new JTextArea();
		nameField.setEditable(false);
		typeField.setEditable(false);
		Container contentPane = this.getContentPane();
		Box box = Box.createVerticalBox();
		JPanel pane1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		pane1.add(new JLabel("Variable Name: "));
		pane1.add(nameField);
		box.add(Box.createVerticalGlue());
		box.add(pane1);
		JPanel pane2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pane2.add(new JLabel("Variable Type: "));
		pane2.add(typeField);
		box.add(pane2);
		JPanel pane3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pane3.add(new JLabel("Variable Value:"));
		JPanel areaPane = new JPanel(new BorderLayout());
		areaPane.add("West", pane3);
		areaPane.add("Center", new JScrollPane(valueArea));

		box.add(areaPane);
		// box.add(Box.createVerticalGlue());
		contentPane.add("Center", box);
		// buttonPane
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		buttonPane = new ButtonPanel();
		buttonPane.addButton(okButton);
		buttonPane.addButton(cancelButton);
		contentPane.add("South", buttonPane);
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
		if (submission != null) {
			this.nameField.setText(submission.getDataField().getName());
			this.typeField.setText(submission.getDataField().getDataType()
					.name());
		} else {
			this.nameField.setText("");
			this.typeField.setText("");
		}
	}

	private void finishSubmission() {
		if (validateInput()) {
			this.cancel = false;
			this.submissionValue = valueArea.getText();
			this.setVisible(false);
		} else {
			JOptionPane.showMessageDialog(this,
					"Please enter a valid value for the variable");
		}
	}

	private void cancelSubmission() {
		this.cancel = true;
		this.submissionValue = null;
		this.setVisible(false);
	}

	public String getSubmissionValue() {
		return submissionValue;
	}

	public boolean validateInput() {
		String value = valueArea.getText().trim();
		System.out.println("Value: " + value);
		if (submission == null) {
			System.out.println("submission null");
			return false;
		}
		return DataTypeUtil.isValidValue(submission.getDataField()
				.getDataType(), value);
	}

	private boolean cancel = false;

	public boolean cancelled() {
		return cancel;
	}

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowClosing(WindowEvent arg0) {
		this.cancelSubmission();
	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
}
