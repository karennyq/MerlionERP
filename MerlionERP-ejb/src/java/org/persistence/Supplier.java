package org.persistence;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;
import javax.persistence.OneToMany;
import javax.persistence.Basic;
import javax.persistence.Enumerated;

@Entity
@Table(name = "SUPPLIER")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum SupplierStatus {

        Active, Inactive
    }
    
    @Id
    @GeneratedValue
    private Long supplier_id;
    @OneToMany(mappedBy = "supplier")
    private Collection<RawMaterialDetail> rawMaterialDetail;
    @Basic
    private String supplier_name;
    
    @Basic
    @Enumerated
    private SupplierStatus supplier_status;
    
    @Basic
    private String supplier_address;

    public void setSupplier_id(Long param) {
        this.supplier_id = param;
    }

    public Long getSupplier_id() {
        return supplier_id;
    }

    public Collection<RawMaterialDetail> getRawMaterialDetail() {
        return rawMaterialDetail;
    }

    public void setRawMaterialDetail(Collection<RawMaterialDetail> param) {
        this.rawMaterialDetail = param;
    }

    public void setSupplier_name(String param) {
        this.supplier_name = param;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_address(String param) {
        this.supplier_address = param;
    }

    public String getSupplier_address() {
        return supplier_address;
    }

    public SupplierStatus getSupplier_status() {
        return supplier_status;
    }

    public void setSupplier_status(SupplierStatus supplier_status) {
        this.supplier_status = supplier_status;
    }
   
}