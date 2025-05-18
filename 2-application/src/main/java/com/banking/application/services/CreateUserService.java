package com.banking.application.services;

import com.banking.application.ports.in.CreateUserUseCase;
import com.banking.application.ports.out.CustomerRepositoryPort;
import com.banking.domain.model.Customer;
import com.banking.domain.model.valueobjects.PIN;
import com.banking.domain.model.valueobjects.Password;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateUserService implements CreateUserUseCase {

    private final CustomerRepositoryPort customerRepository;

    public CreateUserService(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Customer createUser(String username, String rawPassword, String rawPin) {
        if (customerRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Benutzername ist bereits vergeben.");
        }

        Password password = new Password(rawPassword);
        PIN pin = new PIN(rawPin);
        Customer customer = new Customer(username, password, pin);
        return customerRepository.save(customer);
    }
}