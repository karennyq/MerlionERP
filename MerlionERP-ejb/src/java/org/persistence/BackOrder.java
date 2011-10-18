package org.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "BACKORDER")
public class BackOrder implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long backorder_id;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date created_date;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date scheduled_date;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date completed_date;
	
	@Basic
	private Integer boxes_to_produce;
	
	@ManyToOne
	private SalesOrder salesOrder;

	public void setBackorder_id(Long param) {
		this.backorder_id = param;
	}

	public Long getBackorder_id() {
		return backorder_id;
	}

	public void setCreated_date(Date param) {
		this.created_date = param;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setScheduled_date(Date param) {
		this.scheduled_date = param;
	}

	public Date getScheduled_date() {
		return scheduled_date;
	}

	public void setCompleted_date(Date param) {
		this.completed_date = param;
	}

	public Date getCompleted_date() {
		return completed_date;
	}

	public void setBoxes_to_produce(Integer param) {
		this.boxes_to_produce = param;
	}

	public Integer getBoxes_to_produce() {
		return boxes_to_produce;
	}
	
	public SalesOrder getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(SalesOrder param) {
		this.salesOrder = param;
	}

}