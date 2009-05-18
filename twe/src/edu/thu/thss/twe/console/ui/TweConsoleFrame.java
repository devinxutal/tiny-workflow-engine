package edu.thu.thss.twe.console.ui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

import edu.thu.thss.twe.TweContext;

public class TweConsoleFrame extends JFrame {
	private TweContext tweContext;

	private static int HEIGHT = 600;
	private static int WIDTH = 700;

	public TweConsoleFrame(TweContext context) {
		super("Tiny Workflow Engine Console");
		tweContext = context;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((int) (dimension.getWidth() - WIDTH) / 2,
				(int) (dimension.getHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
	}

	public void setTweContext(TweContext context) {
		this.tweContext = context;
	}
}
