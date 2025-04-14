package com.banking.application.ports.out;
import com.banking.domain.model.Transactions;

public interface TransactionRepositoryPort {
    Transactions save(Transactions transaction);
}
