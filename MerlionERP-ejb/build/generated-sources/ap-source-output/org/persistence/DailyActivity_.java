package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.DailyOverview;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-18T00:11:38")
@StaticMetamodel(DailyActivity.class)
public class DailyActivity_ { 

    public static volatile CollectionAttribute<DailyActivity, DailyOverview> dailyOverviews;
    public static volatile SingularAttribute<DailyActivity, Integer> utilization;
    public static volatile SingularAttribute<DailyActivity, Integer> ot_utilization;
    public static volatile SingularAttribute<DailyActivity, Integer> capacity;
    public static volatile SingularAttribute<DailyActivity, Integer> ot_capacity;
    public static volatile SingularAttribute<DailyActivity, Long> daily_activity_id;

}