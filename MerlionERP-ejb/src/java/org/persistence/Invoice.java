package org.persistence;

import java.io.Serializable;
import javax.persistence.*;

import org.persistence.DeliveryOrder;
import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "INVOICE")
public class Invoice implements Serializable {
	private static final long serialVersionUID = 1L;
	public enum PaymentMode {Cash, Cheque}
	
	@Id
	@GeneratedValue
	private Long invoice_id;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date created_date;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date invoiced_date;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date payment_date;
	
	@Basic
	@Enumerated
	private PaymentMode payment_mode;
	
	@Basic
	private Double amt_due;
	
	@Basic
	private Double early_discount;
	
	@Basic
	private Double final_amt_paid;
	
	@OneToOne(mappedBy = "invoice")
	private DeliveryOrder deliveryOrder;

	public void setInvoice_id(Long param) {
		this.invoice_id = param;
	}

	public Long getInvoice_id() {
		return invoice_id;
	}

	public void setCreated_date(Date param) {
		this.created_date = param;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setInvoiced_date(Date param) {
		this.invoiced_date = param;
	}

	public Date getInvoiced_date() {
		return invoiced_date;
	}

	public void setPayment_date(Date param) {
		this.payment_date = param;
	}

	public Date getPayment_date() {
		return payment_date;
	}

	public void setPayment_mode(PaymentMode param) {
		this.payment_mode = param;
	}

	public PaymentMode getPayment_mode() {
		return payment_mode;
	}

	public void setAmt_due(Double param) {
		this.amt_due = param;
	}

	public Double getAmt_due() {
		return amt_due;
	}

	public void setEarly_discount(Double param) {
		this.early_discount = param;
	}

	public Double getEarly_discount() {
		return early_discount;
	}

	public void setFinal_amt_paid(Double param) {
		this.final_amt_paid = param;
	}

	public Double getFinal_amt_paid() {
		return final_amt_paid;
	}
	
	public DeliveryOrder getDeliveryOrder() {
		return deliveryOrder;
	}

	public void setDeliveryOrder(DeliveryOrder param) {
		this.deliveryOrder = param;
	}

}