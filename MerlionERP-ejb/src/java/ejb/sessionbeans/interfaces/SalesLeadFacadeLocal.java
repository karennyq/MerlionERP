/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.persistence.SalesLead;

/**
 *
 * @author Ken
 */
@Local
public interface SalesLeadFacadeLocal {

    void create(SalesLead salesLead);

    void edit(SalesLead salesLead);

    void remove(SalesLead salesLead);

    SalesLead find(Object id);

    List<SalesLead> findAll();

    List<SalesLead> findRange(int[] range);

    int count();

    public java.util.Collection findFilteredSalesLead(int page, int rows, java.lang.String sort, java.lang.String order, java.lang.String salesLeadID, java.lang.String compName);

    public int countFilteredSalesLead(int page, int rows, java.lang.String sort, java.lang.String order, java.lang.String salesLeadID, java.lang.String compName);

    public void createSalesLead(String company_name, String contact_person, String contact_no, String email, String remarks,
            String company_add, String fax_no, String country, String city);
    
    boolean salesLeadCompNameExist(String compName);

    public void updateSalesLead(String inquirer_id, String company_name, String contact_person, String contact_no, String email,
            String remarks, String company_add, String fax_no, String country, String city, String sales_lead_status);
    
    boolean verifyCompName (String salesLeadID, String compName);

    public java.util.Collection findNotConvertedSalesLead(String companyName);

    public int countNotConvertedSalesLead(String companyName);

    public int countAllInquirer();

    public java.util.Collection findAllInquirer();

    public SalesLead findInquirer(Long id);
}
