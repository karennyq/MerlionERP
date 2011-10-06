package org.persistence;

import java.io.Serializable;
import static javax.persistence.InheritanceType.JOINED;
import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "PRODUCTIONOVERVIEW")
@Inheritance(strategy = JOINED)
public class ProductionOverview implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
        @GeneratedValue
	private Long id;

	@Basic
	private Integer working_days;

	@Basic
	private Double capacity;

	@Basic
	private Double utilization;

	@Basic
	private Double ot_capacity;

	@Basic
	private Double ot_utilization;

	@OneToMany
	//@JoinColumn(name = "PRODUCTIONOVERVIEW_id", referencedColumnName = "id")
	private Collection<PlannedDemand> plannedDemand;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setWorking_days(Integer param) {
		this.working_days = param;
	}

	public Integer getWorking_days() {
		return working_days;
	}

	public void setCapacity(Double param) {
		this.capacity = param;
	}

	public Double getCapacity() {
		return capacity;
	}

	public void setUtilization(Double param) {
		this.utilization = param;
	}

	public Double getUtilization() {
		return utilization;
	}

	public void setOt_capacity(Double param) {
		this.ot_capacity = param;
	}

	public Double getOt_capacity() {
		return ot_capacity;
	}

	public void setOt_utilization(Double param) {
		this.ot_utilization = param;
	}

	public Double getOt_utilization() {
		return ot_utilization;
	}

	public Collection<PlannedDemand> getPlannedDemand() {
	    return plannedDemand;
	}

	public void setPlannedDemand(Collection<PlannedDemand> param) {
	    this.plannedDemand = param;
	}

}