package org.persistence;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "RAWMATBATCHMOVEMENT")
public class RawMatBatchMovement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long raw_mat_batch_movement_id;

	@Basic
	private Double kg_moved;

	@Basic
	private String location;

	@Basic
	private String purpose;

	public void setRaw_mat_batch_movement_id(Long param) {
		this.raw_mat_batch_movement_id = param;
	}

	public Long getRaw_mat_batch_movement_id() {
		return raw_mat_batch_movement_id;
	}

	public void setKg_moved(Double param) {
		this.kg_moved = param;
	}

	public Double getKg_moved() {
		return kg_moved;
	}

	public void setLocation(String param) {
		this.location = param;
	}

	public String getLocation() {
		return location;
	}

	public void setPurpose(String param) {
		this.purpose = param;
	}

	public String getPurpose() {
		return purpose;
	}

}