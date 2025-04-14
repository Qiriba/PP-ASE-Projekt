package com.banking.domain.model;

import com.banking.domain.model.exceptions.AccountLockedException;

public class BankEmployee {

    public void unlockCustomerAccount(Customer customer) {
        if (!customer.isLocked()) {
            throw new AccountLockedException("Konto ist nicht gesperrt.");
        }
        customer.unlockCustomer();
    }
}
