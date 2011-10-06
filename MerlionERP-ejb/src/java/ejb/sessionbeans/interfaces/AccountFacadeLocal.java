/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import java.util.List;
import javax.ejb.Local;
import org.persistence.Account;
import org.persistence.Customer;

/**
 *
 * @author alyssia
 */
@Local
public interface AccountFacadeLocal {
    
     Account find(Object id);
     
     List<Account> findAll();
     
         void edit(Account account);


     List<Account> findRange(int[] range);
     
     void create(Account account);
     
     String increaseCredit(String accountID, String amount);
     
     String decreaseCredit(String accountID, String amount);
     
     String addDeposit(String accountID, String amount, String soID);
     
     String deductRefund(String accountID, String amount);
     
     public java.util.Collection findFilteredAccount(int page, int rows, java.lang.String sort, java.lang.String order, java.lang.String custID, String accountStatus);

     public int countFilteredAccount(int page, int rows, java.lang.String sort, java.lang.String order,java.lang.String custID, String accountStatus);

     public java.util.Collection viewAll(int page, int rows, String sort, String order);
     
     public int countViewAll(int page, int rows, String sort, String order); 
          
     int count();
     
     boolean requestForDeposit(Long inquirer_id, Double totalAmt);
     
     public Double amtToDeposit(Long inquirer_id, Double totalAmt);
     
     void updateSoStatusbyMaxCredit(Customer c);
     
     void updateSoStatusbyDeposit(Customer c, String so_id);
}
