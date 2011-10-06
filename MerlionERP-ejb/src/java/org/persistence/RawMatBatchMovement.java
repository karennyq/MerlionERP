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

	public void setRaw_mat_batch_movement_id(Long param) {
		this.raw_mat_batch_movement_id = param;
	}

	public Long getRaw_mat_batch_movement_id() {
		return raw_mat_batch_movement_id;
	}

}