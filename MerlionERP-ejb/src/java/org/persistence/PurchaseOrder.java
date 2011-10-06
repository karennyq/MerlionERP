package org.persistence;

import java.util.ArrayList;
import java.io.Serializable;
import javax.persistence.*;

import java.util.Collection;
import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "PURCHASEORDER")
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum ApprovedStatus {

        Pending, Approved, Rejected   
    }

//    public enum PaymentMode {
//
//        On_Credit, Cash_Deposit, Deposit_Cheque, Credit_and_Deposit
//    }

    public enum Status {

        Active, Inactive
    }
    
    @Id
    @GeneratedValue
    private Long po_id;
    @Basic
    private String po_reference_no;
    @Basic
    private Double discount;
//    @Basic
//    @Enumerated
//    private PaymentMode payment_mode;
    @Basic
    private String shipping_Address;
    @Basic
    @Temporal(TIMESTAMP)
    private Date sent_date;
    @Basic
    @Temporal(TIMESTAMP)
    private Date received_date;
    @Basic
    private String remarks;
    @Basic
    private Integer indicative_lead_time;
    
    @Basic
    @Enumerated
    private Status status;
    @Basic
    @Enumerated
    private ApprovedStatus approved_status;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private SalesQuotation salesQuotation;
    @OneToMany
    // @JoinColumn(name = "T_PURCHASEORDER_id", referencedColumnName = "id")
    private Collection<LineItem> lineItems;
    @OneToOne
    private SalesOrder salesOrder;
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

        double discTotal = this.getActual_total() * (1 - this.discount / 100);
        this.discounted_total = discTotal;

    }

    public void setPo_id(Long param) {
        this.po_id = param;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Long getPo_id() {
        return po_id;
    }

    public void setSent_date(Date param) {
        this.sent_date = param;
    }

    public Date getSent_date() {
        return sent_date;
    }

    public void setReceived_date(Date param) {
        this.received_date = param;
    }

    public Date getReceived_date() {
        return received_date;
    }

    public void setRemarks(String param) {
        this.remarks = param;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setApproved_status(ApprovedStatus param) {
        this.approved_status = param;
    }

    public ApprovedStatus getApproved_status() {
        return approved_status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer param) {
        this.customer = param;
    }

    public SalesQuotation getSalesQuotation() {
        return salesQuotation;
    }

    public void setSalesQuotation(SalesQuotation param) {
        this.salesQuotation = param;
    }

    public Collection<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Collection<LineItem> param) {
        this.lineItems = param;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder param) {
        this.salesOrder = param;
    }

//    public PaymentMode getPayment_mode() {
//        return payment_mode;
//    }
//
//    public void setPayment_mode(PaymentMode payment_mode) {
//        this.payment_mode = payment_mode;
//    }

    public String getShipping_Address() {
        return shipping_Address;
    }

    public void setShipping_Address(String shipping_Address) {
        this.shipping_Address = shipping_Address;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPo_reference_no() {
        return po_reference_no;
    }

    public void setPo_reference_no(String po_reference_no) {
        this.po_reference_no = po_reference_no;
    }

    public Integer getIndicative_lead_time() {
        return indicative_lead_time;
    }

    public void setIndicative_lead_time(Integer indicative_lead_time) {
        this.indicative_lead_time = indicative_lead_time;
    }
    
    
}