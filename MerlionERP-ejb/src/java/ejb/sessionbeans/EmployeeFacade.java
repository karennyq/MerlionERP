/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.EmployeeFacadeLocal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.Employee;
import org.persistence.Employee.ActiveStatus;
import org.persistence.Role;
import util.services.GVLogin;

/**
 *
 * @author Ken
 */
@Stateless
public class EmployeeFacade extends AbstractFacade<Employee> implements EmployeeFacadeLocal {

    @PersistenceContext
    private EntityManager em;

    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeeFacade() {
        super(Employee.class);
    }

    @Override
    public int countFilteredEmployee(int page, int rows, String sort, String order,String nric,String emp_name,String status) {
        String queryStr = "SELECT e FROM Employee e WHERE e.nric LIKE ?1 AND e.emp_name LIKE ?2 AND e.active_status LIKE ?3 ORDER BY e." + sort + " " + order;
        Query query = em.createQuery(queryStr);
        //int maxResult=rows;
        query.setParameter(1,"%"+nric+"%");
        query.setParameter(2,"%"+emp_name+"%");
        query.setParameter(3,"%"+status+"%");
        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredEmployee(int page, int rows, String sort, String order,String nric,String emp_name,String status) {
        String queryStr = "SELECT e FROM Employee e WHERE e.nric LIKE ?1 AND e.emp_name LIKE ?2 AND e.active_status LIKE ?3 ORDER BY e." + sort + " " + order;
        Query query = em.createQuery(queryStr);
        //int maxResult=rows;
        query.setParameter(1,"%"+nric+"%");
        query.setParameter(2,"%"+emp_name+"%");
        query.setParameter(3,"%"+status+"%");
        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<Employee>) query.getResultList();


    }

    @Override
    public void creatEmployee(String emp_name, String email, String nric, ArrayList<Role> roleList) throws NoSuchAlgorithmException {
        String password = nric;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData1[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb1 = new StringBuffer();
        for (int i = 0; i < byteData1.length; i++) {
            sb1.append(Integer.toString((byteData1[i] & 0xff) + 0x100, 16).substring(1));
        }
        String hashedEmpPassword = sb1.toString();
        System.out.println(hashedEmpPassword);
        Employee emp = new Employee();
//        for(Role r:roleList){
//        
//            r.set
//        }
//        
        
        emp.setEmp_name(emp_name);
        emp.setEmail(email);
        emp.setNric(nric);
        emp.setActive_status(ActiveStatus.Active);
        emp.setPassword(hashedEmpPassword);
        emp.setRoles(roleList);
        create(emp);
    }
    
    @Override
    public void updateEmployee(String emp_id,String emp_name, String email, String nric, ArrayList<Role> roleList) throws NoSuchAlgorithmException {
        
        Employee emp = find(Long.parseLong(emp_id));
        
        emp.setEmp_name(emp_name);
        emp.setEmail(email);
        emp.setNric(nric);
        emp.setActive_status(ActiveStatus.Active);
        
        emp.setRoles(roleList);
        
        edit(emp);
    }

    @Override
    public boolean employeeIcExist(String nric) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.nric=?1");
        query.setParameter(1, nric);
        boolean r = query.getResultList().isEmpty();
        return (!r);
    }

