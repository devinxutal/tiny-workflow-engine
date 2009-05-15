package edu.thu.thss.twe.test;

import java.util.List;
import java.util.Random;

import org.hibernate.Session;

import edu.thu.thss.twe.TweContext;
import edu.thu.thss.twe.db.DBHelperFactory;
import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.util.HibernateUtil;

public class HibernateTest {
	public static void main(String[] args) {
		System.out.println("hello");
		Session s = HibernateUtil.currentSession();
		// testStoreWorkflowProcess();
		// testFindAllWorkflowProcesses();
		testFindLatestWorkflowProcesses();
	}

	public static void testStoreWorkflowProcess() {
		for (int i = 0; i < 10; i++) {
			WorkflowProcess process = new WorkflowProcess();
			process.setName(randomName());
			TweContext.getDefaultTweContext().deployWorkflowProcess(process);
		}
		TweContext.getDefaultTweContext().close();
	}

	private static String randomName() {
		String[] names = new String[] { "workflow_1", "workflow_2",
				"workflow_3", "workflow_4", "workflow_5", "workflow_6",
				"workflow_7", "workflow_8", };
		Random rand = new Random();
		return names[rand.nextInt(names.length)];
	}

	private static void testFindAllWorkflowProcesses() {
		List<WorkflowProcess> processes = DBHelperFactory
				.getWorkflowProcessDBHelper().findAllWorkflowProcesses();

		for (WorkflowProcess p : processes) {
			System.out.println(p.getName() + "," + p.getVersion());
		}
	}

	private static void testFindLatestWorkflowProcesses() {
		List<WorkflowProcess> processes = DBHelperFactory
				.getWorkflowProcessDBHelper().findLatestWorkflowProcesses();

		for (WorkflowProcess p : processes) {
			System.out.println(p.getName() + "," + p.getVersion());
		}
	}
}
