package com.banking.application.ports.in;

import com.banking.domain.model.Customer;
public interface CreateUserUseCase {
    Customer createUser(String username, String rawPassword, String rawPin);
    void register(String username, String password, String pin);
}