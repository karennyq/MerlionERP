package org.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "DELIVERYORDERDETAIL")
public class DeliveryOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long delivery_order_detail_id;
    @Basic
    private Integer quantity_ordered;
    @Basic
    private Integer quantity_reserved;
    @OneToMany
    //@JoinColumn(name = "DELIVERYORDERDETAIL_delivery_order_detail_id", referencedColumnName = "delivery_order_detail_id")
    private Collection<PdtBatchMovement> pdtBatchMovements;
    @ManyToOne
    // @JoinColumn(name = "DELIVERYORDERDETAIL_id", referencedColumnName ="product_id")
    private Product product;

    public void setDelivery_order_detail_id(Long param) {
        this.delivery_order_detail_id = param;
    }

    public Long getDelivery_order_detail_id() {
        return delivery_order_detail_id;
    }

    public void setQuantity_ordered(Integer param) {
        this.quantity_ordered = param;
    }

    public Integer getQuantity_ordered() {
        return quantity_ordered;
    }

    public void setQuantity_reserved(Integer param) {
        this.quantity_reserved = param;
    }

    public Integer getQuantity_reserved() {
        return quantity_reserved;
    }

    public Collection<PdtBatchMovement> getPdtBatchMovements() {
        return pdtBatchMovements;
    }

    public void setPdtBatchMovements(Collection<PdtBatchMovement> param) {
        this.pdtBatchMovements = param;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product param) {
        this.product = param;
    }
}