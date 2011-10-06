package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.PlannedDemandAmendment;
import org.persistence.Product;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-06T12:27:25")
@StaticMetamodel(PlannedDemand.class)
public class PlannedDemand_ { 

    public static volatile SingularAttribute<PlannedDemand, Integer> boxes_to_produce;
    public static volatile SingularAttribute<PlannedDemand, Product> product;
    public static volatile CollectionAttribute<PlannedDemand, PlannedDemandAmendment> plannedDemandAmendment;
    public static volatile SingularAttribute<PlannedDemand, Long> planned_dmd_id;
    public static volatile SingularAttribute<PlannedDemand, Integer> ot_boxes_to_produce;
    public static volatile SingularAttribute<PlannedDemand, Double> hours_needed;
    public static volatile SingularAttribute<PlannedDemand, Double> ot_hours_needed;

}