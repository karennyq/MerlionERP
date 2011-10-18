package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.PdtBatchMovement;
import org.persistence.Product;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-18T00:11:38")
@StaticMetamodel(DeliveryOrderDetail.class)
public class DeliveryOrderDetail_ { 

    public static volatile SingularAttribute<DeliveryOrderDetail, Product> product;
    public static volatile SingularAttribute<DeliveryOrderDetail, Integer> quantity_ordered;
    public static volatile SingularAttribute<DeliveryOrderDetail, Integer> quantity_reserved;
    public static volatile SingularAttribute<DeliveryOrderDetail, Long> delivery_order_detail_id;
    public static volatile CollectionAttribute<DeliveryOrderDetail, PdtBatchMovement> pdtBatchMovements;

}