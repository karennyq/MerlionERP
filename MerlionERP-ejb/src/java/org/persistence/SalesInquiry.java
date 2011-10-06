package org.persistence;

import java.io.Serializable;
import javax.persistence.*;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "SALESINQUIRY")
@Inheritance(strategy = JOINED)
public class SalesInquiry implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Priority {

        High, Average, Low
    }

    public enum InquirySource {

        Email, Phone, Fax
    }

    public enum InquiryStatus {

        Not_Converted, Converted
    }

    public enum Status {

        Active, Inactive
    }
    @Id
    @GeneratedValue
    private Long pre_sale_doc_id;
    @Basic
    @Temporal(TIMESTAMP)
    private Date request_date;
    @Basic
    private String remarks;
    @Basic
    @Enumerated
    private Priority priority;
    @Basic
    @Enumerated
    private InquirySource inquiry_source;
    @Basic
    @Enumerated
    private Status status;
    @Basic
    @Enumerated
    private InquiryStatus inquiryStatus;
    @ManyToOne
    private SalesLead inquirer;
    @OneToMany
    // @JoinColumn(name = "T_SALESINQUIRY_id", referencedColumnName = "id")
    private Collection<LineItem> lineItems;
    @Transient
    private Double total_price;

    public InquiryStatus getInquiryStatus() {
        return inquiryStatus;
    }

    public void setInquiryStatus(InquiryStatus inquiryStatus) {
        this.inquiryStatus = inquiryStatus;
    }

    public void setTotal_price() {
        ArrayList<LineItem> itemList = new ArrayList<LineItem>(lineItems);
        double total = 0;
        for (int i = 0; i < itemList.size(); i++) {
            LineItem li = (LineItem) itemList.get(i);
            double itemPrice = (li.getActual_price() == null) ? 0 : li.getActual_price();
            total = total + itemPrice;
        }
        this.total_price = total;
    }

    public Double getTotal_price() {
        setTotal_price();
        return this.total_price;
    }

    public void setPre_sale_doc_id(Long param) {
        this.pre_sale_doc_id = param;
    }

    public Long getPre_sale_doc_id() {
        return pre_sale_doc_id;
    }

    public void setRequest_date(Date param) {
        this.request_date = param;
    }

    public Date getRequest_date() {
        return request_date;
    }

    public void setRemarks(String param) {
        this.remarks = param;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setPriority(Priority param) {
        this.priority = param;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setInquiry_source(InquirySource param) {
        this.inquiry_source = param;
    }

    public InquirySource getInquiry_source() {
        return inquiry_source;
    }

    public void setStatus(Status param) {
        this.status = param;
    }

    public Status getStatus() {
        return status;
    }

    public SalesLead getInquirer() {
        return inquirer;
    }

    public void setInquirer(SalesLead param) {
        this.inquirer = param;
    }

    public Collection<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Collection<LineItem> param) {
        this.lineItems = param;
    }
}