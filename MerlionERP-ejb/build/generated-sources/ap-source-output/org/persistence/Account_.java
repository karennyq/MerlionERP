package org.persistence;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Account.AccountStatus;
import org.persistence.Customer;
import org.persistence.Transaction;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-18T00:11:38")
@StaticMetamodel(Account.class)
public class Account_ { 

    public static volatile SingularAttribute<Account, Double> refundable_amt;
    public static volatile CollectionAttribute<Account, Transaction> transactions;
    public static volatile SingularAttribute<Account, Long> account_id;
    public static volatile SingularAttribute<Account, AccountStatus> accountStatus;
    public static volatile SingularAttribute<Account, Double> credit_amt;
    public static volatile SingularAttribute<Account, Double> max_credit_limit;
    public static volatile SingularAttribute<Account, Double> deposit_amt;
    public static volatile SingularAttribute<Account, Customer> customer;

}