package org.persistence;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SHIPPINGADDRESS")
public class ShippingAddress implements Serializable {
    private static final long serialVersionUID = 1L;

	@Id
	private Long shipping_add_id;
	@Basic
	private String shipping_address;

	public void setShipping_add_id(Long param) {
		this.shipping_add_id = param;
	}

	public Long getShipping_add_id() {
		return shipping_add_id;
	}

	public void setShipping_address(String param) {
		this.shipping_address = param;
	}

	public String getShipping_address() {
		return shipping_address;
	}

}