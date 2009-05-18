package edu.thu.thss.twe.util;

import java.awt.Dimension;
import java.awt.Image;

import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.model.runtime.ProcessInstance;

/**
 * This class used to visualize the bussiness model and create a image to give
 * user a illustrated concept of the model
 * 
 * @author Lee
 * 
 */
public class ModelVisualizer {
	public static int DEFAULT_HEIGHT = 400;
	public static int DEFAULT_WIDTH = 600;

	public Image visualizeProcessInstance(ProcessInstance instance) {
		return visualizeProcessInstance(instance, new Dimension(DEFAULT_WIDTH,
				DEFAULT_HEIGHT));
	}

	public Image visualizeProcessInstance(ProcessInstance instance,
			Dimension dimension) {
		// TODO
		return null;
	}

	public Image visualizeWorkflowProcess(WorkflowProcess process) {
		return visualizeWorkflowProcess(process, new Dimension(DEFAULT_WIDTH,
				DEFAULT_HEIGHT));
	}

	public Image visualizeWorkflowProcess(WorkflowProcess process,
			Dimension dimension) {
		// TODO
		return null;
	}
}
