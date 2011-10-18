package org.persistence;

import java.io.Serializable;
import static javax.persistence.TemporalType.TIMESTAMP;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Basic;
import java.util.Date;
import javax.persistence.Temporal;

@Entity
@Table(name = "ORDERPLACED")
public class OrderPlaced implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long order_placed_id;

	@Basic
	private String raw_mat_po_ref_no;

	@Basic
	private Integer qty_ordered;

	@Basic
	@Temporal(TIMESTAMP)
	private Date date_ordered;

	@Basic
	@Temporal(TIMESTAMP)
	private Date date_created;

	public void setOrder_placed_id(Long param) {
		this.order_placed_id = param;
	}

	public Long getOrder_placed_id() {
		return order_placed_id;
	}

	public void setRaw_mat_po_ref_no(String param) {
		this.raw_mat_po_ref_no = param;
	}

	public String getRaw_mat_po_ref_no() {
		return raw_mat_po_ref_no;
	}

	public void setQty_ordered(Integer param) {
		this.qty_ordered = param;
	}

	public Integer getQty_ordered() {
		return qty_ordered;
	}

	public void setDate_ordered(Date param) {
		this.date_ordered = param;
	}

	public Date getDate_ordered() {
		return date_ordered;
	}

	public void setDate_created(Date param) {
		this.date_created = param;
	}

	public Date getDate_created() {
		return date_created;
	}

}