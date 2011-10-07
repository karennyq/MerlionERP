package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.AuditTrial;
import org.persistence.Employee.ActiveStatus;
import org.persistence.Message;
import org.persistence.Role;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(Employee.class)
public class Employee_ { 

    public static volatile SingularAttribute<Employee, ActiveStatus> active_status;
    public static volatile SingularAttribute<Employee, Date> last_login_date_time;
    public static volatile SingularAttribute<Employee, String> email;
    public static volatile SingularAttribute<Employee, String> emp_name;
    public static volatile SingularAttribute<Employee, Long> emp_id;
    public static volatile CollectionAttribute<Employee, Role> roles;
    public static volatile CollectionAttribute<Employee, AuditTrial> auditTrials;
    public static volatile SingularAttribute<Employee, Integer> no_of_attempts;
    public static volatile SingularAttribute<Employee, String> password;
    public static volatile SingularAttribute<Employee, String> nric;
    public static volatile CollectionAttribute<Employee, Message> messages;

}