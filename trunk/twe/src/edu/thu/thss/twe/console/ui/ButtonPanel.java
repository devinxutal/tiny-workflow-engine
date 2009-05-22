package edu.thu.thss.twe.console.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ButtonPanel extends JPanel {
	private List<JButton> buttons = new LinkedList<JButton>();
	private Box box;

	public ButtonPanel() {
		this.setLayout(new BorderLayout());
		box = Box.createHorizontalBox();
		this.add("Center", box);
		this.setMinimumSize(new Dimension(30, 30));
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	public void addButton(JButton button) {
		if (buttons.add(button)) {
			rerange();
		}
	}

	public void removeButton(JButton button) {
		if (buttons.remove(button)) {
			rerange();
		}
	}

	@Override
	public void setEnabled(boolean enable) {
		for (JButton b : buttons) {
			b.setEnabled(enable);
		}
		super.setEnabled(enable);
	}

	private void rerange() {
		box.removeAll();
		for (JButton button : buttons) {
			box.add(Box.createHorizontalStrut(10));
			box.add(button);
		}
		this.repaint();
	}

}
