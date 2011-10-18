package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.DeliveryOrder;
import org.persistence.Invoice.PaymentMode;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-18T00:11:38")
@StaticMetamodel(Invoice.class)
public class Invoice_ { 

    public static volatile SingularAttribute<Invoice, Date> created_date;
    public static volatile SingularAttribute<Invoice, Date> payment_date;
    public static volatile SingularAttribute<Invoice, Double> early_discount;
    public static volatile SingularAttribute<Invoice, Double> final_amt_paid;
    public static volatile SingularAttribute<Invoice, PaymentMode> payment_mode;
    public static volatile SingularAttribute<Invoice, Date> invoiced_date;
    public static volatile SingularAttribute<Invoice, DeliveryOrder> deliveryOrder;
    public static volatile SingularAttribute<Invoice, Long> invoice_id;
    public static volatile SingularAttribute<Invoice, Double> amt_due;

}