package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.OrderPlaced;
import org.persistence.RawMaterial;
import org.persistence.Supplier;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-06T12:27:25")
@StaticMetamodel(RawMaterialDetail.class)
public class RawMaterialDetail_ { 

    public static volatile SingularAttribute<RawMaterialDetail, Integer> shelf_life;
    public static volatile CollectionAttribute<RawMaterialDetail, OrderPlaced> orderPlaced;
    public static volatile SingularAttribute<RawMaterialDetail, Integer> storage_unit;
    public static volatile SingularAttribute<RawMaterialDetail, Integer> lot_size;
    public static volatile SingularAttribute<RawMaterialDetail, Integer> lead_time;
    public static volatile SingularAttribute<RawMaterialDetail, RawMaterial> rawMaterial;
    public static volatile SingularAttribute<RawMaterialDetail, Supplier> supplier;
    public static volatile SingularAttribute<RawMaterialDetail, Long> raw_mat_detail_id;

}