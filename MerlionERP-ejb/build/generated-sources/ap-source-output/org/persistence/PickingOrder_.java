package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.DeliveryOrder;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-18T00:11:38")
@StaticMetamodel(PickingOrder.class)
public class PickingOrder_ { 

    public static volatile SingularAttribute<PickingOrder, Date> created_date;
    public static volatile SingularAttribute<PickingOrder, Date> completed_date;
    public static volatile SingularAttribute<PickingOrder, Long> pick_order_id;
    public static volatile SingularAttribute<PickingOrder, DeliveryOrder> deliveryOrder;

}