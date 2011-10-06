package org.persistence;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "PDTBATCHMOVEMENT")
public class PdtBatchMovement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long pdt_batch_movement_id;

	public void setPdt_batch_movement_id(Long param) {
		this.pdt_batch_movement_id = param;
	}

	public Long getPdt_batch_movement_id() {
		return pdt_batch_movement_id;
	}

}