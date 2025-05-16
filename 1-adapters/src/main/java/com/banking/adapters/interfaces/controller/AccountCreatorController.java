package com.banking.adapters.interfaces.controller;

import com.banking.adapters.security.JwtUtil;
import com.banking.application.ports.in.AccountCreationUseCase;
import com.banking.domain.model.Account;
import com.banking.domain.model.Customer;
import com.banking.domain.model.dto.AccountCreationRequestDTO;
import com.banking.adapters.persistence.CustomerRepository;
import com.banking.domain.model.exceptions.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountCreatorController {

    private final AccountCreationUseCase accountCreationUseCase;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;
    public AccountCreatorController(AccountCreationUseCase accountCreationUseCase, CustomerRepository customerRepository, JwtUtil jwtUtil) {
        this.accountCreationUseCase = accountCreationUseCase;
        this.customerRepository = customerRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationRequestDTO requestDTO,
                                                 @RequestHeader("Authorization") String authHeader) {


        Customer customer = getCustomerFromToken(authHeader);
        Account account = accountCreationUseCase.createAccount(customer.getId(), requestDTO);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    private Customer getCustomerFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UUID customerId = jwtUtil.extractCustomerId(token);
        return getCustomerById(customerId);
    }

    private Customer getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));
    }
}
