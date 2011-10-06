package org.persistence;

import java.io.Serializable;

import javax.persistence.*;

import org.persistence.Supplier;

@Entity
@Table(name = "ORDERPLACED")
public class OrderPlaced implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long order_placed_id;

	@ManyToOne
	private Supplier supplier;
	
	public void setOrder_placed_id(Long param) {
		this.order_placed_id = param;
	}

	public Long getOrder_placed_id() {
		return order_placed_id;
	}
	
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier param) {
		this.supplier = param;
	}

}