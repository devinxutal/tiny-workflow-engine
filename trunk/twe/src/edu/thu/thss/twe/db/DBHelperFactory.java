package edu.thu.thss.twe.db;

import edu.thu.thss.twe.util.HibernateUtil;

public class DBHelperFactory {
	private static ModelDBHelper workflowProcessDBHelper;

	public static ModelDBHelper getWorkflowProcessDBHelper() {
		if (workflowProcessDBHelper == null) {
			workflowProcessDBHelper = new ModelDBHelper(HibernateUtil
					.currentSession());
		}
		return workflowProcessDBHelper;
	}
}
