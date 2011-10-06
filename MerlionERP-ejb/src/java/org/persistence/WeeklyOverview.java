package org.persistence;

import javax.persistence.*;
import org.persistence.MonthlyOverview;
import org.persistence.DailyOverview;
import java.util.Collection;

@Entity
@Table(name = "WEEKLYOVERVIEW")
public class WeeklyOverview extends ProductionOverview {
	private static final long serialVersionUID = 1L;

	@Basic
	private Integer week;
	
	@ManyToOne
	private MonthlyOverview monthlyOverview;
	
	@OneToMany(mappedBy = "weeklyOverview")
	private Collection<DailyOverview> dailyOverviews;
	
	public void setWeek(Integer param) {
		this.week = param;
	}

	public Integer getWeek() {
		return week;
	}
	
	public MonthlyOverview getMonthlyOverview() {
		return monthlyOverview;
	}

	public void setMonthlyOverview(MonthlyOverview param) {
		this.monthlyOverview = param;
	}

	public Collection<DailyOverview> getDailyOverviews() {
		return dailyOverviews;
	}

	public void setDailyOverviews(Collection<DailyOverview> param) {
		this.dailyOverviews = param;
	}

}