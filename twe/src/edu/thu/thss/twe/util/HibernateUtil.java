package edu.thu.thss.twe.util;

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

public class HibernateUtil {
	public static final SessionFactory sessionFactory;
	static {
		try {
			AnnotationConfiguration config = new AnnotationConfiguration();

			config.addPackage("edu.thu.thss.twe.graph").addAnnotatedClass(
					Activity.class).addAnnotatedClass(Condition.class)
					.addAnnotatedClass(DataField.class).addAnnotatedClass(
							Join.class).addAnnotatedClass(Participant.class)

					.addAnnotatedClass(Submission.class).addAnnotatedClass(
							Split.class).addAnnotatedClass(Transition.class)
					.addAnnotatedClass(TransitionRestriction.class)
					.addAnnotatedClass(WorkflowElement.class)
					.addAnnotatedClass(WorkflowProcess.class);
			config.addPackage("edu.thu.thss.twe.runtime").addAnnotatedClass(
					ProcessInstance.class).addAnnotatedClass(Task.class)
					.addAnnotatedClass(Token.class).addAnnotatedClass(
							Variable.class);
			sessionFactory = config.configure().buildSessionFactory();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}

	
	public static final ThreadLocal<Session> session = new ThreadLocal<Session>();

	
	public static Session currentSession() throws HibernateException {
		Session s = session.get();

		if (s == null) {
			s = sessionFactory.openSession();
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
