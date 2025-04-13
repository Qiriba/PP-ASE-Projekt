package com.banking.application.services;

import com.banking.application.ports.in.LoginUseCase;
import com.banking.application.ports.out.CustomerRepositoryPort;
import com.banking.domain.model.Customer;
import com.banking.domain.model.exceptions.AccountLockedException;
import com.banking.domain.security.JwtProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements LoginUseCase {

    private final CustomerRepositoryPort customerRepository;
    private JwtProvider jwtProvider;
    public LoginService(CustomerRepositoryPort customerRepository, JwtProvider jwtProvider){
        this.customerRepository = customerRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public String login(String username, String password) {
        Customer customer = customerRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        if (customer.getPassword().matches(password)) {
            return jwtProvider.generateToken(customer.getUsername());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}