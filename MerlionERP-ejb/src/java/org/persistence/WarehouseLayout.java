package org.persistence;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "WAREHOUSELAYOUT")
public class WarehouseLayout implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long warehouse_layout_id;
	
	@Basic
	private String location;
	
	@Basic
	private Integer total_unit;
	
	public void setWarehouse_layout_id(Long param) {
		this.warehouse_layout_id = param;
	}

	public Long getWarehouse_layout_id() {
		return warehouse_layout_id;
	}

	public void setLocation(String param) {
		this.location = param;
	}

	public String getLocation() {
		return location;
	}

	public void setTotal_unit(Integer param) {
		this.total_unit = param;
	}

	public Integer getTotal_unit() {
		return total_unit;
	}

}