package org.persistence;

import static javax.persistence.InheritanceType.JOINED;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "SALESLEAD")
@Inheritance(strategy = JOINED)
public class SalesLead implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum SalesLeadStatus {Active, Inactive}
    public enum ConvertStatus {Converted, Not_Converted}
    public enum CustomerType {

        Wholesale, Direct_Sale
    }
    
    @Id
    @GeneratedValue
    private Long inquirer_id;
    
    @Basic
    @Enumerated
    private CustomerType cust_type;
    
    @Basic
    private String company_name;
    
    @Basic
    private String contact_person;
    
    @Basic
    private String contact_no;
    
    @Basic
    private String email;
    
    @Basic
    @Temporal(TIMESTAMP)
    private Date create_date_time;
    
    @Basic
    private String remarks;
    
    @Basic
    private String fax_no;
    
    @Basic
    private String company_add;
    
    @Basic
    private String country;
    
    @Basic
    private String city;
    
    @Basic
    @Enumerated
    private ConvertStatus convert_status;
    
    @Basic
    @Enumerated
    private SalesLeadStatus sales_lead_status;
    
    @OneToMany(mappedBy = "inquirer")
    private Collection<SalesInquiry> preSaleDocuments;
    
    public void setInquirer_id(Long param) {
        this.inquirer_id = param;
    }

    public Long getInquirer_id() {
        return inquirer_id;
    } 

    public void setCompany_name(String param) {
        this.company_name = param;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setContact_person(String param) {
        this.contact_person = param;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_no(String param) {
        this.contact_no = param;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setEmail(String param) {
        this.email = param;
    }

    public String getEmail() {
        return email;
    }

    public void setCreate_date_time(Date param) {
        this.create_date_time = param;
    }

    public Date getCreate_date_time() {
        return create_date_time;
    }

    public void setRemarks(String param) {
        this.remarks = param;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setFax_no(String param) {
        this.fax_no = param;
    }

    public String getFax_no() {
        return fax_no;
    }

    public void setCompany_add(String param) {
        this.company_add = param;
    }

    public String getCompany_add() {
        return company_add;
    }

    public void setCountry(String param) {
        this.country = param;
    }

    public String getCountry() {
        return country;
    }

    public void setCity(String param) {
        this.city = param;
    }

    public String getCity() {
        return city;
    }

    public void setConvert_status(ConvertStatus param) {
        this.convert_status = param;
    }

    public ConvertStatus getConvert_status() {
        return convert_status;
    }

    public void setSales_lead_status(SalesLeadStatus param) {
        this.sales_lead_status = param;
    }

    public SalesLeadStatus getSales_lead_status() {
        return sales_lead_status;
    }

    public Collection<SalesInquiry> getPreSaleDocuments() {
        return preSaleDocuments;
    }

    public void setPreSaleDocuments(Collection<SalesInquiry> param) {
        this.preSaleDocuments = param;
    }

	public CustomerType getCust_type() {
		return cust_type;
	}

	public void setCust_type(CustomerType cust_type) {
		this.cust_type = cust_type;
	}
    
    
    
}