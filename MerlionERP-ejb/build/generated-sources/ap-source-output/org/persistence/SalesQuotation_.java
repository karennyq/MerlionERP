package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.PurchaseOrder;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-06T12:27:25")
@StaticMetamodel(SalesQuotation.class)
public class SalesQuotation_ extends SalesInquiry_ {

    public static volatile SingularAttribute<SalesQuotation, Integer> indicative_lead_time;
    public static volatile SingularAttribute<SalesQuotation, Date> expiry_date;
    public static volatile SingularAttribute<SalesQuotation, Double> discount;
    public static volatile CollectionAttribute<SalesQuotation, PurchaseOrder> purchaseOrders;

}