package org.persistence;

import java.io.Serializable;

import javax.persistence.*;

import org.persistence.Supplier;
import org.persistence.OrderPlaced;
import java.util.Collection;
import org.persistence.RawMatBatch;

@Entity
@Table(name = "RAWMATERIAL")
public class RawMaterial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long raw_material_id;
	
	@Basic
	private Integer lot_size;
	
	@Basic
	private Integer shelf_life;
	
	@Basic
	private Integer lead_time;
	
	@Basic
	private Integer storage_unit;
	
	@OneToMany(mappedBy = "rawMaterial")
	private Collection<RawMatBatch> rawMatBatches;
	
	@ManyToOne
	private Supplier supplier;
	
	@OneToMany
	//@JoinColumn(name = "RAWMATERIAL_raw_material_id", referencedColumnName = "raw_material_id")
	private Collection<OrderPlaced> ordersPlaced;

	public void setRaw_material_id(Long param) {
		this.raw_material_id = param;
	}

	public Long getRaw_material_id() {
		return raw_material_id;
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

	public void setLead_time(Integer param) {
		this.lead_time = param;
	}

	public Integer getLead_time() {
		return lead_time;
	}

	public void setStorage_unit(Integer param) {
		this.storage_unit = param;
	}

	public Integer getStorage_unit() {
		return storage_unit;
	}

	public Collection<RawMatBatch> getRawMatBatches() {
	    return rawMatBatches;
	}

	public void setRawMatBatches(Collection<RawMatBatch> param) {
	    this.rawMatBatches = param;
	}
	
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier param) {
		this.supplier = param;
	}

	public Collection<OrderPlaced> getOrdersPlaced() {
		return ordersPlaced;
	}

	public void setOrdersPlaced(Collection<OrderPlaced> param) {
		this.ordersPlaced = param;
	}

}