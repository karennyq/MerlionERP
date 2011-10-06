package org.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long product_id;
    @Basic
    private String product_name;
    @Basic
    private Double price_per_unit;
    @Basic
    private Integer avail_boxes;
    @Basic
    private Integer reserved_boxes;
    @Basic
    private Integer safety_stock_boxes;
    @Basic
    private Integer box_size;
    @Basic
    private Integer production_capacity;
    @Basic
    private Integer storage_unit;
    @OneToMany(mappedBy = "product")
    private Collection<PdtBatch> pdtBatches;
    @OneToMany(mappedBy = "product")
    private Collection<DeliveryOrderDetail> deliveryOrderDetails;
    @OneToMany
    // @JoinColumn(name = "PRODUCT_id", referencedColumnName ="bulk_discount_id")
    private Collection<BulkDiscount> bulkDiscounts;
    @OneToMany(mappedBy = "product")
    private Collection<SalesForecast> salesForecasts;
    @OneToMany
    //@JoinColumn(name = "PRODUCT_product_id", referencedColumnName = "product_id")
    private Collection<Ingredient> ingredients;

    public void setProduct_id(Long param) {
        this.product_id = param;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_name(String param) {
        this.product_name = param;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setPrice_per_unit(Double param) {
        this.price_per_unit = param;
    }

    public Double getPrice_per_unit() {
        return price_per_unit;
    }

    public void setAvail_boxes(Integer param) {
        this.avail_boxes = param;
    }

    public Integer getAvail_boxes() {
        return avail_boxes;
    }

    public void setReserved_boxes(Integer param) {
        this.reserved_boxes = param;
    }

    public Integer getReserved_boxes() {
        return reserved_boxes;
    }

    public void setSafety_stock_boxes(Integer param) {
        this.safety_stock_boxes = param;
    }

    public Integer getSafety_stock_boxes() {
        return safety_stock_boxes;
    }

    public void setBox_size(Integer param) {
        this.box_size = param;
    }

    public Integer getBox_size() {
        return box_size;
    }

    public void setProduction_capacity(Integer param) {
        this.production_capacity = param;
    }

    public Integer getProduction_capacity() {
        return production_capacity;
    }

    public void setStorage_unit(Integer param) {
        this.storage_unit = param;
    }

    public Integer getStorage_unit() {
        return storage_unit;
    }

    public Collection<PdtBatch> getPdtBatches() {
        return pdtBatches;
    }

    public void setPdtBatches(Collection<PdtBatch> param) {
        this.pdtBatches = param;
    }

    public Collection<BulkDiscount> getBulkDiscounts() {
        return bulkDiscounts;
    }

    public void setBulkDiscounts(Collection<BulkDiscount> param) {
        this.bulkDiscounts = param;
    }

    public Collection<SalesForecast> getSalesForecasts() {
        return salesForecasts;
    }

    public void setSalesForecasts(Collection<SalesForecast> param) {
        this.salesForecasts = param;
    }

    public Collection<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Collection<Ingredient> param) {
        this.ingredients = param;
    }

    public Collection<DeliveryOrderDetail> getDeliveryOrderDetails() {
        return deliveryOrderDetails;
    }

    public void setDeliveryOrderDetails(Collection<DeliveryOrderDetail> deliveryOrderDetails) {
        this.deliveryOrderDetails = deliveryOrderDetails;
    }
}