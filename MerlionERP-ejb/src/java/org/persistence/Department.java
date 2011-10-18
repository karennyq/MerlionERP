package org.persistence;

import java.io.Serializable;
import javax.persistence.*;

import org.persistence.Role;
import java.util.Collection;

@Entity
@Table(name = "DEPARTMENT")
public class Department implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long department_id;
	
	@Basic
	private String department_name;
	
	@OneToMany(mappedBy = "department")
	private Collection<Role> roles;
	
	public void setDepartment_id(Long param) {
		this.department_id = param;
	}

	public Long getDepartment_id() {
		return department_id;
	}
	
	public void setDepartment_name(String param) {
		this.department_name = param;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> param) {
		this.roles = param;
	}

}