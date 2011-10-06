package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.DailyActivity;
import org.persistence.PublicHoliday;
import org.persistence.WeeklyOverview;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-06T12:27:25")
@StaticMetamodel(DailyOverview.class)
public class DailyOverview_ extends ProductionOverview_ {

    public static volatile SingularAttribute<DailyOverview, DailyActivity> dailyActivity;
    public static volatile SingularAttribute<DailyOverview, PublicHoliday> publicHoliday;
    public static volatile SingularAttribute<DailyOverview, WeeklyOverview> weeklyOverview;
    public static volatile SingularAttribute<DailyOverview, Date> day;

}