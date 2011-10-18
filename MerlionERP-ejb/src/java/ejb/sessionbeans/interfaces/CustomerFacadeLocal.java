/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import org.persistence.Customer;

/**
 *
 * @author Ken
 */
@Local
public interface CustomerFacadeLocal {

    void create(Customer customer);

    void edit(Customer customer);

    void remove(Customer customer);

    Customer find(Object id);

    List<Customer> findAll();

    List<Customer> findRange(int[] range);

    int count();

    public Collection findFilteredCustomers(int page, int rows, String sort, String order, String inquirer_id, String company_name);

    public int countFilteredCustomers(int page, int rows, String sort, String order, String inquirer_id, String company_name);

    public void createCustomer(String inquirer_id, String emp_id, String company_name, String contact_person, String contact_no, String email, String remarks,
            String company_add, String fax_no, String cust_type, String country, String city);

    public boolean convertCustomer(String inquirer_id, String emp_id, String company_name, String contact_person, String contact_no, String email, String remarks,
            String company_add, String fax_no, String cust_type, String country, String city);

    public void updateCustomer(String inquirer_id, String company_name, String contact_person, String contact_no, String email, String remarks,
            String company_add, String fax_no, String cust_type, String country, String city);

    public Collection findFilteredCustomers(String emp_id);

    public void updateCustSE(String[] chk_status, String emp2_id);
}
