package edu.thu.thss.twe.db;

import edu.thu.thss.twe.util.HibernateUtil;

public class DBHelperFactory {
	private static WorkflowProcessDBHelper workflowProcessDBHelper;

	public static WorkflowProcessDBHelper getWorkflowProcessDBHelper() {
		if (workflowProcessDBHelper == null) {
			workflowProcessDBHelper = new WorkflowProcessDBHelper(HibernateUtil
					.currentSession());
		}
		return workflowProcessDBHelper;
	}
}
