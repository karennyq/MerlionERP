package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.DeliveryOrder.DeliveryOrderStatus;
import org.persistence.DeliveryOrder.Status;
import org.persistence.DeliveryOrderDetail;
import org.persistence.Invoice;
import org.persistence.PackingOrder;
import org.persistence.PickingOrder;
import org.persistence.SalesOrder;
import org.persistence.ShippingOrder;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-04T14:44:56")
@StaticMetamodel(DeliveryOrder.class)
public class DeliveryOrder_ { 

    public static volatile CollectionAttribute<DeliveryOrder, DeliveryOrderDetail> deliveryOrderDetails;
    public static volatile SingularAttribute<DeliveryOrder, Long> delivery_order_id;
    public static volatile SingularAttribute<DeliveryOrder, Status> status;
    public static volatile SingularAttribute<DeliveryOrder, Invoice> invoice;
    public static volatile SingularAttribute<DeliveryOrder, PickingOrder> pickingOrder;
    public static volatile SingularAttribute<DeliveryOrder, PackingOrder> packingOrder;
    public static volatile SingularAttribute<DeliveryOrder, ShippingOrder> shippingOrder;
    public static volatile SingularAttribute<DeliveryOrder, DeliveryOrderStatus> deliveryStatus;
    public static volatile SingularAttribute<DeliveryOrder, SalesOrder> salesOrder;
    public static volatile SingularAttribute<DeliveryOrder, String> destination;

}