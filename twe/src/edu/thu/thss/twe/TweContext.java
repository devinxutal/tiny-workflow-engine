package edu.thu.thss.twe;

import java.util.LinkedList;
import java.util.List;

import edu.thu.thss.twe.db.DBHelperFactory;
import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.model.runtime.ProcessInstance;
import edu.thu.thss.twe.model.runtime.Token;
import edu.thu.thss.twe.util.HibernateUtil;

public class TweContext {
	private static TweContext defaultContext;

	private String performerId = null;
	private List<ProcessInstance> managedProcessInstances;

	public static TweContext getDefaultTweContext() {
		if (defaultContext == null) {
			defaultContext = new TweContext();
		}
		return defaultContext;
	}

	public TweContext() {

	}

	public void close() {
		autosave();
		HibernateUtil.currentSession().flush();
	}

	public List<Activity> getActivityList() {
		return null;
	}

	public List<Activity> getActivityList(String performerId) {
		return null;
	}

	public String getPerformerId() {
		return performerId;
	}

	public void setPerformerId(String id) {
		this.performerId = id;
	}

	public void deployWorkflowProcess(WorkflowProcess process) {
		DBHelperFactory.getWorkflowProcessDBHelper().deployWorkflowProcess(
				process);
	}

	/**
	 * Loads a workflow process specified by its id.
	 * 
	 * @param id
	 *            ID of the WorkflowProcess
	 * @param autosave
	 *            registers it for auto-save, in case it will be saved when the
	 *            method close() is called
	 * @return the loaded WorkflowProcess, or null if it dosen't exist.
	 */
	public WorkflowProcess loadWorkflowProcess(long id, boolean autosave) {
		//WorkflowProcess process = 
		return null;
	}

	/**
	 * Loads a ProcessInstance specified by its id.
	 * 
	 * @param id
	 *            ID of the ProcessInstance
	 * @param autosave
	 *            registers it for auto-save, in case it will be saved when the
	 *            method close() is called
	 * @return the loaded ProcessInstance, or null if it dosen't exist.
	 */
	public ProcessInstance loadProcessInstance(long id, boolean autosave) {
		return null;
	}

	/**
	 * Loads a Token specified by its id.
	 * 
	 * @param id
	 *            ID of the Token
	 * @param autosave
	 *            registers it for auto-save, in case it will be saved when the
	 *            method close() is called
	 * @return the loaded Token, or null if it dosen't exist.
	 */
	public Token loadToken(long id, boolean autosave) {
		return null;
	}

	private void autosave() {
		if (managedProcessInstances != null) {
			for (ProcessInstance i : managedProcessInstances) {
				DBHelperFactory.getWorkflowProcessDBHelper()
						.saveProcessInstance(i);
			}
		}
	}

	private void addManagedProcessInstance(ProcessInstance instance) {
		if (managedProcessInstances == null) {
			managedProcessInstances = new LinkedList<ProcessInstance>();
		}
		managedProcessInstances.add(instance);
	}
}
