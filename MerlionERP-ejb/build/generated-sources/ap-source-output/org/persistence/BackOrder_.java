package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.SalesOrder;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-18T00:11:38")
@StaticMetamodel(BackOrder.class)
public class BackOrder_ { 

    public static volatile SingularAttribute<BackOrder, Date> created_date;
    public static volatile SingularAttribute<BackOrder, Date> completed_date;
    public static volatile SingularAttribute<BackOrder, Integer> boxes_to_produce;
    public static volatile SingularAttribute<BackOrder, Long> backorder_id;
    public static volatile SingularAttribute<BackOrder, Date> scheduled_date;
    public static volatile SingularAttribute<BackOrder, SalesOrder> salesOrder;

}