package org.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.persistence.OrderPlaced;

@Entity
@Table(name = "SUPPLIER")
public class Supplier implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long supplier_id;
	
	@OneToMany(mappedBy = "supplier")
	private Collection<RawMaterial> rawMaterials;
	
	@OneToMany(mappedBy = "supplier")
	private Collection<OrderPlaced> ordersPlaced;

	public void setSupplier_id(Long param) {
		this.supplier_id = param;
	}

	public Long getSupplier_id() {
		return supplier_id;
	}
	
	public Collection<RawMaterial> getRawMaterials() {
		return rawMaterials;
	}

	public void setRawMaterials(Collection<RawMaterial> param) {
		this.rawMaterials = param;
	}

	public Collection<OrderPlaced> getOrdersPlaced() {
		return ordersPlaced;
	}

	public void setOrdersPlaced(Collection<OrderPlaced> param) {
		this.ordersPlaced = param;
	}

}