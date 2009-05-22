package edu.thu.thss.twe.console.ui.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ImagePanel extends JPanel {
	private JLabel imageLabel;
	private Image image;
	private JScrollPane scrollPane;

	public ImagePanel() {
		this.setLayout(new BorderLayout());
		imageLabel = new JLabel();
		imageLabel.setHorizontalAlignment(JLabel.CENTER);
		scrollPane = new JScrollPane(imageLabel);
		this.add("Center", scrollPane);
		//this.setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		if (image != null) {
			this.imageLabel.setIcon(new ImageIcon(image));
		}
	}

	@Override
	public void setEnabled(boolean enable) {

		super.setEnabled(enable);
	}

	private void rerange() {
	}

}
