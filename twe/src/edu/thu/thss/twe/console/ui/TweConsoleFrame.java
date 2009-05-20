package edu.thu.thss.twe.console.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import edu.thu.thss.twe.TweContext;
import edu.thu.thss.twe.console.ui.dialogs.LoginDialog;
import edu.thu.thss.twe.console.ui.pages.ConsolePage;
import edu.thu.thss.twe.console.ui.pages.ProcessInstancePage;
import edu.thu.thss.twe.console.ui.pages.TaskPage;
import edu.thu.thss.twe.console.ui.pages.WorkflowProcessPage;

public class TweConsoleFrame extends JFrame {
	private TweContext tweContext;

	private static int HEIGHT = 600;
	private static int WIDTH = 700;

	private List<ConsolePage> pages = new LinkedList<ConsolePage>();;
	private JTabbedPane tabbedPane;

	public TweConsoleFrame(TweContext context) {
		super("Tiny Workflow Engine Console [unauthenticated]");
		tweContext = context;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((int) (dimension.getWidth() - WIDTH) / 2,
				(int) (dimension.getHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new TweWindowListener());
		initComponents();
	}

	private void initComponents() {
		this.tabbedPane = new JTabbedPane();
		this.add("Center", tabbedPane);
		// pages
		initConsolePages();
		for (ConsolePage page : pages) {
			tabbedPane.add(page.getName(), page);
		}
	}

	private void initConsolePages() {
		ConsolePage page = new WorkflowProcessPage("Workflow Processes", this);
		pages.add(page);
		page = new ProcessInstancePage("ProcessInstances", this);
		pages.add(page);
		page = new TaskPage("Tasks", this);
		pages.add(page);
	}

	public void setTweContext(TweContext context) {
		this.tweContext = context;
		for (ConsolePage page : pages) {
			page.setTweContext(context);
		}
	}

	public void start() {
		this.setVisible(true);
		if (login()) {
			this.setTitle("Tiny Workflow Engine Console [authenticated as "
					+ tweContext.getPerformerId() + "]");
			for (ConsolePage page : pages) {
				page.setTweContext(tweContext);
			}

		}
	}

	public void notifyUpdate() {
		for (ConsolePage page : pages) {
			page.updateContent();
		}
	}

	private boolean login() {
		LoginDialog dialog = new LoginDialog(this);
		while (true) {
			dialog.setVisible(true);
			if (dialog.cancelled()) {
				dialog.dispose();
				return false;
			}
			if (dialog.validateInput()) {
				if (autheticate(dialog.getUserName(), dialog.getPassword())) {

					dialog.dispose();
					tweContext.setPerformerId(dialog.getUserName());
					return true;
				}
			}
		}
	}

	private boolean autheticate(String id, String pwd) {
		if (id.equals(pwd))
			return true;
		else
			return false;
	}

	class TweWindowListener extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent e) {
			tweContext.close();
			super.windowClosing(e);
		}

	}
}
