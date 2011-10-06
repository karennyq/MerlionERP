package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Customer;
import org.persistence.LineItem;
import org.persistence.PurchaseOrder.ApprovedStatus;
import org.persistence.PurchaseOrder.Status;
import org.persistence.SalesOrder;
import org.persistence.SalesQuotation;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-04T14:44:56")
@StaticMetamodel(PurchaseOrder.class)
public class PurchaseOrder_ { 

    public static volatile SingularAttribute<PurchaseOrder, Long> po_id;
    public static volatile SingularAttribute<PurchaseOrder, Status> status;
    public static volatile SingularAttribute<PurchaseOrder, Date> received_date;
    public static volatile SingularAttribute<PurchaseOrder, String> remarks;
    public static volatile SingularAttribute<PurchaseOrder, Integer> indicative_lead_time;
    public static volatile SingularAttribute<PurchaseOrder, Date> sent_date;
    public static volatile SingularAttribute<PurchaseOrder, Customer> customer;
    public static volatile SingularAttribute<PurchaseOrder, Double> discount;
    public static volatile SingularAttribute<PurchaseOrder, SalesQuotation> salesQuotation;
    public static volatile SingularAttribute<PurchaseOrder, ApprovedStatus> approved_status;
    public static volatile SingularAttribute<PurchaseOrder, String> shipping_Address;
    public static volatile CollectionAttribute<PurchaseOrder, LineItem> lineItems;
    public static volatile SingularAttribute<PurchaseOrder, SalesOrder> salesOrder;
    public static volatile SingularAttribute<PurchaseOrder, String> po_reference_no;

}