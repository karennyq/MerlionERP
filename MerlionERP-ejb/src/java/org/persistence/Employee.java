package org.persistence;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

//import static javax.persistence.InheritanceType.TABLE_PER_CLASS;
@Entity
@Table(name = "EMPLOYEE")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum ActiveStatus {Active, Inactive}
    
    @Id
    @GeneratedValue
    private Long emp_id;
    
    @Basic
    private String emp_name;
    
    @Basic
    private String password;
    
    @Basic
    private String email;
    
    @Basic
    @Enumerated
    //@Column(nullable = false)
    private ActiveStatus active_status;
    
    @Basic
    private String nric;
    
    @Basic
    @Temporal(TIMESTAMP)
    private Date last_login_date_time;
    
    @Basic
    private Integer no_of_attempts;
    
    @ManyToMany
    private Collection<Role> roles;
    
    @OneToMany
    //@JoinColumn(name = "EMPLOYEE_emp_id", referencedColumnName = "emp_id")
    private Collection<Message> messages;
    
    @OneToMany(mappedBy = "employee")
    private Collection<AuditTrial> auditTrials;    
    
    public void setEmp_id(Long param) {
        this.emp_id = param;
    }

    public Long getEmp_id() {
        return emp_id;
    }

    public void setEmp_name(String param) {
        this.emp_name = param;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setPassword(String param) {
        this.password = param;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String param) {
        this.email = param;
    }

    public String getEmail() {
        return email;
    }

    public void setActive_status(ActiveStatus param) {
        this.active_status = param;
    }

    public ActiveStatus getActive_status() {
        return active_status;
    }

    public void setNric(String param) {
        this.nric = param;
    }

    public String getNric() {
        return nric;
    }

    public void setLast_login_date_time(Date param) {
        this.last_login_date_time = param;
    }

    public Date getLast_login_date_time() {
        return last_login_date_time;
    }

    public void setNo_of_attempts(Integer param) {
        this.no_of_attempts = param;
    }

    public Integer getNo_of_attempts() {
        return no_of_attempts;
    }
    
    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> param) {
        this.roles = param;
    }

    public Collection<Message> getMessages() {
        return messages;
    }

    public void setMessages(Collection<Message> param) {
        this.messages = param;
    }

    public Collection<AuditTrial> getAuditTrials() {
        return auditTrials;
    }

    public void setAuditTrials(Collection<AuditTrial> param) {
        this.auditTrials = param;
    }
    
}