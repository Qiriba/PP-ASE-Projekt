package com.banking.application.services;

import com.banking.application.ports.out.AccountRepositoryPort;
import com.banking.application.ports.out.CustomerRepositoryPort;
import com.banking.domain.model.Account;
import com.banking.domain.model.Customer;
import com.banking.domain.model.dto.AccountCreationRequestDTO;
import com.banking.domain.model.valueobjects.Money;

import com.banking.domain.model.valueobjects.PIN;
import com.banking.domain.model.valueobjects.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountCreationServiceTest {

    private CustomerRepositoryPort customerRepository;
    private AccountRepositoryPort accountRepository;
    private AccountCreationService accountCreationService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepositoryPort.class);
        accountRepository = mock(AccountRepositoryPort.class);
        accountCreationService = new AccountCreationService(customerRepository, accountRepository);
    }

    @Test
    void shouldCreateAccountForExistingCustomer() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer("testuser", new Password("password"), new PIN("1234"));
        AccountCreationRequestDTO request = new AccountCreationRequestDTO(new BigDecimal(100), "EUR");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = accountCreationService.createAccount(customerId, request);

        // Assert
        assertNotNull(result);
        assertEquals(Money.class, result.getBalance().getClass());
        assertEquals(new BigDecimal("100.00"), result.getBalance().getAmount());
        assertEquals(Currency.getInstance("EUR"), result.getBalance().getCurrency());

        verify(customerRepository).findById(customerId);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        AccountCreationRequestDTO request = new AccountCreationRequestDTO(new BigDecimal(100), "EUR");

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountCreationService.createAccount(customerId, request);
        });

        assertEquals("Kunde nicht gefunden", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }
}
