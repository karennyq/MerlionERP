package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Employee;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-18T00:11:38")
@StaticMetamodel(AuditTrial.class)
public class AuditTrial_ { 

    public static volatile SingularAttribute<AuditTrial, Long> audit_trial_id;
    public static volatile SingularAttribute<AuditTrial, Long> row_id;
    public static volatile SingularAttribute<AuditTrial, String> new_value;
    public static volatile SingularAttribute<AuditTrial, String> entity;
    public static volatile SingularAttribute<AuditTrial, String> old_value;
    public static volatile SingularAttribute<AuditTrial, Employee> employee;
    public static volatile SingularAttribute<AuditTrial, String> audit_col;
    public static volatile SingularAttribute<AuditTrial, Date> trial_date_time;

}