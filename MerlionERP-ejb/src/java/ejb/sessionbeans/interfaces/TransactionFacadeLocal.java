/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans.interfaces;

import javax.ejb.Local;
import org.persistence.Transaction;

/**
 *
 * @author alyssia
 */
@Local
public interface TransactionFacadeLocal {
    
     Transaction find(Object id);
     
     public java.util.Collection findFilteredTransaction(int page, int rows, java.lang.String sort, java.lang.String order,java.lang.String acctID,java.lang.String trxType,java.lang.String trxNature);

     public int countFilteredTransaction(int page, int rows, java.lang.String sort, java.lang.String order,java.lang.String acctID,java.lang.String trxType,java.lang.String trxNature);

     void createTransaction(String acctID,String amt,String trxType,String trxNature);
     
     int count();
}
