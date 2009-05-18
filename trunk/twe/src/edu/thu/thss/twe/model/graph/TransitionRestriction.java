package edu.thu.thss.twe.model.graph;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transition_restrictions")
public class TransitionRestriction {
	/**
	 * @hibernate.id generator-class="native" column="id"
	 */
	long id;

	private Join join;
	private Split split;

	public TransitionRestriction() {
	}

	public TransitionRestriction(Join join, Split split) {
		this.join = join;
		this.split = split;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "join_id")
	public Join getJoin() {
		return join;
	}

	public void setJoin(Join join) {
		this.join = join;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "split_id")
	public Split getSplit() {
		return split;
	}

	public void setSplit(Split split) {
		this.split = split;
	}
}
