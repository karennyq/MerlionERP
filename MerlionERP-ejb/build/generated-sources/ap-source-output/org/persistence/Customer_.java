package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Account;
import org.persistence.Customer.CustomerType;
import org.persistence.Employee;
import org.persistence.PurchaseOrder;
import org.persistence.SoleDistribution;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(Customer.class)
public class Customer_ extends SalesLead_ {

    public static volatile SingularAttribute<Customer, CustomerType> cust_type;
    public static volatile CollectionAttribute<Customer, SoleDistribution> soleDistribution;
    public static volatile SingularAttribute<Customer, Account> account;
    public static volatile SingularAttribute<Customer, Employee> employee;
    public static volatile SingularAttribute<Customer, Date> convert_date;
    public static volatile CollectionAttribute<Customer, PurchaseOrder> purchaseOrders;

}