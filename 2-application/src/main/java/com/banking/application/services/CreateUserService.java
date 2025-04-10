package com.banking.application.services;

import com.banking.application.ports.in.CreateUserUseCase;
import com.banking.application.ports.out.CustomerRepositoryPort;
import com.banking.domain.model.Customer;
import com.banking.domain.model.valueobjects.PIN;
import com.banking.domain.model.valueobjects.Password;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateUserService implements CreateUserUseCase {

    private final CustomerRepositoryPort customerRepository;

    public CreateUserService(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createUser(String username, String rawPassword, String rawPin) {
        Password password = new Password(rawPassword);
        PIN pin = new PIN(rawPin);
        Customer customer = new Customer(UUID.randomUUID(), username, password, pin);
        return customerRepository.save(customer);
    }

    @Override
    public void register(String username, String password, String pin) {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(
                id,
                username,
                new Password(password),
                new PIN(pin)
        );

        customerRepository.save(customer);
    }
}