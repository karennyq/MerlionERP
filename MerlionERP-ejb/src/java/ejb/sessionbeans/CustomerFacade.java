/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.AccountFacadeLocal;
import ejb.sessionbeans.interfaces.CustomerFacadeLocal;
import ejb.sessionbeans.interfaces.EmployeeFacadeLocal;
import ejb.sessionbeans.interfaces.SalesLeadFacadeLocal;
import ejb.sessionbeans.interfaces.SalesInquiryFacadeLocal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.Account;
import org.persistence.Customer;
import org.persistence.Customer.CustomerType;
import org.persistence.Employee;
import org.persistence.SalesInquiry;
import org.persistence.SalesLead;
import org.persistence.SalesLead.ConvertStatus;

/**
 *
 * @author Ken
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> implements CustomerFacadeLocal {
    
    @EJB
    SalesLeadFacadeLocal salesLeadFacade;
    @EJB
    AccountFacadeLocal accountFacade;
    @EJB
    SalesInquiryFacadeLocal salesInquiryFacade;
    @EJB
    EmployeeFacadeLocal employeeFacade;
    
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;
    
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public CustomerFacade() {
        super(Customer.class);
    }
    
    @Override
    public Collection findFilteredCustomers() {
        Query query = em.createQuery("SELECT c FROM Customer c");
        
        return (Collection) query.getResultList();
    }
    
    @Override
    public void createCustomer(String inquirer_id, String emp_id, String company_name, String contact_person, String contact_no, String email, String remarks,
            String company_add, String fax_no, String cust_type, String country, String city) {
        
        Customer c = new Customer();
        Date convert_date_time = new Date();
        
        Employee e = employeeFacade.find(Long.valueOf(emp_id));
        c.setEmployee(e);
        c.setCompany_name(company_name);
        c.setContact_person(contact_person);
        c.setContact_no(contact_no);
        c.setEmail(email);
        c.setRemarks(remarks);
        c.setCompany_add(company_add);
        c.setFax_no(fax_no);
        c.setConvert_date(convert_date_time);
        if (cust_type.equalsIgnoreCase("Wholesale")) {
            c.setCust_type(CustomerType.Wholesale);
        } else if (cust_type.equalsIgnoreCase("Direct Sale")) {
            c.setCust_type(CustomerType.Direct_Sale);
        }
        c.setCountry(country);
        c.setCity(city);
        
        SalesLead sl = salesLeadFacade.find(Long.parseLong(inquirer_id));
        sl.setConvert_status(ConvertStatus.Converted);
        salesLeadFacade.edit(sl);
        
        create(c);
        Account a = new Account();
        a.setCredit_amt(0.0);
        a.setDeposit_amt(0.0);
        a.setMax_credit_limit(0.0);
        a.setRefundable_amt(0.0);
        a.setAccountStatus(Account.AccountStatus.New);
        
        a.setCustomer(c);
        accountFacade.create(a);
        
        c.setAccount(a);
        edit(c);
        
        SalesLead sl1 = salesLeadFacade.find(Long.parseLong(inquirer_id));
        System.out.println(sl1.getCompany_name());
        
        //transfering sales docs
        if (sl1.getPreSaleDocuments() != null) {
            //System.out.println("heeeeeeeee");
            //if (sl.getPreSaleDocuments().size() != 0) {
            //System.out.println(sl.getPreSaleDocuments().size());    
                ArrayList<SalesInquiry> siList = new ArrayList(salesInquiryFacade.findSalesInquiryByInquirer(Long.parseLong(inquirer_id)));
                System.out.println("!!!!!!!!!!!!111"+sl1.getPreSaleDocuments().size());
                sl1.setPreSaleDocuments(null);
                salesLeadFacade.edit(sl1);
                System.out.println("!!!!!!!!!!!!"+siList.size());
                for (SalesInquiry ps : siList) {
                    ps.setInquirer(c);
                    System.out.println("!!!!!!!!!!!!"+ps.getInquirer().getCompany_name());
                    salesInquiryFacade.edit(ps);
                }
                
                c.setPreSaleDocuments(siList);
                edit(c);
           // }
            
        }
    }
    
    @Override
    public boolean convertCustomer(String inquirer_id, String emp_id, String company_name, String contact_person, String contact_no, String email, String remarks,
            String company_add, String fax_no, String cust_type, String country, String city) {

        //SalesLeadFacade salesLeadFacade = new SalesLeadFacade();
        SalesLead sl = salesLeadFacade.find(Long.parseLong(inquirer_id));
        //System.out.println(inquirer_id);
        if (sl.getConvert_status().equals(ConvertStatus.Converted)) {
            return false;
        } else {
            System.out.println("CONVERT CUST: " +emp_id);
            createCustomer(inquirer_id,emp_id, company_name, contact_person, contact_no, email, remarks,
                    company_add, fax_no, cust_type, country, city);
            return true;
        }


        //System.out.println(inquirer_id);
        //SalesLeadFacade salesLeadFacade = new SalesLeadFacade();
        //SalesLead sl = salesLeadFacade.find(Long.parseLong(inquirer_id));
        //sl.setConvert_status(ConvertStatus.Converted);
        //salesLeadFacade.edit(sl);
    }
    
    @Override
    public void updateCustomer(String inquirer_id, String company_name, String contact_person, String contact_no, String email, String remarks,
            String company_add, String fax_no, String cust_type, String country, String city) {
        Customer c = find(Long.parseLong(inquirer_id));
        Date convert_date_time = new Date();
        
        c.setCompany_name(company_name);
        c.setContact_person(contact_person);
        c.setContact_no(contact_no);
        c.setEmail(email);
        c.setRemarks(remarks);
        c.setCompany_add(company_add);
        c.setFax_no(fax_no);
        c.setConvert_date(convert_date_time);
        if (cust_type.equalsIgnoreCase("Wholesale")) {
            c.setCust_type(CustomerType.Wholesale);
        } else if (cust_type.equalsIgnoreCase("Direct Sale")) {
            c.setCust_type(CustomerType.Direct_Sale);
        }
        c.setCountry(country);
        c.setCity(city);
        edit(c);
    }
    @Override
    public Collection findFilteredCustomers(String emp_id) {
        long emp_idL = Long.parseLong(emp_id);
        
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.employee.emp_id=?1");
        query.setParameter(1,emp_idL);
        
        return (Collection) query.getResultList();
    }
    
    @Override
    public void updateCustSE(String [] chk_status,String emp2_id)
    {
        long emp2_idL = Long.parseLong(emp2_id);
        Employee e = employeeFacade.find(emp2_idL);
        
        for(int i = 0;i<chk_status.length;i++)
        {
            long inquirer_id = Long.parseLong(chk_status[i]);
            Customer c = find(inquirer_id);
            c.setEmployee(e);
            edit(c);
        }
    }

}
