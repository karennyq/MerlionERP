package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.OrderPlaced;
import org.persistence.RawMatBatch;
import org.persistence.Supplier;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-04T14:44:56")
@StaticMetamodel(RawMaterial.class)
public class RawMaterial_ { 

    public static volatile SingularAttribute<RawMaterial, Integer> shelf_life;
    public static volatile SingularAttribute<RawMaterial, Integer> storage_unit;
    public static volatile SingularAttribute<RawMaterial, Integer> lot_size;
    public static volatile CollectionAttribute<RawMaterial, OrderPlaced> ordersPlaced;
    public static volatile SingularAttribute<RawMaterial, Integer> lead_time;
    public static volatile CollectionAttribute<RawMaterial, RawMatBatch> rawMatBatches;
    public static volatile SingularAttribute<RawMaterial, Long> raw_material_id;
    public static volatile SingularAttribute<RawMaterial, Supplier> supplier;

}