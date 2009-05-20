package edu.thu.thss.twe.session;

import edu.thu.thss.twe.util.HibernateUtil;

public class SessionFactory {
	private static ModelSession workflowProcessDBHelper;

	private static ModelSession getModelSession() {
		if (workflowProcessDBHelper == null) {
			workflowProcessDBHelper = new ModelSession(HibernateUtil
					.currentSession());
		}
		return workflowProcessDBHelper;
	}
}
