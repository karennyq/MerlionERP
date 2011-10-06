package org.persistence;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * Entity implementation class for Entity: Customer
 * 
 */
@Entity
public class Customer extends SalesLead {

    private static final long serialVersionUID = 1L;

    public enum CustomerType {

        Wholesale, Direct_Sale
    }
    @Basic
    @Temporal(TIMESTAMP)
    private Date convert_date;
    @Basic
    @Enumerated
    private CustomerType cust_type;
    @OneToMany(mappedBy = "customer")
    private Collection<PurchaseOrder> purchaseOrders;
    @OneToOne
    private Account account;
    @OneToMany(mappedBy = "customer")
    private Collection<SoleDistribution> soleDistribution;
    
    @ManyToOne
    private Employee employee;

    public void setConvert_date(Date param) {
        this.convert_date = param;
    }

    public Date getConvert_date() {
        return convert_date;
    }

    public void setCust_type(CustomerType param) {
        this.cust_type = param;
    }

    public CustomerType getCust_type() {
        return cust_type;
    }

    public Collection<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Collection<PurchaseOrder> param) {
        this.purchaseOrders = param;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account param) {
        this.account = param;
    }

    public Collection<SoleDistribution> getSoleDistribution() {
        return soleDistribution;
    }

    public void setSoleDistribution(Collection<SoleDistribution> param) {
        this.soleDistribution = param;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
