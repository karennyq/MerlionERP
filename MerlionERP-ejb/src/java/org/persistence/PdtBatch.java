package org.persistence;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.persistence.PdtBatchMovement;
import java.util.Collection;
import javax.persistence.OneToMany;

@Entity
@Table(name = "PDTBATCH")
public class PdtBatch implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long pdt_batch_id;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date manufactured_date;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date inbound_date;
	
	@ManyToOne
	private Product product;
	
	@Basic
	private Integer box_count;
	
	@Basic
	private Integer boxes_remaining;
	
	@OneToMany
	//@JoinColumn(name = "BATCH_batch_id", referencedColumnName = "batch_id")
	private Collection<PdtBatchMovement> pdtBatchMovements;

	public void setPdt_batch_id(Long param) {
		this.pdt_batch_id = param;
	}

	public Long getPdt_batch_id() {
		return pdt_batch_id;
	}
	
	public void setManufactured_date(Date param) {
		this.manufactured_date = param;
	}

	public Date getManufactured_date() {
		return manufactured_date;
	}

	public void setInbound_date(Date param) {
		this.inbound_date = param;
	}

	public Date getInbound_date() {
		return inbound_date;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product param) {
		this.product = param;
	}

	public void setBox_count(Integer param) {
		this.box_count = param;
	}

	public Integer getBox_count() {
		return box_count;
	}

	public void setBoxes_remaining(Integer param) {
		this.boxes_remaining = param;
	}

	public Integer getBoxes_remaining() {
		return boxes_remaining;
	}

	public Collection<PdtBatchMovement> getPdtBatchMovements() {
	    return pdtBatchMovements;
	}

	public void setPdtBatchMovements(Collection<PdtBatchMovement> param) {
	    this.pdtBatchMovements = param;
	}

}