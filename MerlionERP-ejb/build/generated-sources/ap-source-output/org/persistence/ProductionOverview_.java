package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.PlannedDemand;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-04T14:44:56")
@StaticMetamodel(ProductionOverview.class)
public class ProductionOverview_ { 

    public static volatile SingularAttribute<ProductionOverview, Long> id;
    public static volatile SingularAttribute<ProductionOverview, Integer> working_days;
    public static volatile SingularAttribute<ProductionOverview, Double> utilization;
    public static volatile SingularAttribute<ProductionOverview, Double> ot_utilization;
    public static volatile SingularAttribute<ProductionOverview, Double> capacity;
    public static volatile CollectionAttribute<ProductionOverview, PlannedDemand> plannedDemand;
    public static volatile SingularAttribute<ProductionOverview, Double> ot_capacity;

}