package org.persistence;

import java.io.Serializable;

import javax.persistence.*;

import org.persistence.DailyOverview;
import java.util.Collection;

@Entity
@Table(name = "DAILYACTIVITY")
public class DailyActivity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long daily_activity_id;
	
	@Basic
	private Integer ot_utilization;
	
	@Basic
	private Integer ot_capacity;
	
	@Basic
	private Integer utilization;
	
	@Basic
	private Integer capacity;
	
	@OneToMany(mappedBy = "dailyActivity")
	private Collection<DailyOverview> dailyOverviews;

	public void setDaily_activity_id(Long param) {
		this.daily_activity_id = param;
	}

	public Long getDaily_activity_id() {
		return daily_activity_id;
	}

	public void setOt_utilization(Integer param) {
		this.ot_utilization = param;
	}

	public Integer getOt_utilization() {
		return ot_utilization;
	}

	public void setOt_capacity(Integer param) {
		this.ot_capacity = param;
	}

	public Integer getOt_capacity() {
		return ot_capacity;
	}

	public void setUtilization(Integer param) {
		this.utilization = param;
	}

	public Integer getUtilization() {
		return utilization;
	}

	public void setCapacity(Integer param) {
		this.capacity = param;
	}

	public Integer getCapacity() {
		return capacity;
	}
	
	public Collection<DailyOverview> getDailyOverviews() {
		return dailyOverviews;
	}

	public void setDailyOverviews(Collection<DailyOverview> param) {
		this.dailyOverviews = param;
	}

}