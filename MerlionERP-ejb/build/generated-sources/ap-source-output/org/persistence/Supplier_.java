package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.RawMaterialDetail;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(Supplier.class)
public class Supplier_ { 

    public static volatile CollectionAttribute<Supplier, RawMaterialDetail> rawMaterialDetail;
    public static volatile SingularAttribute<Supplier, Long> supplier_id;
    public static volatile SingularAttribute<Supplier, String> supplier_name;
    public static volatile SingularAttribute<Supplier, String> supplier_address;

}