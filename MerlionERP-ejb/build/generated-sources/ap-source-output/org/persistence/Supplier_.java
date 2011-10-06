package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.OrderPlaced;
import org.persistence.RawMaterial;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-04T14:44:56")
@StaticMetamodel(Supplier.class)
public class Supplier_ { 

    public static volatile CollectionAttribute<Supplier, RawMaterial> rawMaterials;
    public static volatile SingularAttribute<Supplier, Long> supplier_id;
    public static volatile CollectionAttribute<Supplier, OrderPlaced> ordersPlaced;

}