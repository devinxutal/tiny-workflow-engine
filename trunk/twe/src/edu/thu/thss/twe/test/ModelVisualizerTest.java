package edu.thu.thss.twe.test;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.parser.Parser;
import edu.thu.thss.twe.parser.ParserFactory;
import edu.thu.thss.twe.util.ModelVisualizer;

public class ModelVisualizerTest {
	public static void main(String[] args) {
		new ModelVisualizerTest().test();

	}

	public void test() {
		JFrame frame = new JFrame("Test");
		frame.setBounds(200, 200, 800, 600);
		JPanel panel = new ImagePanel();
		frame.add(panel);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
	}

	public class ImagePanel extends JPanel {
		Image image;

		public ImagePanel() {
			Parser parser = ParserFactory.getXmlParser();
			try {
				WorkflowProcess process = parser.parse(new FileInputStream(
						new File("sample xpdl/test.xml")));
				image = new ModelVisualizer().visualizeWorkflowProcess(process);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void setImage(Image image) {
			this.image = image;
			this.repaint();
		}

		@Override
		protected void paintComponent(Graphics e) {
			super.paintComponent(e);
			if (image != null) {
				e.drawImage(image, 0, 0, image.getWidth(this), image
						.getHeight(this), this);
			}

		}
	}
}
