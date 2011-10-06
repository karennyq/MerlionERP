package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.WeeklyOverview;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-06T12:27:25")
@StaticMetamodel(MonthlyOverview.class)
public class MonthlyOverview_ extends ProductionOverview_ {

    public static volatile SingularAttribute<MonthlyOverview, String> month;
    public static volatile SingularAttribute<MonthlyOverview, Integer> year;
    public static volatile CollectionAttribute<MonthlyOverview, WeeklyOverview> weeklyOverviews;

}