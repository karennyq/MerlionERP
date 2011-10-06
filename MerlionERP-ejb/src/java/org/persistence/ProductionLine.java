package org.persistence;

import java.io.Serializable;

import javax.persistence.*;

import org.persistence.DailyActivity;
import java.util.Collection;

@Entity
@Table(name = "PRODUCTIONLINE")
public class ProductionLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long production_line_id;
	
	@Basic
	private String production_line_name;
	
	@OneToMany
	//@JoinColumn(name = "PRODUCTIONLINE_production_line_id", referencedColumnName = "production_line_id")
	private Collection<DailyActivity> dailyActivities;
	
	public void setProduction_line_id(Long param) {
		this.production_line_id = param;
	}

	public Long getProduction_line_id() {
		return production_line_id;
	}

	public void setProduction_line_name(String param) {
		this.production_line_name = param;
	}

	public String getProduction_line_name() {
		return production_line_name;
	}

	public Collection<DailyActivity> getDailyActivities() {
		return dailyActivities;
	}

	public void setDailyActivities(Collection<DailyActivity> param) {
		this.dailyActivities = param;
	}

}