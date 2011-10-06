package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Supplier;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-04T14:44:56")
@StaticMetamodel(OrderPlaced.class)
public class OrderPlaced_ { 

    public static volatile SingularAttribute<OrderPlaced, Long> order_placed_id;
    public static volatile SingularAttribute<OrderPlaced, Supplier> supplier;

}