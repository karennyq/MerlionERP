package org.persistence;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "PUBLICHOLIDAY")
public class PublicHoliday implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long pub_holi_id;
	
	@Basic
	private String ph_name;
	
	@Basic
	@Temporal(DATE)
	private Date ph_date;

	public void setPub_holi_id(Long param) {
		this.pub_holi_id = param;
	}

	public Long getPub_holi_id() {
		return pub_holi_id;
	}

	public void setPh_name(String param) {
		this.ph_name = param;
	}

	public String getPh_name() {
		return ph_name;
	}

	public void setPh_date(Date param) {
		this.ph_date = param;
	}

	public Date getPh_date() {
		return ph_date;
	}

}