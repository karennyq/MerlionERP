package org.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "RAWMATERIAL")
public class RawMaterial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long raw_material_id;
    @Basic
    private String mat_name;
    @OneToMany(mappedBy = "rawMaterial")
    private Collection<RawMatBatch> rawMatBatches;
    @OneToMany(mappedBy = "rawMaterial")
	private Collection<RawMaterialDetail> rawMaterialDetails;

    public void setRaw_material_id(Long param) {
        this.raw_material_id = param;
    }

    public Long getRaw_material_id() {
        return raw_material_id;
    }

    public Collection<RawMatBatch> getRawMatBatches() {
        return rawMatBatches;
    }

    public void setRawMatBatches(Collection<RawMatBatch> param) {
        this.rawMatBatches = param;
    }

    public String getMat_name() {
        return mat_name;
    }

    public void setMat_name(String mat_name) {
        this.mat_name = mat_name;
    }

	public Collection<RawMaterialDetail> getRawMaterialDetails() {
	    return rawMaterialDetails;
	}

	public void setRawMaterialDetails(Collection<RawMaterialDetail> param) {
	    this.rawMaterialDetails = param;
	}
}