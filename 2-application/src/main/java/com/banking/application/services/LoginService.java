package com.banking.application.services;

import com.banking.application.ports.in.LoginUseCase;
import com.banking.application.ports.out.CustomerRepositoryPort;
import com.banking.domain.model.Customer;
import com.banking.domain.model.exceptions.AccountLockedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements LoginUseCase {

    private final CustomerRepositoryPort customerRepository;

    public LoginService(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> login(String username, String password) {
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(username);

        if (optionalCustomer.isEmpty()) return Optional.empty();

        Customer customer = optionalCustomer.get();

        try {
            if (customer.login(password)) {
                customerRepository.save(customer); // Resets failed login attempts
                return Optional.of(customer);
            } else {
                customerRepository.save(customer); // Update failed attempts
                return Optional.empty();
            }
        } catch (AccountLockedException e) {
            return Optional.empty();
        }
    }
}