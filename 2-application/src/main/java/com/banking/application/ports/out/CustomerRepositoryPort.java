package com.banking.application.ports.out;

import com.banking.domain.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryPort {
    Customer save(Customer customer);
    Optional<Customer> findById(UUID id);
    Optional<Customer> findByUsername(String username);
    Optional<Customer> findCustomerByAccountId(UUID accountId);

}