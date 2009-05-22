package edu.thu.thss.twe.console.ui.pages;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.thu.thss.twe.TweContext;
import edu.thu.thss.twe.console.ui.TweConsoleFrame;

public abstract class ConsolePage extends JPanel {
	protected TweConsoleFrame consoleFrame;
	protected TweContext tweContext;
	protected String name;

	public ConsolePage(String name, TweConsoleFrame frame, TweContext context) {
		this.name = name;
		this.consoleFrame = frame;
		this.tweContext = context;
	}

	public ConsolePage(String name, TweConsoleFrame frame) {
		this.consoleFrame = frame;
		this.name = name;
	}

	public abstract void updateContent();

	public abstract void updateControls();

	public TweContext getTweContext() {
		return tweContext;
	}

	public void setTweContext(TweContext tweContext) {
		this.tweContext = tweContext;
		this.updateContent();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void showErrorMessage(String msg) {
		JOptionPane.showMessageDialog(consoleFrame, msg, "Error",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(consoleFrame, msg, "Message",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public int showConfirm(String msg) {
		return JOptionPane.showConfirmDialog(consoleFrame, msg,
				"Confirm Operation", JOptionPane.INFORMATION_MESSAGE);
	}
}
