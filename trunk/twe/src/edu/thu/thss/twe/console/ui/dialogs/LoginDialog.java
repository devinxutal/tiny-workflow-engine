package edu.thu.thss.twe.console.ui.dialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginDialog extends JDialog implements KeyListener, WindowListener {

	private static int HEIGHT = 200;
	private static int WIDTH = 300;

	private JTextField nameField;
	private JTextField pwdField;

	public LoginDialog(Frame frame) throws HeadlessException {
		super(frame, "Login", true);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((int) (dimension.getWidth() - WIDTH) / 2,
				(int) (dimension.getHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
		initComponents();
		this.addWindowListener(this);
	}

	private void initComponents() {
		nameField = new JTextField(20);
		pwdField = new JTextField(20);
		nameField.addKeyListener(this);
		pwdField.addKeyListener(this);
		Container contentPane = this.getContentPane();
		Box box = Box.createVerticalBox();
		JPanel pane1 = new JPanel();
		pane1.add(new JLabel("User Name:"));
		pane1.add(nameField);
		box.add(Box.createVerticalGlue());
		box.add(pane1);
		JPanel pane2 = new JPanel();
		pane2.add(new JLabel("Password :"));
		pane2.add(pwdField);
		box.add(pane2);
		box.add(Box.createVerticalGlue());
		contentPane.add(box);
	}

	public void keyPressed(KeyEvent arg0) {

	}

	public void keyReleased(KeyEvent arg0) {

	}

	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			if (e.getSource().equals(nameField)) {
				pwdField.requestFocus();
				pwdField.selectAll();
			} else if (e.getSource().equals(pwdField)) {
				if (validateInput()) {
					this.setVisible(false);
				}
			}
		}
	}

	public String getUserName() {
		return nameField.getText();
	}

	public String getPassword() {
		return pwdField.getText();
	}

	public boolean validateInput() {
		if (nameField.getText().trim().length() == 0) {
			return false;
		}
		if (pwdField.getText().trim().length() == 0) {
			return false;
		}
		return true;
	}

	private boolean cancel = false;

	public boolean cancelled(){
		return cancel;
	}

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void windowClosing(WindowEvent arg0) {
		System.out.println("faint");
		cancel = true;
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
