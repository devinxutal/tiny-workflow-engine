package edu.thu.thss.twe.model.graph;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Devin
 * 
 */

@Entity
@Table(name = "conditions")
public class Condition {

	long id;

	@Id
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
