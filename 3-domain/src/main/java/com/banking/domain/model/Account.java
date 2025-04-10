package com.banking.domain.model;

import com.banking.domain.model.valueobjects.Money;
import com.banking.domain.model.valueobjects.AccountNumber;
import com.banking.domain.model.Transaction;
import com.banking.domain.model.enums.TransactionType;
import com.banking.domain.model.exceptions.AccountLockedException;
import com.banking.domain.model.exceptions.InsufficientFundsException;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Embedded  // AccountNumber als Value Object
    private AccountNumber accountNumber;

    @Embedded  // Money als Value Object für das Guthaben
    private Money balance;

    private boolean locked;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactionHistory = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    public Account() {
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
        addTransaction(amount, TransactionType.DEPOSIT, null);
    }

    public void withdraw(Money amount) {
        ensureNotLocked();
        if (!balance.isGreaterOrEqual(amount)) {
            throw new InsufficientFundsException("Nicht genug Guthaben");
        }
        balance = balance.subtract(amount);
        addTransaction(amount, TransactionType.WITHDRAWAL, null);
    }

    public void transferTo(Account target, Money amount) {
        ensureNotLocked();
        if (!balance.isGreaterOrEqual(amount)) {
            throw new InsufficientFundsException("Nicht genug Guthaben für Überweisung");
        }
        this.balance = this.balance.subtract(amount);
        target.receiveTransfer(this, amount);

        // Transaktionen in beiden Accounts speichern
        this.addTransaction(amount, TransactionType.TRANSFER_OUT, target);
        target.addTransaction(amount, TransactionType.TRANSFER_IN, this);
    }

    public void receiveTransfer(Account sender, Money amount) {
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

    private void addTransaction(Money amount, TransactionType type, Account otherAccountId) {
        transactionHistory.add(new Transaction(
                UUID.randomUUID(),
                LocalDateTime.now(),
                amount,
                type,
                this,
                otherAccountId
        ));
    }

    // === Getter ===

    public UUID getId() {
        return id;
    }

    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public Money getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory); // defensive copy
    }

    public boolean isLocked() {
        return locked;
    }

    public Customer getCustomer() {
        return customer;
    }
}
