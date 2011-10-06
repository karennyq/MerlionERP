package org.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "DELIVERYORDER")
public class DeliveryOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum DeliveryOrderStatus {

        Pending, Picked, Packed, Shipped
    }

    public enum Status {

        Active, Inactive
    }
    @Id
    @GeneratedValue
    private Long delivery_order_id;
    @Basic
    private String destination;
    @Basic
    @Enumerated
    private DeliveryOrderStatus deliveryStatus;
    @Basic
    @Enumerated
    private Status status;
    @ManyToOne
    private SalesOrder salesOrder;
    @OneToOne
    private ShippingOrder shippingOrder;
    @OneToOne
    private PickingOrder pickingOrder;
    @OneToOne
    private PackingOrder packingOrder;
    @OneToOne
    private Invoice invoice;
    @OneToMany
    //@JoinColumn(name = "DELIVERYORDER_id", referencedColumnName ="delivery_order_detail_id")
    private Collection<DeliveryOrderDetail> deliveryOrderDetails;

    public void setDelivery_order_id(Long param) {
        this.delivery_order_id = param;
    }

    public Long getDelivery_order_id() {
        return delivery_order_id;
    }

    public void setDestination(String param) {
        this.destination = param;
    }

    public String getDestination() {
        return destination;
    }

    public void setDeliveryStatus(DeliveryOrderStatus param) {
        this.deliveryStatus = param;
    }

    public DeliveryOrderStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder param) {
        this.salesOrder = param;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder param) {
        this.shippingOrder = param;
    }

    public PickingOrder getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(PickingOrder param) {
        this.pickingOrder = param;
    }

    public PackingOrder getPackingOrder() {
        return packingOrder;
    }

    public void setPackingOrder(PackingOrder param) {
        this.packingOrder = param;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice param) {
        this.invoice = param;
    }

    public Collection<DeliveryOrderDetail> getDeliveryOrderDetails() {
        return deliveryOrderDetails;
    }

    public void setDeliveryOrderDetails(Collection<DeliveryOrderDetail> param) {
        this.deliveryOrderDetails = param;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}