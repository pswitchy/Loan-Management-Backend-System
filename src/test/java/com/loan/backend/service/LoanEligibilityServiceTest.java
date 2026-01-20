package com.loan.backend.service;

import com.loan.backend.dto.LoanRequest;
import com.loan.backend.enums.LoanType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanEligibilityServiceTest {

    private LoanEligibilityService eligibilityService;

    @BeforeEach
    void setUp() {
        eligibilityService = new LoanEligibilityService();
    }

    @Test
    void testCheckEligibility_Success() {
        LoanRequest request = LoanRequest.builder()
                .customerId(1L)
                .loanType(LoanType.PERSONAL)
                .principalAmount(BigDecimal.valueOf(50000))
                .tenureMonths(12)
                .monthlyIncome(BigDecimal.valueOf(30000))
                .creditScore(700)
                .build();

        assertTrue(eligibilityService.checkEligibility(request));
    }

    @Test
    void testCheckEligibility_LowCreditScore() {
        LoanRequest request = LoanRequest.builder()
                .customerId(1L)
                .loanType(LoanType.PERSONAL)
                .principalAmount(BigDecimal.valueOf(50000))
                .tenureMonths(12)
                .monthlyIncome(BigDecimal.valueOf(30000))
                .creditScore(600) // Less than 650
                .build();

        assertFalse(eligibilityService.checkEligibility(request));
    }

    @Test
    void testCheckEligibility_LowIncome() {
        LoanRequest request = LoanRequest.builder()
                .customerId(1L)
                .loanType(LoanType.PERSONAL)
                .principalAmount(BigDecimal.valueOf(50000))
                .tenureMonths(12)
                .monthlyIncome(BigDecimal.valueOf(10000)) // Less than 25000
                .creditScore(700)
                .build();

        assertFalse(eligibilityService.checkEligibility(request));
    }
}
