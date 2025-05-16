package com.banking.domain.model;

import com.banking.domain.model.exceptions.InsufficientFundsException;
import com.banking.domain.model.valueobjects.Money;
import com.banking.domain.model.valueobjects.AccountNumber;
import com.banking.domain.model.exceptions.AccountLockedException;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Embedded
    private AccountNumber accountNumber;

    @Embedded
    private Money balance;

    private boolean locked;

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transactions> transactionsHistoryAsSource = new ArrayList<>();

    @OneToMany(mappedBy = "targetAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transactions> transactionsHistoryAsTarget = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    protected Account() {
    }

    public Account(UUID id, AccountNumber accountNumber, Money initialBalance, Customer customer) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.locked = false;
        this.customer = customer;
    }


    public void deposit(Money amount) {
        ensureNotLocked();
        balance = balance.add(amount);
    }

    public void withdraw(Money amount) {
        ensureNotLocked();
        if (!balance.isGreaterOrEqual(amount)) {
            throw new InsufficientFundsException("Nicht genug Guthaben");
        }
        balance = balance.subtract(amount);
    }

    public void transferTo(Account target, Money amount) {
        ensureNotLocked();
        if (!balance.isGreaterOrEqual(amount)) {
            throw new InsufficientFundsException("Nicht genug Guthaben für Überweisung");
        }
        this.balance = this.balance.subtract(amount);
        target.receiveTransfer(amount);
    }

    public void receiveTransfer(Money amount) {
        this.balance = this.balance.add(amount);
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }


    private void ensureNotLocked() {
        if (locked) {
            throw new AccountLockedException("Konto ist gesperrt");
        }
    }

    public UUID getId() {
        return id;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public Money getBalance() {
        return balance;
    }
    public Currency getCurrency() { return balance.getCurrency();}
    @Transient
    public List<Transactions> getTransactionHistory() {
        List<Transactions> all = new ArrayList<>();
        all.addAll(transactionsHistoryAsSource);
        all.addAll(transactionsHistoryAsTarget);
        return all;
    }

    public boolean isLocked() {
        return locked;
    }

    public Customer getCustomer() {
        return customer;
    }
}
