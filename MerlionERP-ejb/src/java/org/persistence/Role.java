package org.persistence;

import java.io.Serializable;
import javax.persistence.*;

import org.persistence.Employee;
import java.util.Collection;
import org.persistence.Department;

@Entity
@Table(name = "ROLE")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long role_id;
    @Basic
    private String role_name;
    @ManyToMany(mappedBy = "roles")
    private Collection<Employee> employees;
    @ManyToOne
    private Department department;
    @Basic
    private String[] accessRights;

    public String[] getAccessRights() {
        return accessRights;
    }

    public void setAccessRights(String[] accessRights) {
        this.accessRights = accessRights;
    }

    public void setRole_id(Long param) {
        this.role_id = param;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_name(String param) {
        this.role_name = param;
    }

    public String getRole_name() {
        return role_name;
    }

    public Collection<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Collection<Employee> param) {
        this.employees = param;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department param) {
        this.department = param;
    }
}