/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.sessionbeans;

import ejb.sessionbeans.interfaces.AccountFacadeLocal;
import ejb.sessionbeans.interfaces.CustomerFacadeLocal;
import ejb.sessionbeans.interfaces.PurchaseOrderFacadeLocal;
import ejb.sessionbeans.interfaces.SalesOrderFacadeLocal;
import ejb.sessionbeans.interfaces.TransactionFacadeLocal;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.persistence.Account;
import org.persistence.Customer;
import org.persistence.PurchaseOrder;
import org.persistence.SalesOrder;

/**
 *
 * @author alyssia
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> implements AccountFacadeLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext(unitName = "MerlionERP-ejbPU")
    private EntityManager em;
    @EJB
    TransactionFacadeLocal transactionFacade;
    @EJB
    CustomerFacadeLocal customerFacade;
    @EJB
    SalesOrderFacadeLocal salesOrderFacade;
    @EJB
    PurchaseOrderFacadeLocal purchaseOrderFacade;
    @EJB
    AccountFacadeLocal accountFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    @Override
    public String increaseCredit(String accountID, String amount) {

        String content = "";
        Account account = em.find(Account.class, Long.parseLong(accountID));

        if (account.getAccountStatus() == Account.AccountStatus.New) {
            account.setAccountStatus(Account.AccountStatus.Existing);
        }

        account.setMax_credit_limit(account.getMax_credit_limit() + Double.parseDouble(amount));
        content = "Maximumum Credit Limit of $" + amount + " increased successfully.";
        transactionFacade.createTransaction(accountID, amount, "Credit", "Credit");
        edit(account);
        updateSoStatusbyMaxCredit(account.getCustomer());

        return content;
    }

    @Override
    public String decreaseCredit(String accountID, String amount) {

        String content = "";
        Account account = em.find(Account.class, Long.parseLong(accountID));

        if ((account.getMax_credit_limit() - Double.parseDouble(amount)) < 0 && (account.getAccountStatus() == Account.AccountStatus.Existing)) {
            content = "Not allowed. Amount is more than current maximum credit limit.";
        } else if ((account.getMax_credit_limit() - Double.parseDouble(amount)) >= 0 && (account.getAccountStatus() == Account.AccountStatus.Existing)) {
            account.setMax_credit_limit(account.getMax_credit_limit() - Double.parseDouble(amount));
            transactionFacade.createTransaction(accountID, amount, "Credit", "Debit");
            content = "Maximumum Credit Limit of $" + amount + " decreased successfully.";
        } else {
            System.out.println("Maximum credit limit have not been assessed!");
            content = "Not allowed. Maximum credit limit have not been assessed.";
        }
        edit(account);
        updateSoStatusbyMaxCredit(account.getCustomer());

        return content;
    }

    @Override
    public String addDeposit(String accountID, String amount, String soID) {

        String content = "";
        Account account = em.find(Account.class, Long.parseLong(accountID));
        account.setDeposit_amt(account.getDeposit_amt() + Double.parseDouble(amount));
        content = "Deposit of $" + amount + " added successfully.";
        transactionFacade.createTransaction(accountID, amount, "Deposit", "Credit");
        edit(account);
        updateSoStatusbyDeposit(account.getCustomer(), soID);
        return content;
    }

    @Override
    public String deductRefund(String accountID, String amount) {

        String content = "";
        Account account = em.find(Account.class, Long.parseLong(accountID));

        if ((account.getRefundable_amt() - Double.parseDouble(amount)) < 0) {
            content = "Not allowed. Amount is more than current refund amount.";
        } else {
            account.setRefundable_amt(account.getRefundable_amt() - Double.parseDouble(amount));
            transactionFacade.createTransaction(accountID, amount, "Refund", "Debit");
            content = "Refund of $" + amount + " deducted successfully.";
        }

        edit(account);
        return content;
    }

    @Override
    public int countFilteredAccount(int page, int rows, String sort, String order, String custId, String accountStatus) {
        String queryStr = "SELECT a FROM Account a, Customer c WHERE a.account_id = c.account.account_id AND c.inquirer_id LIKE ?1 AND a.accountStatus LIKE ?2 ORDER BY c." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        query.setParameter(1, "%" + custId + "%");
        query.setParameter(2, "%" + accountStatus + "%");
        return query.getResultList().size();
    }

    @Override
    public Collection findFilteredAccount(int page, int rows, String sort, String order, String custId, String accountStatus) {

        String queryStr = "SELECT a FROM Account a, Customer c WHERE a.account_id = c.account.account_id AND c.inquirer_id LIKE ?1 AND a.accountStatus LIKE ?2 ORDER BY c." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        query.setParameter(1, "%" + custId + "%");
        query.setParameter(2, "%" + accountStatus + "%");
        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<Account>) query.getResultList();
    }

    @Override
    public Collection viewAll(int page, int rows, String sort, String order) {

        String queryStr = "SELECT a FROM Account a, Customer c WHERE a.account_id = c.account.account_id AND a.credit_amt >= a.max_credit_limit AND a.credit_amt <> '0' AND a.max_credit_limit <> '0' AND a.accountStatus = ?1 ORDER BY c." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        //for enum
        query.setParameter(1, Account.AccountStatus.valueOf("Existing"));

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return (Collection<Account>) query.getResultList();
    }

    @Override
    public int countViewAll(int page, int rows, String sort, String order) {
        String queryStr = "SELECT a FROM Account a, Customer c WHERE a.account_id = c.account.account_id AND a.credit_amt >= a.max_credit_limit AND a.credit_amt <> '0' AND a.max_credit_limit <> '0'  AND a.accountStatus = ?1 ORDER BY c." + sort + " " + order;
        Query query = em.createQuery(queryStr);

        //for enum
        query.setParameter(1, Account.AccountStatus.valueOf("Existing"));

        int firstResult = (page - 1) * rows;
        query.setMaxResults(rows);
        query.setFirstResult(firstResult);
        return query.getResultList().size();
    }

    @Override
    public boolean requestForDeposit(Long inquirer_id, Double totalAmt) {
        Customer c = customerFacade.find(inquirer_id);
        if (!c.getAccount().getAccountStatus().equals(Account.AccountStatus.Blacklist)) {
            if ((c.getAccount().getMax_credit_limit() - c.getAccount().getCredit_amt()) >= totalAmt) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    @Override
    public Double amtToDeposit(Long inquirer_id, Double totalAmt) {
        Customer c = customerFacade.find(inquirer_id);
        if (!c.getAccount().getAccountStatus().equals(Account.AccountStatus.Blacklist)) {
            if ((c.getAccount().getMax_credit_limit() - c.getAccount().getCredit_amt()) >= totalAmt) {
                return 0.00;
            } else {
                return (totalAmt - ((c.getAccount().getMax_credit_limit() - c.getAccount().getCredit_amt())));
            }
        } else {
            return (totalAmt - ((c.getAccount().getMax_credit_limit() - c.getAccount().getCredit_amt())));
        }
    }

    @Override
    public void updateSoStatusbyMaxCredit(Customer c) {
                        em.refresh(c);
        Customer cust = customerFacade.find(c.getInquirer_id());
        for (PurchaseOrder po : cust.getPurchaseOrders()) {
            if (po.getSalesOrder() != null) {
                if (!po.getSalesOrder().getStatus().equals(SalesOrder.Status.Cancelled)) {
                    if (po.getSalesOrder().getCreditCheck().equals(SalesOrder.CreditCheck.Pending)) {

                        if (po.getCustomer().getAccount().getAccountStatus().equals(Account.AccountStatus.New)) {
                            po.getSalesOrder().setCreditCheck(SalesOrder.CreditCheck.Pending);
                            salesOrderFacade.edit(po.getSalesOrder());
                            purchaseOrderFacade.edit(po);
                        } else if (po.getCustomer().getAccount().getAccountStatus().equals(Account.AccountStatus.Blacklist)) {
                            po.getSalesOrder().setCreditCheck(SalesOrder.CreditCheck.Not_Approved);
                            salesOrderFacade.edit(po.getSalesOrder());
                        } else {
                            Double amtToDeposit = amtToDeposit(po.getCustomer().getInquirer_id(), po.getDiscounted_total());
                            if (requestForDeposit(po.getCustomer().getInquirer_id(), po.getDiscounted_total())) {
                                po.getSalesOrder().setDeposit_requested(amtToDeposit);
                                po.getSalesOrder().setCreditCheck(SalesOrder.CreditCheck.Pending);
                                Double maxLimit = po.getCustomer().getAccount().getMax_credit_limit();
                                Double currentCredit = po.getCustomer().getAccount().getCredit_amt();
                                po.getCustomer().getAccount().setCredit_amt(maxLimit);
                                accountFacade.edit(po.getCustomer().getAccount());
                                transactionFacade.createTransaction(po.getCustomer().getAccount().getAccount_id() + "", maxLimit-currentCredit + "", "Credit", "Credit");
                                salesOrderFacade.edit(po.getSalesOrder());
                                em.flush();

                            } else {
                                po.getSalesOrder().setDeposit_requested(amtToDeposit);
                                po.getSalesOrder().setCreditCheck(SalesOrder.CreditCheck.Approved);
                                salesOrderFacade.edit(po.getSalesOrder());


                                Double creditAmt = po.getCustomer().getAccount().getCredit_amt();
                                po.getCustomer().getAccount().setCredit_amt(creditAmt + po.getDiscounted_total() - po.getSalesOrder().getDeposit_requested());
                                transactionFacade.createTransaction(po.getCustomer().getAccount().getAccount_id() + "", (creditAmt + po.getDiscounted_total() - po.getSalesOrder().getDeposit_requested()) + "", "Credit", "Credit");

                                accountFacade.edit(po.getCustomer().getAccount());
                            }
                            //purchaseOrderFacade.edit(po);
                        }
                    }
                }
                if (po.getSalesOrder().getAtpCheck().equals(SalesOrder.ATPCheck.Sufficient)
                        && po.getSalesOrder().getCreditCheck().equals(SalesOrder.CreditCheck.Approved)) {
                    po.getSalesOrder().setStatus(SalesOrder.Status.Confirmed);
                    po.getSalesOrder().setDate_confirmed(new Date());
                }
                salesOrderFacade.edit(po.getSalesOrder());
                em.flush();
            }


        }

    }

    @Override
    public void updateSoStatusbyDeposit(Customer c, String so_id) {
        SalesOrder so = salesOrderFacade.find(Long.parseLong(so_id));
        if ((!c.getAccount().getAccountStatus().equals(Account.AccountStatus.New)) || (!c.getSales_lead_status().equals(Customer.SalesLeadStatus.Inactive))) {
            so.setCreditCheck(SalesOrder.CreditCheck.Approved);
            salesOrderFacade.edit(so);

            if (so.getAtpCheck().equals(SalesOrder.ATPCheck.Sufficient)
                    && so.getCreditCheck().equals(SalesOrder.CreditCheck.Approved)) {
                so.setStatus(SalesOrder.Status.Confirmed);
                so.setDate_confirmed(new Date());
            }
            salesOrderFacade.edit(so);
                em.flush();


        }

    }
}
