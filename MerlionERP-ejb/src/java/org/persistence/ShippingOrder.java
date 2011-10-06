package org.persistence;

import java.io.Serializable;
import javax.persistence.*;
import org.persistence.DeliveryOrder;
import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "SHIPPINGORDER")
public class ShippingOrder implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long shipping_order_id;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date shipped_date;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date created_date;
	
	@OneToOne(mappedBy = "shippingOrder")
	private DeliveryOrder deliveryOrder;

	public void setShipping_order_id(Long param) {
		this.shipping_order_id = param;
	}

	public Long getShipping_order_id() {
		return shipping_order_id;
	}

	public void setShipped_date(Date param) {
		this.shipped_date = param;
	}

	public Date getShipped_date() {
		return shipped_date;
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