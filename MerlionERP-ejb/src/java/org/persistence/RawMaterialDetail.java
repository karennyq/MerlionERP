package org.persistence;

import java.io.Serializable;

import javax.persistence.*;
import org.persistence.RawMaterial;
import org.persistence.OrderPlaced;
import java.util.Collection;
import org.persistence.Supplier;

@Entity
@Table(name = "RAWMATERIALDETAIL")
public class RawMaterialDetail implements Serializable {
	 private static final long serialVersionUID = 1L;

	@Id
	private Long raw_mat_detail_id;
	@Basic
	private Integer lead_time;
	@Basic
	private Integer lot_size;
	@Basic
	private Integer shelf_life;
	@Basic
	private Integer storage_unit;
	@ManyToOne
	private RawMaterial rawMaterial;

	@OneToMany
	private Collection<OrderPlaced> orderPlaced;

	@ManyToOne
	private Supplier supplier;

	public void setRaw_mat_detail_id(Long param) {
		this.raw_mat_detail_id = param;
	}

	public Long getRaw_mat_detail_id() {
		return raw_mat_detail_id;
	}

	public void setLead_time(Integer param) {
		this.lead_time = param;
	}

	public Integer getLead_time() {
		return lead_time;
	}

	public void setLot_size(Integer param) {
		this.lot_size = param;
	}

	public Integer getLot_size() {
		return lot_size;
	}

	public void setShelf_life(Integer param) {
		this.shelf_life = param;
	}

	public Integer getShelf_life() {
		return shelf_life;
	}

	public void setStorage_unit(Integer param) {
		this.storage_unit = param;
	}

	public Integer getStorage_unit() {
		return storage_unit;
	}

	public RawMaterial getRawMaterial() {
	    return rawMaterial;
	}

	public void setRawMaterial(RawMaterial param) {
	    this.rawMaterial = param;
	}

	public Collection<OrderPlaced> getOrderPlaced() {
	    return orderPlaced;
	}

	public void setOrderPlaced(Collection<OrderPlaced> param) {
	    this.orderPlaced = param;
	}

	public Supplier getSupplier() {
	    return supplier;
	}

	public void setSupplier(Supplier param) {
	    this.supplier = param;
	}

}