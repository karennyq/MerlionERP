package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.BackOrder;
import org.persistence.DeliveryOrder;
import org.persistence.LineItem;
import org.persistence.PurchaseOrder;
import org.persistence.SalesOrder.ATPCheck;
import org.persistence.SalesOrder.CreditCheck;
import org.persistence.SalesOrder.Status;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-18T00:11:38")
@StaticMetamodel(SalesOrder.class)
public class SalesOrder_ { 

    public static volatile CollectionAttribute<SalesOrder, DeliveryOrder> deliveryOrders;
    public static volatile SingularAttribute<SalesOrder, Date> date_creation;
    public static volatile SingularAttribute<SalesOrder, Status> status;
    public static volatile SingularAttribute<SalesOrder, Date> date_confirmed;
    public static volatile SingularAttribute<SalesOrder, CreditCheck> creditCheck;
    public static volatile SingularAttribute<SalesOrder, Double> discount;
    public static volatile SingularAttribute<SalesOrder, Date> completed_date;
    public static volatile SingularAttribute<SalesOrder, ATPCheck> atpCheck;
    public static volatile SingularAttribute<SalesOrder, Double> deposit_requested;
    public static volatile SingularAttribute<SalesOrder, Integer> lead_time;
    public static volatile SingularAttribute<SalesOrder, Long> so_id;
    public static volatile SingularAttribute<SalesOrder, PurchaseOrder> purchaseOrder;
    public static volatile CollectionAttribute<SalesOrder, BackOrder> backOrders;
    public static volatile CollectionAttribute<SalesOrder, LineItem> lineItems;

}