    @Override
    public boolean employeeEmailExist(String email) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.email=?1");
        query.setParameter(1, email);
        boolean r = query.getResultList().isEmpty();
        return (!r);
    }

    @Override
    public Employee checkExistence(String username) {
        try {
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.nric='" + username + "'");
            Employee emp = (Employee) query.getSingleResult();
            em.refresh(emp);
            return emp; //Exist
        } catch (NoResultException nre) {
            return null; //Not Exist
        }
    }
    
    @Override
    public boolean updateEmployeeIcExist(String nric,String emp_id) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.nric=?1 AND e.emp_id<>?2");
        query.setParameter(1, nric);
        query.setParameter(2,Long.parseLong(emp_id));
        boolean r = query.getResultList().isEmpty();
        return (!r);
    }

    @Override
    public boolean updateEmployeeEmailExist(String email,String emp_id) {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.email=?1 AND e.emp_id<>?2");
        query.setParameter(1, email);
        query.setParameter(2,Long.parseLong(emp_id));
        boolean r = query.getResultList().isEmpty();
        return (!r);
    }

    @Override
    public Employee updateCheckExistence(String username,String emp_id) {
        try {
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.nric='" + username + "' AND e.emp_id<>?2");
            query.setParameter(2,Long.parseLong(emp_id));
            Employee emp = (Employee) query.getSingleResult();
            return emp; //Exist
        } catch (NoResultException nre) {
            return null; //Not Exist
        }
    }

    @Override
    public boolean checkPassword(Employee emp, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        String hashedPassword = sb.toString();
        if (emp.getPassword().equals(hashedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateNoOfAttempts(Employee emp, boolean loginStatus) {
        Calendar cal = Calendar.getInstance();
        Integer noOfAttempts = 0;
        if (loginStatus == true) { //login sucessfully
            noOfAttempts = 0;
            emp.setNo_of_attempts(noOfAttempts);
            emp.setLast_login_date_time(new Date());
        } else { //login failed

            if (emp.getLast_login_date_time() != null) {
                long lastAttempt = emp.getLast_login_date_time().getTime();
                System.out.println("------lastA------" + lastAttempt);
                long now = new Date().getTime();
                System.out.println("----now------" + now);
                System.out.print("----------------- " + TimeUnit.MILLISECONDS.toMinutes(now - lastAttempt));

                if (TimeUnit.MILLISECONDS.toMinutes(now - lastAttempt) > GVLogin.noOfFailedAttemptsTime) {
                    noOfAttempts = 1;
                } else {
                    noOfAttempts = (emp.getNo_of_attempts() == null) ? 0 : emp.getNo_of_attempts() + 1;
                    System.out.println(emp.getNo_of_attempts() + "------------ no of attempts in db------");
                }
            }
            emp.setNo_of_attempts(noOfAttempts);
            if (noOfAttempts >= GVLogin.noOfFailedAttemptsLock) {
                emp.setActive_status(ActiveStatus.Inactive);
                emp.setNo_of_attempts(0);
            }

            emp.setLast_login_date_time(new Date());
        }
        edit(emp);
    }
    
    @Override
    public void updateActiveStatus(String emp_id, String status)
    {
        long emp_idL = Long.parseLong(emp_id);
        Employee emp = find(emp_idL);
        
        if(status.equalsIgnoreCase("Active"))
         {
             emp.setActive_status(Employee.ActiveStatus.Active);
         }
         else if(status.equalsIgnoreCase("Inactive"))
         {
             emp.setActive_status(Employee.ActiveStatus.Inactive);
         }
        
        edit(emp);
    }
    
    @Override
    public void resetPassword(String emp_id) throws Exception
    {
        long emp_idL = Long.parseLong(emp_id);
        Employee emp = find(emp_idL);
        String nric = emp.getNric();
        
        String password = nric;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData1[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb1 = new StringBuffer();
        for (int i = 0; i < byteData1.length; i++) {
            sb1.append(Integer.toString((byteData1[i] & 0xff) + 0x100, 16).substring(1));
        }
        String hashedEmpPassword = sb1.toString();
        
        emp.setPassword(hashedEmpPassword);
        
        edit(emp);
        
        
    }
    
    @Override
    public void changePassword(String emp_id,String new_password) throws Exception
    {
        System.out.print(emp_id+"   "+new_password);
        long emp_idL = Long.parseLong(emp_id);
        Employee emp = find(emp_idL);
        
        String password = new_password;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData1[] = md.digest();
        //convert the byte to hex format method 1
        StringBuffer sb1 = new StringBuffer();
        for (int i = 0; i < byteData1.length; i++) {
            sb1.append(Integer.toString((byteData1[i] & 0xff) + 0x100, 16).substring(1));
        }
        String hashedEmpPassword = sb1.toString();
        
        emp.setPassword(hashedEmpPassword);
        
        edit(emp);
        
        
    }
 @Override
    public Collection salesExecutiveEmployees() {
        
        Query query = em.createQuery("SELECT e FROM Employee e");
       // Employee e = (Employee) query.getResultList();
        
        ArrayList<Employee> empList = new ArrayList<Employee>();
         
        for(Object o: query.getResultList()){
             System.out.println("EMP");

             Employee e = (Employee)o;
             for(Role r: e.getRoles()){
                   System.out.println("ROLES");
                 if(r.getRole_name().equalsIgnoreCase("Sales Executive")){
                    empList.add(e);
                }
             }
        }
        System.out.println("SALES EXE: " +empList.size());
        return (Collection<Employee>) empList;
    }
    
}
