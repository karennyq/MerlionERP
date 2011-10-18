/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.SalesLeadFacadeLocal;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.SalesLead;
import org.persistence.SalesLead.ConvertStatus;
import org.persistence.SalesLead.SalesLeadStatus;

/**
 *
 * @author Ken
 */
@Stateless
public class SalesLeadFacade extends AbstractFacade<SalesLead> implements SalesLeadFacadeLocal {

    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public SalesLeadFacade() {
        super(SalesLead.class);
    }

    @Override
    public Collection findFilteredSalesLead(int page, int rows, String sort, String order, String salesLeadID, String compName) {
        
        Query query = em.createQuery("SELECT s FROM SalesLead s WHERE TYPE(s) <> Customer AND s.convert_status = ?1 AND s.inquirer_id LIKE ?2 AND s.company_name LIKE ?3 ORDER BY s." + sort + " " + order);
        
        //for enum
        query.setParameter(1, SalesLead.ConvertStatus.valueOf("Not_Converted"));
        
        query.setParameter(2,"%"+salesLeadID+"%");
        query.setParameter(3,"%"+compName+"%");
        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<SalesLead>) query.getResultList();
        
    }

    @Override
    public int countFilteredSalesLead(int page, int rows, String sort, String order, String salesLeadID, String compName) {

        Query query = em.createQuery("SELECT s FROM SalesLead s WHERE TYPE(s) <> Customer AND s.convert_status = ?1 AND s.inquirer_id LIKE ?2 AND s.company_name LIKE ?3 ORDER BY s." + sort + " " + order);
        
        //for enum
        query.setParameter(1, SalesLead.ConvertStatus.valueOf("Not_Converted"));
        
        query.setParameter(2,"%"+salesLeadID+"%");
        query.setParameter(3,"%"+compName+"%");
        return query.getResultList().size();
    }

    @Override
    public Collection findNotConvertedSalesLead(int page, int rows, String sort, String order, String companyName) {
        // Query query = em.createQuery("SELECT s FROM SalesLead s WHERE TYPE(s)<>Customer");
        Query query = em.createQuery("SELECT s FROM SalesLead s WHERE s.convert_status=?1 AND s.company_name LIKE ?2 ORDER BY s." + sort + " " + order);
        query.setParameter(1, SalesLead.ConvertStatus.valueOf("Not_Converted"));
        query.setParameter(2, "%"+companyName+"%");
        return (Collection<SalesLead>) query.getResultList();
    }

    @Override
    public int countNotConvertedSalesLead(int page, int rows, String sort, String order, String companyName) {
        Query query = em.createQuery("SELECT s FROM SalesLead s WHERE s.convert_status=?1 AND s.company_name LIKE ?2 ORDER BY s." + sort + " " + order);
        query.setParameter(1, SalesLead.ConvertStatus.valueOf("Not_Converted"));
        query.setParameter(2, "%"+companyName+"%");
        return query.getResultList().size();
    }

    @Override
    public void createSalesLead(String company_name, String contact_person, String contact_no, String email, String remarks,
            String company_add, String fax_no, String country, String city, String cust_type) {
        SalesLead sl = new SalesLead();
        Date create_date_time = new Date();
        sl.setCompany_name(company_name);
        sl.setContact_person(contact_person);
        sl.setContact_no(contact_no);
        sl.setEmail(email);
        sl.setRemarks(remarks);
        sl.setCompany_add(company_add);
        sl.setFax_no(fax_no);
        sl.setCreate_date_time(create_date_time);
        sl.setCountry(country);
        sl.setCity(city);
        sl.setSales_lead_status(SalesLeadStatus.Active);
        sl.setConvert_status(ConvertStatus.Not_Converted);
        sl.setCust_type(SalesLead.CustomerType.valueOf(cust_type));
        create(sl);
    }

    @Override
    public boolean salesLeadCompNameExist(String compName){
        Query query = em.createQuery("SELECT s FROM SalesLead s WHERE s.company_name=?1");
        query.setParameter(1, compName);
        boolean r = query.getResultList().isEmpty();
        return (!r);
    }
    
    @Override
    public void updateSalesLead(String inquirer_id, String company_name, String contact_person, String contact_no, String email,
            String remarks, String company_add, String fax_no, String country, String city, String sales_lead_status, String cust_type) {
        SalesLead sl = find(Long.parseLong(inquirer_id));
        Date create_date_time = new Date();
        sl.setCompany_name(company_name);
        sl.setContact_person(contact_person);
        sl.setContact_no(contact_no);
        sl.setEmail(email);
        sl.setRemarks(remarks);
        sl.setCompany_add(company_add);
        sl.setFax_no(fax_no);
        sl.setCreate_date_time(create_date_time);
        sl.setCountry(country);
        sl.setCity(city);
        sl.setCust_type(SalesLead.CustomerType.valueOf(cust_type));
        
        if (sales_lead_status.equalsIgnoreCase("Active")) {
            sl.setSales_lead_status(SalesLeadStatus.Active);
        } else if (sales_lead_status.equalsIgnoreCase("Inactive")) {
            sl.setSales_lead_status(SalesLeadStatus.Inactive);
        }
        edit(sl);
    }
    
    @Override
    public boolean verifyCompName (String salesLeadID, String compName){

        Query query = em.createQuery("SELECT s FROM SalesLead s WHERE s.company_name=?1");
        query.setParameter(1, compName);
 
        boolean r = query.getResultList().isEmpty();
        boolean result = true;
        
        if (!r){
            
             SalesLead sl = (SalesLead) query.getSingleResult();
             Long extractedInquirer_id = sl.getInquirer_id();
            
            if (extractedInquirer_id == Long.parseLong(salesLeadID)){
                result = true;
            }
            else {
                result = false;
            }
        }
        return result;
    }
    

    //--------------Karen (20 Sep)
    @Override
    public Collection findAllInquirer() {
        Query query = em.createQuery("SELECT s FROM SalesLead s WHERE TYPE(s) = Customer OR s.convert_status=?1");
        query.setParameter(1, SalesLead.ConvertStatus.valueOf("Not_Converted"));
        return (Collection<SalesLead>) query.getResultList();
    }

    @Override
    public int countAllInquirer() {
        Query query = em.createQuery("SELECT s FROM SalesLead s WHERE TYPE(s) = Customer OR s.convert_status=?1");
        query.setParameter(1, SalesLead.ConvertStatus.valueOf("Not_Converted"));
        return query.getResultList().size();
    }

    @Override
    public SalesLead findInquirer(Long id) {
        Query query = em.createQuery("SELECT s FROM SalesLead s WHERE (TYPE(s) = Customer OR s.convert_status=?1) AND s.inquirer_id=?2");
        query.setParameter(1, SalesLead.ConvertStatus.valueOf("Not_Converted"));
        query.setParameter(2, id);
        SalesLead sl = (SalesLead) query.getSingleResult();
        return sl;
    }
    //--------------Karen (20 Sep)
}
