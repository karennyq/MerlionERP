package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Customer;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-11T17:19:03")
@StaticMetamodel(SoleDistribution.class)
public class SoleDistribution_ { 

    public static volatile SingularAttribute<SoleDistribution, String> region;
    public static volatile SingularAttribute<SoleDistribution, Long> sole_dis_id;
    public static volatile SingularAttribute<SoleDistribution, Customer> customer;

}