package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.BulkDiscount;
import org.persistence.DeliveryOrderDetail;
import org.persistence.Ingredient;
import org.persistence.PdtBatch;
import org.persistence.SalesForecast;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-06T12:27:25")
@StaticMetamodel(Product.class)
public class Product_ { 

    public static volatile CollectionAttribute<Product, BulkDiscount> bulkDiscounts;
    public static volatile SingularAttribute<Product, Integer> safety_stock_boxes;
    public static volatile SingularAttribute<Product, Long> product_id;
    public static volatile SingularAttribute<Product, String> product_name;
    public static volatile SingularAttribute<Product, Integer> avail_boxes;
    public static volatile CollectionAttribute<Product, PdtBatch> pdtBatches;
    public static volatile SingularAttribute<Product, Integer> production_capacity;
    public static volatile CollectionAttribute<Product, Ingredient> ingredients;
    public static volatile SingularAttribute<Product, Integer> reserved_boxes;
    public static volatile SingularAttribute<Product, Integer> storage_unit;
    public static volatile CollectionAttribute<Product, DeliveryOrderDetail> deliveryOrderDetails;
    public static volatile SingularAttribute<Product, Integer> box_size;
    public static volatile SingularAttribute<Product, Double> price_per_box;
    public static volatile CollectionAttribute<Product, SalesForecast> salesForecasts;

}