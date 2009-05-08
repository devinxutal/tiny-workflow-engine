package edu.thu.thss.twe.runtime;

import edu.thu.thss.twe.graph.Activity;
import edu.thu.thss.twe.graph.Transition;

public class Token {
	long id;
	
	private Activity currentActivity;
	private ProcessInstance processInstance;
	
	
	public void signal(){
		
	}
	public void signal(String transitionName){
		
	}
	public void signal(Transition transition){
		
	}
}
