package com.banking.domain.model;

import com.banking.domain.model.valueobjects.PIN;
import com.banking.domain.model.valueobjects.Password;
import com.banking.domain.model.exceptions.AccountLockedException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;  // UUID als Primärschlüssel

    private String username;

    @Embedded  // Wertobjekt für das Password
    private Password password;

    @Embedded  // Wertobjekt für die PIN
    private PIN pin;

    private boolean locked;
    private int failedLoginAttempts;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Map<UUID, Account> accounts = new HashMap<>();

    // Standardkonstruktor für JPA
    public Customer() {
    }

    public Customer(UUID id, String username, Password password, PIN pin) {
        this.id = id;
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
                lockAccount();
            }
            return false;
        }
    }

    public void lockAccount() {
        this.locked = true;
    }

    public void unlockAccount() {
        this.locked = false;
        this.failedLoginAttempts = 0;
    }

    public void addAccount(Account account) {
        accounts.put(account.getId(), account);
    }

    public Account getAccount(UUID accountId) {
        return accounts.get(accountId);
    }

    // Getter-Methoden
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

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