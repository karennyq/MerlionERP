/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet.reports;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author YvonneOng
 */
public class POReportHelperClass implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Basic
    private Long line_item_id;
    @Basic
    private String product_name;
    @Basic
    private Integer quantity;
    @Basic
    private Double base_price;
    @Basic
    private Double bulk_discount;
    @Transient
    private Double actual_price;
    @Transient
    private Double actual_total;
    @Basic
    private Double discount;
    @Transient
    private Double discounted_total;

    public Double getActual_price() {
        return actual_price;
    }

    public void setActual_price(Double actual_price) {
        this.actual_price = actual_price;
    }

    public Double getActual_total() {
        return actual_total;
    }

    public void setActual_total(Double actual_total) {
        this.actual_total = actual_total;
    }

    public Double getBase_price() {
        return base_price;
    }

    public void setBase_price(Double base_price) {
        this.base_price = base_price;
    }

    public Double getBulk_discount() {
        return bulk_discount;
    }

    public void setBulk_discount(Double bulk_discount) {
        this.bulk_discount = bulk_discount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDiscounted_total() {
        return discounted_total;
    }

    public void setDiscounted_total(Double discounted_total) {
        this.discounted_total = discounted_total;
    }

    public Long getLine_item_id() {
        return line_item_id;
    }

    public void setLine_item_id(Long line_item_id) {
        this.line_item_id = line_item_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
}
