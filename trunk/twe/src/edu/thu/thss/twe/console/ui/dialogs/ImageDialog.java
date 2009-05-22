package edu.thu.thss.twe.console.ui.dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JDialog;

import edu.thu.thss.twe.console.ui.panels.ImagePanel;

public class ImageDialog extends JDialog {

	private static int HEIGHT = 500;
	private static int WIDTH = 600;

	private ImagePanel imagePanel;

	public ImageDialog(Frame frame) throws HeadlessException {
		super(frame, "View Visualized Process", true);
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((int) (dimension.getWidth() - WIDTH) / 2,
				(int) (dimension.getHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
		initComponents();
	}

	private void initComponents() {
		imagePanel = new ImagePanel();
		this.add("Center", imagePanel);
	}

	public void setImage(Image img) {
		this.imagePanel.setImage(img);
		this.imagePanel.repaint();
	}
}
