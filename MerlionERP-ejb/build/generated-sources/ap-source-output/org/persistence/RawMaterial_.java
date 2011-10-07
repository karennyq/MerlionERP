package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.RawMatBatch;
import org.persistence.RawMaterialDetail;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(RawMaterial.class)
public class RawMaterial_ { 

    public static volatile SingularAttribute<RawMaterial, String> mat_name;
    public static volatile CollectionAttribute<RawMaterial, RawMatBatch> rawMatBatches;
    public static volatile CollectionAttribute<RawMaterial, RawMaterialDetail> rawMaterialDetails;
    public static volatile SingularAttribute<RawMaterial, Long> raw_material_id;

}