package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Role;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-07T10:39:55")
@StaticMetamodel(Department.class)
public class Department_ { 

    public static volatile SingularAttribute<Department, Long> department_id;
    public static volatile CollectionAttribute<Department, Role> roles;
    public static volatile SingularAttribute<Department, String> department_name;

}