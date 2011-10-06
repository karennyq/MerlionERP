package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.DailyActivity;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-04T14:44:56")
@StaticMetamodel(ProductionLine.class)
public class ProductionLine_ { 

    public static volatile SingularAttribute<ProductionLine, Long> production_line_id;
    public static volatile CollectionAttribute<ProductionLine, DailyActivity> dailyActivities;
    public static volatile SingularAttribute<ProductionLine, String> production_line_name;

}