package org.persistence;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.OneToOne;

@Entity
@Table(name = "ACCOUNT")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum AccountStatus {

        New, Existing,Blacklist
    }
    @Id
    @GeneratedValue
    private Long account_id;
    @Basic
    private Double max_credit_limit;
    @Basic
    private Double credit_amt;
    @Basic
    private Double deposit_amt;
    @Basic
    private Double refundable_amt;
    @Basic
    @Enumerated
    private AccountStatus accountStatus;

    @OneToMany
    //@JoinColumn(name = "ACCOUNT_account_id", referencedColumnName = "account_id")
    private Collection<Transaction> transactions;
    @OneToOne(mappedBy = "account")
    private Customer customer;

    public void setAccount_id(Long param) {
        this.account_id = param;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setMax_credit_limit(Double param) {
        this.max_credit_limit = param;
    }

    public Double getMax_credit_limit() {
        return max_credit_limit;
    }

    public void setCredit_amt(Double param) {
        this.credit_amt = param;
    }

    public Double getCredit_amt() {
        return credit_amt;
    }

    public void setDeposit_amt(Double param) {
        this.deposit_amt = param;
    }

    public Double getDeposit_amt() {
        return deposit_amt;
    }

    public void setRefundable_amt(Double param) {
        this.refundable_amt = param;
    }

    public Double getRefundable_amt() {
        return refundable_amt;
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<Transaction> param) {
        this.transactions = param;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer param) {
        this.customer = param;
    }
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
    
}