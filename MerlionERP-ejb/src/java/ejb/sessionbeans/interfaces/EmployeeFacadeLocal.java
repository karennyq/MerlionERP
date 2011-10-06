/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.persistence.Employee;

/**
 *
 * @author Ken
 */
@Local
public interface EmployeeFacadeLocal {

    void create(Employee employee);

    void edit(Employee employee);

    void remove(Employee employee);

    Employee find(Object id);

    List<Employee> findAll();

    List<Employee> findRange(int[] range);

    int count();

    public boolean employeeIcExist(String nric);

    public boolean employeeEmailExist(java.lang.String email);

    public void updateNoOfAttempts(org.persistence.Employee emp, boolean loginStatus);

    public boolean checkPassword(org.persistence.Employee emp, java.lang.String password) throws java.security.NoSuchAlgorithmException;

    public org.persistence.Employee checkExistence(java.lang.String username);

//    public void creatEmployee(java.lang.String emp_name, java.lang.String email, java.lang.String nric) throws java.security.NoSuchAlgorithmException;
    //  public java.util.Collection findFilteredEmployee(int page, int rows, java.lang.String sort, java.lang.String order);
    public java.util.Collection findFilteredEmployee(int page, int rows, java.lang.String sort, java.lang.String order, java.lang.String nric, java.lang.String emp_name, java.lang.String status);

    public int countFilteredEmployee(int page, int rows, java.lang.String sort, java.lang.String order, java.lang.String nric, java.lang.String emp_name, java.lang.String status);

    public void creatEmployee(java.lang.String emp_name, java.lang.String email, java.lang.String nric, java.util.ArrayList<org.persistence.Role> roleList) throws java.security.NoSuchAlgorithmException;

    public void updateEmployee(java.lang.String emp_id, java.lang.String emp_name, java.lang.String email, java.lang.String nric, java.util.ArrayList<org.persistence.Role> roleList) throws java.security.NoSuchAlgorithmException;

    public org.persistence.Employee updateCheckExistence(java.lang.String username, java.lang.String emp_id);

    public boolean updateEmployeeEmailExist(java.lang.String email, java.lang.String emp_id);

    public boolean updateEmployeeIcExist(java.lang.String nric, java.lang.String emp_id);

    public void updateActiveStatus(String emp_id, String status);

    public void resetPassword(String emp_id) throws Exception;

    public void changePassword(String emp_id, String new_password) throws Exception;

    public java.util.Collection salesExecutiveEmployees();
}
