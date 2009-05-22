package edu.thu.thss.twe.util;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.Condition;
import edu.thu.thss.twe.model.graph.DataField;
import edu.thu.thss.twe.model.graph.Join;
import edu.thu.thss.twe.model.graph.Participant;
import edu.thu.thss.twe.model.graph.Split;
import edu.thu.thss.twe.model.graph.Submission;
import edu.thu.thss.twe.model.graph.Transition;
import edu.thu.thss.twe.model.graph.TransitionRestriction;
import edu.thu.thss.twe.model.graph.WorkflowElement;
import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.model.runtime.ProcessInstance;
import edu.thu.thss.twe.model.runtime.Task;
import edu.thu.thss.twe.model.runtime.Token;
import edu.thu.thss.twe.model.runtime.Variable;

public class HibernateUtility {
	public static SessionFactory sessionFactory;
	public static final AnnotationConfiguration configuration;
	static {
		try {
			configuration = new AnnotationConfiguration();

		} catch (HibernateException e) {// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}

	public static void configure(Map<String, String> properties) {
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			configuration.setProperty(entry.getKey(), entry.getValue());
		}

		configuration.addPackage("edu.thu.thss.twe.model.graph")
				.addAnnotatedClass(Activity.class).addAnnotatedClass(
						Condition.class).addAnnotatedClass(DataField.class)
				.addAnnotatedClass(Join.class).addAnnotatedClass(
						Participant.class)

				.addAnnotatedClass(Submission.class).addAnnotatedClass(
						Split.class).addAnnotatedClass(Transition.class)
				.addAnnotatedClass(TransitionRestriction.class)
				.addAnnotatedClass(WorkflowElement.class).addAnnotatedClass(
						WorkflowProcess.class);
		configuration.addPackage("edu.thu.thss.twe.model.runtime")
				.addAnnotatedClass(ProcessInstance.class).addAnnotatedClass(
						Task.class).addAnnotatedClass(Token.class)
				.addAnnotatedClass(Variable.class);
	}

	public static SessionFactory getSessionFactory() {
		synchronized (configuration) {
			System.err.println("new session factory");
			if (sessionFactory == null) {
				sessionFactory = configuration.buildSessionFactory();
			}
		}
		return sessionFactory;
	}

	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();

	public static Session currentSession() throws HibernateException {
		Session s = session.get();

		if (s == null) {
			s = getSessionFactory().openSession();
			session.set(s);
		}

		return s;
	}

	public static void closeSession() throws HibernateException {
		Session s = session.get();
		if (s != null) {
			s.close();
		}
		session.set(null);
	}
}
