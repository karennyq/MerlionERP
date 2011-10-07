package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Department;
import org.persistence.Employee;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(Role.class)
public class Role_ { 

    public static volatile SingularAttribute<Role, String[]> accessRights;
    public static volatile SingularAttribute<Role, Long> role_id;
    public static volatile SingularAttribute<Role, Department> department;
    public static volatile SingularAttribute<Role, String> role_name;
    public static volatile CollectionAttribute<Role, Employee> employees;

}