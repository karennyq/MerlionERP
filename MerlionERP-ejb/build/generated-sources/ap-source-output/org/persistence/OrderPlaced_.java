package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-11T17:19:03")
@StaticMetamodel(OrderPlaced.class)
public class OrderPlaced_ { 

    public static volatile SingularAttribute<OrderPlaced, Integer> qty_ordered;
    public static volatile SingularAttribute<OrderPlaced, String> raw_mat_po_ref_no;
    public static volatile SingularAttribute<OrderPlaced, Date> date_created;
    public static volatile SingularAttribute<OrderPlaced, Date> date_ordered;
    public static volatile SingularAttribute<OrderPlaced, Long> order_placed_id;

}