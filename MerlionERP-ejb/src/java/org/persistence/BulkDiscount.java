package org.persistence;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "BULKDISCOUNT")
public class BulkDiscount implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long bulk_discount_id;
	
	@Basic
	private Integer boxes_required;
	
	@Basic
	private Double discount_given;

	public void setBulk_discount_id(Long param) {
		this.bulk_discount_id = param;
	}

	public Long getBulk_discount_id() {
		return bulk_discount_id;
	}

	public void setBoxes_required(Integer param) {
		this.boxes_required = param;
	}

	public Integer getBoxes_required() {
		return boxes_required;
	}

	public void setDiscount_given(Double param) {
		this.discount_given = param;
	}

	public Double getDiscount_given() {
		return discount_given;
	}

}