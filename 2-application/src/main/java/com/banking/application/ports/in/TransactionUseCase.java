package com.banking.application.ports.in;

import com.banking.domain.model.Account;
import com.banking.domain.model.valueobjects.Money;

public interface TransactionUseCase {
    void deposit(Account account, Money amount);
    void withdraw(Account account, Money amount);
    void transfer(Account source, Account target, Money amount);

}
