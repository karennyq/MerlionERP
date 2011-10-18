package org.persistence;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "OperatingRegion")
public class OperatingRegion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long sole_dis_id;
    @ManyToOne
    private Customer customer;
    @Basic
    private String region;

    public void setSole_dis_id(Long param) {
        this.sole_dis_id = param;
    }

    public Long getSole_dis_id() {
        return sole_dis_id;
    }

    public void setRegion(String param) {
        this.region = param;
    }

    public String getRegion() {
        return region;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}