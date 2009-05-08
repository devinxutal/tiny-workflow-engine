package edu.thu.thss.twe.graph;

public abstract class WorkflowElemnt {
	long id;
	protected String name;
	protected String descrioption;
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescrioption() {
		return descrioption;
	}
	public void setDescrioption(String descrioption) {
		this.descrioption = descrioption;
	}
	
}
