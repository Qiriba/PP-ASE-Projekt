package com.banking.domain.model.factory;

import com.banking.domain.model.Account;
import com.banking.domain.model.Transactions;
import com.banking.domain.model.enums.TransactionType;
import com.banking.domain.model.valueobjects.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionFactory {

    public Transactions createDeposit(Account account, Money amount) {
        return new Transactions(
                UUID.randomUUID(),
                LocalDateTime.now(),
                amount,
                TransactionType.DEPOSIT,
                account,
                null
        );
    }

    public Transactions createWithdrawal(Account account, Money amount) {
        return new Transactions(
                UUID.randomUUID(),
                LocalDateTime.now(),
                amount,
                TransactionType.WITHDRAWAL,
                account,
                null
        );
    }

    public Transactions createTransfer(Account source, Account target, Money amount) {
        return new Transactions(
                UUID.randomUUID(),
                LocalDateTime.now(),
                amount,
                TransactionType.TRANSFER,
                source,
                target
        );
    }
}
