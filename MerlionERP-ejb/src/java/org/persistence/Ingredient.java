package org.persistence;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.persistence.RawMaterial;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "INGREDIENT")
public class Ingredient implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long ingredient_id;
	
	@Basic
	private Integer quantity;
	
	@ManyToOne
	private RawMaterial rawMaterial;

	public void setIngredient_id(Long param) {
		this.ingredient_id = param;
	}

	public Long getIngredient_id() {
		return ingredient_id;
	}

	public void setQuantity(Integer param) {
		this.quantity = param;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public RawMaterial getRawMaterial() {
	    return rawMaterial;
	}

	public void setRawMaterial(RawMaterial param) {
	    this.rawMaterial = param;
	}

}