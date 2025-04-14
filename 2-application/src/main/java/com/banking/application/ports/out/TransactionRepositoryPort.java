package com.banking.application.ports.out;
import com.banking.domain.model.Transactions;

public interface TransactionRepositoryPort {
    void save(Transactions transaction);
}
