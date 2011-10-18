package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Account;
import org.persistence.Employee;
import org.persistence.OperatingRegion;
import org.persistence.PurchaseOrder;
import org.persistence.ShippingAddress;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-18T00:11:38")
@StaticMetamodel(Customer.class)
public class Customer_ extends SalesLead_ {

    public static volatile CollectionAttribute<Customer, OperatingRegion> operatingRegions;
    public static volatile CollectionAttribute<Customer, ShippingAddress> shippingAddresses;
    public static volatile SingularAttribute<Customer, Account> account;
    public static volatile SingularAttribute<Customer, Employee> employee;
    public static volatile SingularAttribute<Customer, Date> convert_date;
    public static volatile CollectionAttribute<Customer, PurchaseOrder> purchaseOrders;

}