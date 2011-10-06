package org.persistence;

import java.io.Serializable;

import javax.persistence.*;

import static javax.persistence.InheritanceType.JOINED;
import java.util.Collection;

@Entity
@Table(name = "PLANNEDDEMAND")
@Inheritance(strategy = JOINED)
public class PlannedDemand implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long planned_dmd_id;
	
	@Basic
	private Integer boxes_to_produce;
	
	@Basic
	private Double hours_needed;
	
	@Basic
	private Integer ot_boxes_to_produce;
	
	@Basic
	private Double ot_hours_needed;

	@ManyToOne
	private Product product;

	@OneToMany
	//@JoinColumn(name = "PLANNEDDEMAND_planned_dmd_id", referencedColumnName = "planned_dmd_id")
	private Collection<PlannedDemandAmendment> plannedDemandAmendment;

	public void setPlanned_dmd_id(Long param) {
		this.planned_dmd_id = param;
	}

	public Long getPlanned_dmd_id() {
		return planned_dmd_id;
	}

	public void setBoxes_to_produce(Integer param) {
		this.boxes_to_produce = param;
	}

	public Integer getBoxes_to_produce() {
		return boxes_to_produce;
	}

	public void setHours_needed(Double param) {
		this.hours_needed = param;
	}

	public Double getHours_needed() {
		return hours_needed;
	}

	public void setOt_boxes_to_produce(Integer param) {
		this.ot_boxes_to_produce = param;
	}

	public Integer getOt_boxes_to_produce() {
		return ot_boxes_to_produce;
	}

	public void setOt_hours_needed(Double param) {
		this.ot_hours_needed = param;
	}

	public Double getOt_hours_needed() {
		return ot_hours_needed;
	}

	public Product getProduct() {
	    return product;
	}

	public void setProduct(Product param) {
	    this.product = param;
	}

	public Collection<PlannedDemandAmendment> getPlannedDemandAmendment() {
	    return plannedDemandAmendment;
	}

	public void setPlannedDemandAmendment(Collection<PlannedDemandAmendment> param) {
	    this.plannedDemandAmendment = param;
	}

}