package com.loan.backend.service;

import com.loan.backend.dto.LoanRequest;
import com.loan.backend.enums.LoanType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class LoanEligibilityService {

    private static final int MIN_CREDIT_SCORE = 650;
    
    // Income Thresholds
    private static final Map<LoanType, BigDecimal> MIN_INCOME_MAP = Map.of(
        LoanType.PERSONAL, BigDecimal.valueOf(30000),
        LoanType.AUTO, BigDecimal.valueOf(25000),
        LoanType.HOME, BigDecimal.valueOf(50000)
    );
    
    // Interest Rates
    private static final Map<LoanType, BigDecimal> INTEREST_RATE_MAP = Map.of(
        LoanType.PERSONAL, BigDecimal.valueOf(12.5),
        LoanType.AUTO, BigDecimal.valueOf(10.0),
        LoanType.HOME, BigDecimal.valueOf(8.5)
    );

    public boolean checkEligibility(LoanRequest request) {
        if (request.getCreditScore() < MIN_CREDIT_SCORE) {
            return false;
        }
        
        BigDecimal minIncome = MIN_INCOME_MAP.getOrDefault(request.getLoanType(), BigDecimal.ZERO);
        if (request.getMonthlyIncome().compareTo(minIncome) < 0) {
            return false;
        }
        
        return true;
    }
    
    public BigDecimal getInterestRate(LoanType type) {
        return INTEREST_RATE_MAP.getOrDefault(type, BigDecimal.valueOf(15.0));
    }
}
