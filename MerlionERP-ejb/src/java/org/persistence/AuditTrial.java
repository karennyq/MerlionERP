package org.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "AUDITTRIAL")
public class AuditTrial implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long audit_trial_id;
	
	@Basic
	@Temporal(TIMESTAMP)
	private Date trial_date_time;
	
	@Basic
	private String entity;
	
	@Basic
	private String audit_col;
	
	@Basic
	private String old_value;
	
	@Basic
	private String new_value;
	
	@Basic
	private Long row_id;
	
	@ManyToOne
	private Employee employee;

	public void setAudit_trial_id(Long param) {
		this.audit_trial_id = param;
	}

	public Long getAudit_trial_id() {
		return audit_trial_id;
	}
	
	public void setTrial_date_time(Date param) {
		this.trial_date_time = param;
	}

	public Date getTrial_date_time() {
		return trial_date_time;
	}

	public void setEntity(String param) {
		this.entity = param;
	}

	public String getEntity() {
		return entity;
	}

	public void setAudit_col(String param) {
		this.audit_col = param;
	}

	public String getAudit_col() {
		return audit_col;
	}

	public void setOld_value(String param) {
		this.old_value = param;
	}

	public String getOld_value() {
		return old_value;
	}

	public void setNew_value(String param) {
		this.new_value = param;
	}

	public String getNew_value() {
		return new_value;
	}

	public void setRow_id(Long param) {
		this.row_id = param;
	}

	public Long getRow_id() {
		return row_id;
	}
	
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee param) {
		this.employee = param;
	}

}