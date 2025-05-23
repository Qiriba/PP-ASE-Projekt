package com.banking.domain.model;

import com.banking.domain.model.valueobjects.PIN;
import com.banking.domain.model.valueobjects.Password;
import com.banking.domain.model.exceptions.AccountLockedException;

import java.util.*;

import jakarta.persistence.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String username;

    @Embedded
    private Password password;

    @Embedded
    private PIN pin;

    private boolean locked;
    private int failedLoginAttempts;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();


    public Customer() {
    }

    public Customer(String username, Password password, PIN pin) {
        this.username = username;
        this.password = password;
        this.pin = pin;
        this.locked = false;
        this.failedLoginAttempts = 0;
    }

    public boolean login(String inputPassword) {
        if (locked) {
            throw new AccountLockedException("Konto gesperrt");
        }

        if (this.password.matches(inputPassword)) {
            failedLoginAttempts = 0; // reset failed login attempts
            return true;
        } else {
            failedLoginAttempts++;
            if (failedLoginAttempts >= 3) {
                lockCustomer();
            }
            return false;
        }
    }

    public void lockCustomer() {
        this.locked = true;
    }

    public void unlockCustomer() {
        this.locked = false;
        this.failedLoginAttempts = 0;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public Account getAccount(UUID accountId) {
        return accounts.stream()
                .filter(a -> a.getId().equals(accountId))
                .findFirst()
                .orElse(null);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Password getPassword() {return password;}
    public PIN getPin() {
        return pin;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void setPin(PIN pin) {
        this.pin = pin;
    }

}