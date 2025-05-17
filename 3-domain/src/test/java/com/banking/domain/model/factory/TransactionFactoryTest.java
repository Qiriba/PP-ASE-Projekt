package com.banking.domain.model.factory;

import com.banking.domain.model.Account;
import com.banking.domain.model.Customer;
import com.banking.domain.model.Transactions;
import com.banking.domain.model.enums.TransactionType;

import com.banking.domain.model.valueobjects.AccountNumber;
import com.banking.domain.model.valueobjects.Money;
import com.banking.domain.model.valueobjects.Password;
import com.banking.domain.model.valueobjects.PIN;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

class TransactionFactoryTest {

    private TransactionFactory factory;
    private Account dummyAccount;
    private Account dummyTargetAccount;
    private Money amount;

    @BeforeEach
    void setUp() {
        factory = new TransactionFactory();

        // Dummy Daten erstellen
        AccountNumber accountNumber1 = new AccountNumber("1234567890");
        AccountNumber accountNumber2 = new AccountNumber("0987654321");

        Money initialBalance = new Money(BigDecimal.valueOf(1000), Currency.getInstance("EUR"));

        Customer dummyCustomer = new Customer("dummyUser", new Password("password123"), new PIN("1234"));

        dummyAccount = new Account(accountNumber1, initialBalance, dummyCustomer);
        dummyTargetAccount = new Account(accountNumber2, initialBalance, dummyCustomer);

        amount = new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR"));
    }

    @Test
    void createDeposit_ShouldReturnDepositTransaction() {
        Transactions transaction = factory.createDeposit(dummyAccount, amount);

        assertNotNull(transaction);
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(dummyAccount, transaction.getSourceAccount());
        assertNull(transaction.getTargetAccount());
        assertNotNull(transaction.getTimestamp());
    }

    @Test
    void createWithdrawal_ShouldReturnWithdrawalTransaction() {
        Transactions transaction = factory.createWithdrawal(dummyAccount, amount);

        assertNotNull(transaction);
        assertEquals(TransactionType.WITHDRAWAL, transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(dummyAccount, transaction.getSourceAccount());
        assertNull(transaction.getTargetAccount());
        assertNotNull(transaction.getTimestamp());
    }

    @Test
    void createTransfer_ShouldReturnTransferTransaction() {
        Transactions transaction = factory.createTransfer(dummyAccount, dummyTargetAccount, amount);

        assertNotNull(transaction);
        assertEquals(TransactionType.TRANSFER, transaction.getType());
        assertEquals(amount, transaction.getAmount());
        assertEquals(dummyAccount, transaction.getSourceAccount());
        assertEquals(dummyTargetAccount, transaction.getTargetAccount());
        assertNotNull(transaction.getTimestamp());
    }
}
