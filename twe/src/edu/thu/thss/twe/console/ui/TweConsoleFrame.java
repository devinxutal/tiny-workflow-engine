package edu.thu.thss.twe.console.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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

	private static int HEIGHT = 800;
	private static int WIDTH = 1000;

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
		JMenuBar menubar = new JMenuBar();
		menubar.add(generateMenu());
		this.setJMenuBar(menubar);
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
		login();
	}

	public void notifyUpdate() {
		updateControls();
		for (ConsolePage page : pages) {
			page.updateContent();
		}
	}

	private JMenu generateMenu() {
		JMenu menu = new JMenu("Menu");
		JMenuItem login = new JMenuItem("Login");
		JMenuItem logout = new JMenuItem("Logout");
		JMenuItem exit = new JMenuItem("Exit");
		login.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		logout.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		exit.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		menu.add(login);
		menu.add(logout);
		menu.addSeparator();
		menu.add(exit);
		return menu;
	}

	private void updateControls() {
		if (tweContext != null) {
			if (tweContext.getPerformerId() == null) {
				this.setTitle("Tiny Workflow Engine Console [unauthenticated]");
			} else {
				if (tweContext.isSuperMode()) {
					this
							.setTitle("Tiny Workflow Engine Console [authenticated as "
									+ tweContext.getPerformerId()
									+ "][Super Mode]");
				} else {
					this
							.setTitle("Tiny Workflow Engine Console [authenticated as "
									+ tweContext.getPerformerId() + "]");
				}

			}
		} else {
			this.setTitle("Tiny Workflow Engine Console [unauthenticated]");
		}
	}

	private boolean checkLogin() {
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

	private void login() {
		if (checkLogin()) {
			for (ConsolePage page : pages) {
				page.setTweContext(tweContext);
			}

		}
		notifyUpdate();
	}

	private void logout() {
		if (tweContext != null) {
			tweContext.setPerformerId(null);
			tweContext.setSuperMode(false);
		}
		notifyUpdate();
	}

	private void exit() {
		tweContext.close();
		System.exit(0);
	}

	private boolean autheticate(String id, String pwd) {
		if (tweContext != null) {
			return tweContext.authenticate(id, pwd);
		}
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
