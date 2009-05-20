package edu.thu.thss.twe.config;

import org.hibernate.Session;

import edu.thu.thss.twe.TweContext;
import edu.thu.thss.twe.util.HibernateUtil;

public class Configuration {
	private static Configuration config;

	Session session;
	TweContext tweContext;

	public static Configuration getConfiguration() {
		if (config == null) {
			config = new Configuration();
		}
		return config;
	}

	private Configuration() {
		session = HibernateUtil.currentSession();
		tweContext = new TweContext(session);
	}

	public TweContext getTweContext() {
		return tweContext;
	}
}
