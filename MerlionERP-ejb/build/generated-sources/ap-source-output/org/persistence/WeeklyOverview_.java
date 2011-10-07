package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.DailyOverview;
import org.persistence.MonthlyOverview;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(WeeklyOverview.class)
public class WeeklyOverview_ extends ProductionOverview_ {

    public static volatile CollectionAttribute<WeeklyOverview, DailyOverview> dailyOverviews;
    public static volatile SingularAttribute<WeeklyOverview, Integer> week;
    public static volatile SingularAttribute<WeeklyOverview, MonthlyOverview> monthlyOverview;

}