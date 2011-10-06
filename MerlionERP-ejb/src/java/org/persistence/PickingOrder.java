package org.persistence;

import java.io.Serializable;
import javax.persistence.*;
import org.persistence.DeliveryOrder;
import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "PICKINGORDER")
public class PickingOrder implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long pick_order_id;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date created_date;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date completed_date;
	
	@OneToOne(mappedBy = "pickingOrder")
	private DeliveryOrder deliveryOrder;

	public void setPick_order_id(Long param) {
		this.pick_order_id = param;
	}

	public Long getPick_order_id() {
		return pick_order_id;
	}

	public void setCreated_date(Date param) {
		this.created_date = param;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCompleted_date(Date param) {
		this.completed_date = param;
	}

	public Date getCompleted_date() {
		return completed_date;
	}

	public DeliveryOrder getDeliveryOrder() {
		return deliveryOrder;
	}

	public void setDeliveryOrder(DeliveryOrder param) {
		this.deliveryOrder = param;
	}

}