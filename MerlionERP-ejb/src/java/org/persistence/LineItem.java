package org.persistence;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "LINEITEM")
public class LineItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long line_item_id;
    @ManyToOne
    private Product product;
    @Basic
    private Double base_price;
    @Basic
    private Double bulk_discount;
    @Basic
    private Integer quantity;
    
    // get total cost
    @Transient
    private Double actual_price;
    
    @Basic
    private Integer indicative_lead_time;

    public Integer getIndicative_lead_time() {
        return indicative_lead_time;
    }

    public void setIndicative_lead_time(Integer indicative_lead_time) {
        this.indicative_lead_time = indicative_lead_time;
    }

    public void setActual_price() {
        double itemBasePrice = (this.base_price == null) ? 0 : this.base_price;
        double itemQuantity = (this.quantity == null) ? 0 : this.quantity;
        double itemBulkDiscount = (this.bulk_discount == null) ? 0
                : this.bulk_discount;
        double price = itemBasePrice
                * (itemQuantity * (1 - itemBulkDiscount / 100));
        this.actual_price = price;
    }

    public Double getActual_price() {
        this.setActual_price();
        return actual_price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product param) {
        this.product = param;
    }

    public void setLine_item_id(Long param) {
        this.line_item_id = param;
    }

    public Long getLine_item_id() {
        return line_item_id;
    }

    public void setBase_price(Double param) {
        this.base_price = param;
    }

    public Double getBase_price() {
        return base_price;
    }

    public void setBulk_discount(Double param) {
        this.bulk_discount = param;
    }

    public Double getBulk_discount() {
        return bulk_discount;
    }

    public void setQuantity(Integer param) {
        this.quantity = param;
    }

    public Integer getQuantity() {
        return quantity;
    }
}