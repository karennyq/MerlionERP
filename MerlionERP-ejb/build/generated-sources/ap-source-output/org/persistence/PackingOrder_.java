package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.DeliveryOrder;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-06T12:27:25")
@StaticMetamodel(PackingOrder.class)
public class PackingOrder_ { 

    public static volatile SingularAttribute<PackingOrder, Date> completed_date;
    public static volatile SingularAttribute<PackingOrder, Date> created_date;
    public static volatile SingularAttribute<PackingOrder, DeliveryOrder> deliveryOrder;
    public static volatile SingularAttribute<PackingOrder, Long> pack_order_id;

}