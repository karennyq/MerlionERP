package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.DeliveryOrder;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(ShippingOrder.class)
public class ShippingOrder_ { 

    public static volatile SingularAttribute<ShippingOrder, Date> created_date;
    public static volatile SingularAttribute<ShippingOrder, Long> shipping_order_id;
    public static volatile SingularAttribute<ShippingOrder, DeliveryOrder> deliveryOrder;
    public static volatile SingularAttribute<ShippingOrder, Date> shipped_date;

}