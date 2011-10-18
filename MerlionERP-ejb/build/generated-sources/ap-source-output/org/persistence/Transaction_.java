package org.persistence;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.persistence.Transaction.TransType;
import org.persistence.Transaction.TransactionNature;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-10-18T00:11:38")
@StaticMetamodel(Transaction.class)
public class Transaction_ { 

    public static volatile SingularAttribute<Transaction, Long> trans_id;
    public static volatile SingularAttribute<Transaction, Double> amt_involved;
    public static volatile SingularAttribute<Transaction, TransType> trans_type;
    public static volatile SingularAttribute<Transaction, TransactionNature> transaction_nature;
    public static volatile SingularAttribute<Transaction, Date> trans_date_time;

}