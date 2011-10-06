package org.persistence;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;
import org.persistence.RawMaterial;
import org.persistence.RawMatBatchMovement;
import java.util.Collection;

@Entity
@Table(name = "RAWMATBATCH")
public class RawMatBatch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long raw_mat_batch_id;

	@Basic
	@Temporal(TIMESTAMP)
	private Date inbound_date;

	@Basic
	@Temporal(TIMESTAMP)
	private Date expired_date;

	@Basic
	private Double kg_count;

	@Basic
	private Double kg_remaining;

	@ManyToOne
	private RawMaterial rawMaterial;

	@OneToMany
	// @JoinColumn(name = "RAWMATBATCH_raw_mat_batch_id", referencedColumnName =
	// "raw_mat_batch_id")
	private Collection<RawMatBatchMovement> rawMatBatchMovements;

	@Basic
	private Integer total_storage_unit;

	@Basic
	private String location;

	public void setRaw_mat_batch_id(Long param) {
		this.raw_mat_batch_id = param;
	}

	public Long getRaw_mat_batch_id() {
		return raw_mat_batch_id;
	}

	public void setInbound_date(Date param) {
		this.inbound_date = param;
	}

	public Date getInbound_date() {
		return inbound_date;
	}

	public void setExpired_date(Date param) {
		this.expired_date = param;
	}

	public Date getExpired_date() {
		return expired_date;
	}

	public void setKg_count(Double param) {
		this.kg_count = param;
	}

	public Double getKg_count() {
		return kg_count;
	}

	public void setKg_remaining(Double param) {
		this.kg_remaining = param;
	}

	public Double getKg_remaining() {
		return kg_remaining;
	}

	public RawMaterial getRawMaterial() {
		return rawMaterial;
	}

	public void setRawMaterial(RawMaterial param) {
		this.rawMaterial = param;
	}

	public Collection<RawMatBatchMovement> getRawMatBatchMovements() {
		return rawMatBatchMovements;
	}

	public void setRawMatBatchMovements(Collection<RawMatBatchMovement> param) {
		this.rawMatBatchMovements = param;
	}

	public void setTotal_storage_unit(Integer param) {
		this.total_storage_unit = param;
	}

	public Integer getTotal_storage_unit() {
		return total_storage_unit;
	}

	public void setLocation(String param) {
		this.location = param;
	}

	public String getLocation() {
		return location;
	}

}