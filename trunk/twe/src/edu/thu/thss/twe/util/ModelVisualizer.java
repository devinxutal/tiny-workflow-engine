package edu.thu.thss.twe.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.DataField;
import edu.thu.thss.twe.model.graph.Participant;
import edu.thu.thss.twe.model.graph.Transition;
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
	public static int DEFAULT_HEIGHT = 500;
	public static int DEFAULT_WIDTH = 750;

	public Image visualizeProcessInstance(ProcessInstance instance) {
		return visualizeProcessInstance(instance, new Dimension(DEFAULT_WIDTH,
				DEFAULT_HEIGHT));
	}

	public Image visualizeProcessInstance(ProcessInstance instance,
			Dimension dimension) {
		return null;
	}

	public Image visualizeWorkflowProcess(WorkflowProcess process) {
		return visualizeWorkflowProcess(process, new Dimension(DEFAULT_WIDTH,
				DEFAULT_HEIGHT));
	}

	public Image visualizeWorkflowProcess(WorkflowProcess process,
			Dimension dimension) {
		int height=dimension.height;
		int width=dimension.width;
		
		List<Activity> activities=process.getActivities();
		List<Participant> participants=process.getParticipants();
		List<Transition> transitions=process.getTransitions();
		List<DataField> dataFileds=process.getDataFileds();
		
		BufferedImage image=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphic = image.createGraphics(); 
		graphic.setColor(Color.BLACK);

		List<Transition> queue=new LinkedList<Transition>();
		Map<Activity, Point> activityPostionMap=new HashMap<Activity, Point>();
		Map<Transition, Boolean> transitionDrawMap=new HashMap<Transition, Boolean>();
		
		DrawUtil drawUtil=new DrawUtil();
		drawUtil.setGraphic(graphic);
		drawUtil.setHeight(height);
		drawUtil.setWidth(width);
		drawUtil.setActivityPostionMap(activityPostionMap);
		drawUtil.DrawInit(participants);
		
		Activity currentActivity=process.getStartActivity();
	    drawUtil.DrawStartNode(currentActivity);
		drawUtil.DrawActivity(currentActivity);
	    List<Transition> leavingTransitions=currentActivity.getLeavingTransitions();
	    if(leavingTransitions!=null){
	        for(int i=0;i<leavingTransitions.size();i++){
	        	queue.add(leavingTransitions.get(i));
	        }
	    }
		while(queue.isEmpty()==false){
			Transition transition=queue.remove(0);
			drawUtil.DrawTransition(transition);
			transitionDrawMap.put(transition, new Boolean(true));
			currentActivity=transition.getTargetActivity();
			drawUtil.DrawActivity(currentActivity);
			leavingTransitions=currentActivity.getLeavingTransitions();
		    if(leavingTransitions!=null){
		        for(int i=0;i<leavingTransitions.size();i++){
		        	transition=leavingTransitions.get(i);
			        Boolean isDraw=transitionDrawMap.get(transition);
			        if(isDraw==null)
			        	queue.add(leavingTransitions.get(i));
		        }
		    }
		}
		currentActivity=process.getEndActivity();
		drawUtil.DrawEndNode(currentActivity);
		return image;
	}
}
