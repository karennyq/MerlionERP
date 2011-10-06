package org.persistence;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "SALESORDER")
public class SalesOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Status {
        Pending, Confirmed, Cancelled
    }
    public enum CreditCheck {
        Pending, Approved, Not_Approved
    }
    public enum ATPCheck {
        Pending, Sufficient, Not_Sufficient
    }
    @Id
    @GeneratedValue
    private Long so_id;
    @Basic
    @Enumerated
    private Status status;
    @Basic
    @Enumerated
    private CreditCheck creditCheck;
    @Basic
    @Enumerated
    private ATPCheck atpCheck;    
    @Basic
    @Temporal(TIMESTAMP)
    private Date completed_date;
    @Basic
    private Integer lead_time;
    @Basic
    private Double discount;
    @Basic
    private Double deposit_requested;
    @OneToOne(mappedBy = "salesOrder")
    private PurchaseOrder purchaseOrder;
    @OneToMany
    // @JoinColumn(name = "T_SALEORDER_id", referencedColumnName = "id")
    private Collection<LineItem> lineItems;
    @OneToMany(mappedBy = "salesOrder")
    private Collection<BackOrder> backOrders;
    @OneToMany(mappedBy = "salesOrder")
    private Collection<DeliveryOrder> deliveryOrders;
    @Basic
    @Temporal(TIMESTAMP)
    private Date date_confirmed;
    @Basic
    @Temporal(TIMESTAMP)
    private Date date_creation;
    
    @Transient
    private Double actual_total;
    
    @Transient
    private Double discounted_total;

    public Double getActual_total() {
        setActual_total();
        
        return actual_total;
    }

    public void setActual_total() {
        ArrayList<LineItem> list = (this.getLineItems() == null) ? new ArrayList<LineItem>(): new ArrayList<LineItem>(this.getLineItems());
        double total = 0.0;
        for (LineItem li : list) {
            total = total + li.getActual_price();
        }
        this.actual_total = total;
    }

    public Double getDiscounted_total() {
        setDiscounted_total();
        return discounted_total;
    }

    public void setDiscounted_total() {

        double discTotal = this.getActual_total() * (1 - this.discount/100);
        this.discounted_total = discTotal;

    }
    public void setDiscounted_total(double val){
    this.discounted_total=val;
    }
    
    public void setSo_id(Long param) {
        this.so_id = param;
    }

    public Long getSo_id() {
        return so_id;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setStatus(Status param) {
        this.status = param;
    }

    public Status getStatus() {
        return status;
    }

    public void setCompleted_date(Date param) {
        this.completed_date = param;
    }

    public Date getCompleted_date() {
        return completed_date;
    }

    public void setLead_time(Integer param) {
        this.lead_time = param;
    }

    public Integer getLead_time() {
        return lead_time;
    }

    public void setDeposit_requested(Double param) {
        this.deposit_requested = param;
    }

    public Double getDeposit_requested() {
        return deposit_requested;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder param) {
        this.purchaseOrder = param;
    }

    public Collection<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Collection<LineItem> param) {
        this.lineItems = param;
    }

    public Collection<BackOrder> getBackOrders() {
        return backOrders;
    }

    public void setBackOrders(Collection<BackOrder> param) {
        this.backOrders = param;
    }

    public Collection<DeliveryOrder> getDeliveryOrders() {
        return deliveryOrders;
    }

    public void setDeliveryOrders(Collection<DeliveryOrder> param) {
        this.deliveryOrders = param;
    }

    public void setDate_confirmed(Date param) {
        this.date_confirmed = param;
    }

    public Date getDate_confirmed() {
        return date_confirmed;
    }

    public void setDate_creation(Date param) {
        this.date_creation = param;
    }

    public Date getDate_creation() {
        return date_creation;
    }

    public ATPCheck getAtpCheck() {
        return atpCheck;
    }

    public void setAtpCheck(ATPCheck atpCheck) {
        this.atpCheck = atpCheck;
    }

    public CreditCheck getCreditCheck() {
        return creditCheck;
    }

    public void setCreditCheck(CreditCheck creditCheck) {
        this.creditCheck = creditCheck;
    }

  
    
}