package org.persistence;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.OneToOne;

@Entity
@Table(name = "DAILYOVERVIEW")
public class DailyOverview extends ProductionOverview {

    private static final long serialVersionUID = 1L;
    @ManyToOne
    private WeeklyOverview weeklyOverview;
    @ManyToOne
    private DailyActivity dailyActivity;
    @Basic
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date day;
    @OneToOne
    private PublicHoliday publicHoliday;

    public WeeklyOverview getWeeklyOverview() {
        return weeklyOverview;
    }

    public void setWeeklyOverview(WeeklyOverview param) {
        this.weeklyOverview = param;
    }

    public DailyActivity getDailyActivity() {
        return dailyActivity;
    }

    public void setDailyActivity(DailyActivity param) {
        this.dailyActivity = param;
    }

    public void setDay(Date param) {
        this.day = param;
    }

    public Date getDay() {
        return day;
    }

    public PublicHoliday getPublicHoliday() {
        return publicHoliday;
    }

    public void setPublicHoliday(PublicHoliday param) {
        this.publicHoliday = param;
    }
}