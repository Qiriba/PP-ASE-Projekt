package com.banking.adapters.persistence;
import com.banking.application.ports.out.CustomerRepositoryPort;
import com.banking.domain.model.Customer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SpringCustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerRepository customerRepository;

    public SpringCustomerRepositoryAdapter(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }
}