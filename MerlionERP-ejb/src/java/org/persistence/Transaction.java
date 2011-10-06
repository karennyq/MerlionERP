package org.persistence;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "TRANSACTION")
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	public enum TransType {Credit, Deposit, Refund}	
	public enum TransactionNature {Debit, Credit}

	@Id
	@GeneratedValue
	private Long trans_id;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date trans_date_time;
	
	@Basic
    @Enumerated
	private TransType trans_type;
	
	@Basic
    @Enumerated
	private TransactionNature transaction_nature;
	
	@Basic
	private Double amt_involved;
	
	public void setTrans_id(Long param) {
		this.trans_id = param;
	}

	public Long getTrans_id() {
		return trans_id;
	}
	
	public void setTrans_date_time(Date param) {
		this.trans_date_time = param;
	}

	public Date getTrans_date_time() {
		return trans_date_time;
	}

	public void setTrans_type(TransType param) {
		this.trans_type = param;
	}

	public TransType getTrans_type() {
		return trans_type;
	}

	public void setTransaction_nature(TransactionNature param) {
		this.transaction_nature = param;
	}

	public TransactionNature getTransaction_nature() {
		return transaction_nature;
	}

	public void setAmt_involved(Double param) {
		this.amt_involved = param;
	}

	public Double getAmt_involved() {
		return amt_involved;
	}

}