/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.TransactionFacadeLocal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.persistence.Account;
import org.persistence.Transaction;

/**
 *
 * @author alyssia
 */
@Stateless
public class TransactionFacade extends AbstractFacade<Transaction>implements TransactionFacadeLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TransactionFacade() {
        super(Transaction.class);
    }
    
    @Override
    public int countFilteredTransaction(int page, int rows, String sort, String order,String acctID,String trxType,String trxNature) {
        Account a = em.find(Account.class, Long.valueOf(acctID));    
        ArrayList<Transaction> transList = new ArrayList<Transaction>();
         int trx_Type = 0;
         int trx_Nature =0;
                 
         if (!trxType.isEmpty()) {
             trx_Type = Integer.parseInt(trxType) - 1;
             if (trx_Type == 0) {
                 for (Transaction t: a.getTransactions()) {
                     if (t.getTrans_type() == Transaction.TransType.Credit) {
                        transList.add(t);
                     }
                 }
             } else if (trx_Type == 1) {
                 for (Transaction t: a.getTransactions()) {
                     if (t.getTrans_type()== Transaction.TransType.Deposit) {
                         transList.add(t);
                     }
                 }
             } else if (trx_Type == 2) {
                 for (Transaction t: a.getTransactions()) {
                     if (t.getTrans_type()== Transaction.TransType.Refund) {
                        transList.add(t);
                     }
                 }
             }
         }else if (!trxNature.isEmpty()) {
             trx_Nature = Integer.parseInt(trxNature) - 1;
             if (trx_Nature == 0) {
                 for (Transaction t: a.getTransactions()) {
                     if (t.getTransaction_nature() == Transaction.TransactionNature.Debit) {
                         transList.add(t);
                     }
                 }
             } else if (trx_Nature == 1) {
                 for (Transaction t: a.getTransactions()) {
                     if (t.getTransaction_nature() == Transaction.TransactionNature.Credit) {
                         transList.add(t);
                     }
                 }
             }
         }
         
         if(trxType.isEmpty() && trxNature.isEmpty()){
             transList = new ArrayList<Transaction>(a.getTransactions());
         }
         return transList.size();
   }

    @Override
    public Collection findFilteredTransaction(int page, int rows, String sort, String order,String acctID,String trxType,String trxNature) {
       
         Account a = em.find(Account.class, Long.valueOf(acctID));
         ArrayList<Transaction> transList = new ArrayList<Transaction>();
         int trx_Type = 0;
         int trx_Nature =0;
         
         if (!trxType.isEmpty()) {
             trx_Type = Integer.parseInt(trxType) - 1;
             if (trx_Type == 0) {
                 for (Transaction t: a.getTransactions()) {
                     if (t.getTrans_type() == Transaction.TransType.Credit) {
                         transList.add(t);
                     }
                 }
             } else if (trx_Type == 1) {
                 for (Transaction t: a.getTransactions()) {
                     if (t.getTrans_type()== Transaction.TransType.Deposit) {
                         transList.add(t);
                     }
                 }
             } else if (trx_Type == 2) {
                 for (Transaction t: a.getTransactions()) {
                     if (t.getTrans_type()== Transaction.TransType.Refund) {
                        transList.add(t);
                     }
                 }
             }
         }else if(!trxNature.isEmpty()) {
             trx_Nature = Integer.parseInt(trxNature) - 1;
             if (trx_Nature == 0) {
                 for (Transaction t: a.getTransactions()) {
                     if (t.getTransaction_nature() == Transaction.TransactionNature.Debit) {
                        transList.add(t);
                     }
                 }
             } else if (trx_Nature == 1) {
                 for (Transaction t: a.getTransactions()) {
                     if (t.getTransaction_nature() == Transaction.TransactionNature.Credit) {
                        transList.add(t);
                     }
                 }
             }
         }
         
         if(trxType.isEmpty() && trxNature.isEmpty()){
             transList = new ArrayList<Transaction>(a.getTransactions());
         }
         return transList;
     }
    
    @Override
    public void createTransaction(String acctID,String amt,String trxType,String trxNature) {
        Transaction t = new Transaction();
        Account a = em.find(Account.class,Long.valueOf(acctID));
       
        t.setAmt_involved(Double.parseDouble(amt));
        t.setTrans_date_time(new Date());
        
        if(trxType.equals("Credit")){
            t.setTrans_type(Transaction.TransType.Credit);
        }else if(trxType.equals("Deposit")){
            t.setTrans_type(Transaction.TransType.Deposit);
        }else if(trxType.equals("Refund")){
            t.setTrans_type(Transaction.TransType.Refund);
        }
        
        if(trxNature.equals("Credit")){
            t.setTransaction_nature(Transaction.TransactionNature.Credit);
        }else if(trxNature.equals("Debit")){
            t.setTransaction_nature(Transaction.TransactionNature.Debit);
        }
        create(t);
        Collection<Transaction> trans = a.getTransactions();
        trans.add(t);
        a.setTransactions(trans);
   }
}
