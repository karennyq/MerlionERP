package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Product;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-04T14:44:56")
@StaticMetamodel(LineItem.class)
public class LineItem_ { 

    public static volatile SingularAttribute<LineItem, Product> product;
    public static volatile SingularAttribute<LineItem, Double> base_price;
    public static volatile SingularAttribute<LineItem, Long> line_item_id;
    public static volatile SingularAttribute<LineItem, Integer> quantity;
    public static volatile SingularAttribute<LineItem, Integer> indicative_lead_time;
    public static volatile SingularAttribute<LineItem, Double> bulk_discount;

}