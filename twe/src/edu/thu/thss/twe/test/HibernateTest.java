package edu.thu.thss.twe.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Random;

import edu.thu.thss.twe.config.Configuration;
import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.parser.ParserFactory;

public class HibernateTest {
	public static void main(String[] args) {
		System.out.println("hello");
		// Session s = HibernateUtil.currentSession();
		// testStoreWorkflowProcess();
		// testFindAllWorkflowProcesses();
		// testFindLatestWorkflowProcesses();
		testDeleteThenSave();

	}

	public static void testStoreWorkflowProcess() {
		for (int i = 0; i < 10; i++) {
			WorkflowProcess process = new WorkflowProcess();
			process.setName(randomName());
			Configuration.getConfiguration().getTweContext()
					.deployWorkflowProcess(process);
		}
		Configuration.getConfiguration().getTweContext().close();
	}

	private static String randomName() {
		String[] names = new String[] { "workflow_1", "workflow_2",
				"workflow_3", "workflow_4", "workflow_5", "workflow_6",
				"workflow_7", "workflow_8", };
		Random rand = new Random();
		return names[rand.nextInt(names.length)];
	}

	private static void testFindAllWorkflowProcesses() {
		List<WorkflowProcess> processes = Configuration.getConfiguration()
				.getTweContext().getModelSession().findAllWorkflowProcesses();

		for (WorkflowProcess p : processes) {
			System.out.println(p.getName() + "," + p.getVersion());
		}
	}

	private static void testFindLatestWorkflowProcesses() {
		List<WorkflowProcess> processes = Configuration.getConfiguration()
				.getTweContext().getModelSession()
				.findLatestWorkflowProcesses();

		for (WorkflowProcess p : processes) {
			System.out.println(p.getName() + "," + p.getVersion());
		}
	}

	private static void testParseAndDeployWorkflowProcess() {
		try {
			WorkflowProcess process = ParserFactory.getXmlParser().parse(
					new FileInputStream(new File("sample xpdl/test.xml")));
			System.out.println(process.getName());
			Configuration.getConfiguration().getTweContext()
					.deployWorkflowProcess(process);
			process.setDescrioption("ahahhaha");
			Configuration.getConfiguration().getTweContext().getModelSession()
					.saveWorkflowProcess(process);
			;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testDeleteThenSave() {
		for (int i = 0; i < 10; i++) {
			WorkflowProcess process = new WorkflowProcess();
			process.setName(randomName());
			Configuration.getConfiguration().getTweContext()
					.deployWorkflowProcess(process);

			Configuration.getConfiguration().getTweContext().getModelSession()
					.deleteWorkflowProcess(process);
			Configuration.getConfiguration().getTweContext().getModelSession()
					.saveWorkflowProcess(process);
		}
		Configuration.getConfiguration().getTweContext().close();
	}
}
