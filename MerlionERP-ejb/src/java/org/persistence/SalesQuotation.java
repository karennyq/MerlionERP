package org.persistence;

import java.util.ArrayList;
import javax.persistence.*;

//import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
public class SalesQuotation extends SalesInquiry {

    private static final long serialVersionUID = 1L;
    @Basic
    @Temporal(TIMESTAMP)
    private Date expiry_date;
    @Basic
    private Integer indicative_lead_time;
   
    @Basic
    private Double discount;


    @OneToMany(mappedBy = "salesQuotation")
    private Collection<PurchaseOrder> purchaseOrders;
    
    
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
    
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setExpiry_date(Date param) {
        this.expiry_date = param;
    }

    public Date getExpiry_date() {
        return expiry_date;
    }

    public void setIndicative_lead_time(Integer param) {
        this.indicative_lead_time = param;
    }

    public Integer getIndicative_lead_time() {
        return indicative_lead_time;
    }


    public Collection<PurchaseOrder> getPurchaseOrders() {
        return purchaseOrders;
    }

    public void setPurchaseOrders(Collection<PurchaseOrder> param) {
        this.purchaseOrders = param;
    }

    /*
     * public double getQuotationPrice() { double totalPrice=0; for(int
     * i=0;i<this.getLineItems().size();i++){ ArrayList<LineItem> al= new
     * ArrayList<LineItem>(this.getLineItems()); LineItem item =
     * (LineItem)al.get(i); double
     * discountedPrice=item.getBase_price()*item.getBulk_discount();
     * totalPrice=totalPrice+discountedPrice; } return totalPrice; }
     */
}