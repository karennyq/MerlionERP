package org.persistence;

import javax.persistence.*;
import org.persistence.WeeklyOverview;
import java.util.Collection;

@Entity
@Table(name = "MONTHLYOVERVIEW")
public class MonthlyOverview extends ProductionOverview {
	private static final long serialVersionUID = 1L;

	@Basic
	private Integer year;
	
	@Basic
	private String month;
	
	@OneToMany(mappedBy = "monthlyOverview")
	private Collection<WeeklyOverview> weeklyOverviews;

	public void setYear(Integer param) {
		this.year = param;
	}

	public Integer getYear() {
		return year;
	}

	public void setMonth(String param) {
		this.month = param;
	}

	public String getMonth() {
		return month;
	}
	
	public Collection<WeeklyOverview> getWeeklyOverviews() {
		return weeklyOverviews;
	}

	public void setWeeklyOverviews(Collection<WeeklyOverview> param) {
		this.weeklyOverviews = param;
	}

}