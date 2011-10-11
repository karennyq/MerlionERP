package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.PdtBatchMovement;
import org.persistence.Product;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-11T17:19:03")
@StaticMetamodel(PdtBatch.class)
public class PdtBatch_ { 

    public static volatile SingularAttribute<PdtBatch, Product> product;
    public static volatile SingularAttribute<PdtBatch, Date> inbound_date;
    public static volatile SingularAttribute<PdtBatch, Long> pdt_batch_id;
    public static volatile SingularAttribute<PdtBatch, Integer> boxes_remaining;
    public static volatile SingularAttribute<PdtBatch, Integer> box_count;
    public static volatile CollectionAttribute<PdtBatch, PdtBatchMovement> pdtBatchMovements;
    public static volatile SingularAttribute<PdtBatch, Date> manufactured_date;

}