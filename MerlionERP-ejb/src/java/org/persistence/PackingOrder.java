package org.persistence;

import java.io.Serializable;
import javax.persistence.*;
import org.persistence.DeliveryOrder;
import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "PACKINGORDER")
public class PackingOrder implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long pack_order_id;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date completed_date;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date created_date;
	
	@OneToOne(mappedBy = "packingOrder")
	private DeliveryOrder deliveryOrder;
	
	public void setPack_order_id(Long param) {
		this.pack_order_id = param;
	}

	public Long getPack_order_id() {
		return pack_order_id;
	}

	public void setCompleted_date(Date param) {
		this.completed_date = param;
	}

	public Date getCompleted_date() {
		return completed_date;
	}

	public void setCreated_date(Date param) {
		this.created_date = param;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public DeliveryOrder getDeliveryOrder() {
		return deliveryOrder;
	}

	public void setDeliveryOrder(DeliveryOrder param) {
		this.deliveryOrder = param;
	}

}