package com.loan.backend.service;

import com.loan.backend.dto.LoanRequest;
import com.loan.backend.dto.LoanResponse;
import com.loan.backend.entity.Customer;
import com.loan.backend.entity.Loan;
import com.loan.backend.enums.LoanType;
import com.loan.backend.repository.CustomerRepository;
import com.loan.backend.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LoanEligibilityService eligibilityService;
    
    @Mock
    private RepaymentService repaymentService;

    @InjectMocks
    private LoanService loanService;

    private Customer customer;
    private LoanRequest loanRequest;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .kycVerified(true)
                .build();

        loanRequest = LoanRequest.builder()
                .customerId(1L)
                .loanType(LoanType.PERSONAL)
                .principalAmount(BigDecimal.valueOf(100000))
                .tenureMonths(12)
                .build();
    }

    @Test
    void testApplyLoan_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(eligibilityService.checkEligibility(any(LoanRequest.class))).thenReturn(true);
        when(eligibilityService.getInterestRate(LoanType.PERSONAL)).thenReturn(BigDecimal.valueOf(12.0));
        
        Loan savedLoan = Loan.builder()
                .id(1L)
                .customer(customer)
                .loanType(LoanType.PERSONAL)
                .principalAmount(BigDecimal.valueOf(100000))
                .interestRate(BigDecimal.valueOf(12.0))
                .tenureMonths(12)
                .build();
                
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        LoanResponse response = loanService.applyLoan(loanRequest);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(100000), response.getPrincipalAmount());
    }
}
