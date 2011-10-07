package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.RawMatBatchMovement;
import org.persistence.RawMaterial;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(RawMatBatch.class)
public class RawMatBatch_ { 

    public static volatile SingularAttribute<RawMatBatch, Long> raw_mat_batch_id;
    public static volatile SingularAttribute<RawMatBatch, Integer> total_storage_unit;
    public static volatile SingularAttribute<RawMatBatch, Date> inbound_date;
    public static volatile SingularAttribute<RawMatBatch, String> location;
    public static volatile SingularAttribute<RawMatBatch, Double> kg_remaining;
    public static volatile CollectionAttribute<RawMatBatch, RawMatBatchMovement> rawMatBatchMovements;
    public static volatile SingularAttribute<RawMatBatch, Date> expired_date;
    public static volatile SingularAttribute<RawMatBatch, Double> kg_count;
    public static volatile SingularAttribute<RawMatBatch, RawMaterial> rawMaterial;

}