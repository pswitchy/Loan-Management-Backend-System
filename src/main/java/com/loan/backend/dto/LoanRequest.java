package com.loan.backend.dto;

import com.loan.backend.enums.LoanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class LoanRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Loan Type is required")
    private LoanType loanType;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "1000.0", message = "Minimum loan amount is 1000")
    private BigDecimal principalAmount;

    @NotNull(message = "Tenure is required")
    @Min(value = 6, message = "Minimum tenure is 6 months")
    private Integer tenureMonths;
    
    @NotNull(message = "Monthly income is required for eligibility check")
    private BigDecimal monthlyIncome;
    
    @NotNull(message = "Credit score is required")
    private Integer creditScore;

    public LoanRequest() {}

    public LoanRequest(Long customerId, LoanType loanType, BigDecimal principalAmount, Integer tenureMonths, BigDecimal monthlyIncome, Integer creditScore) {
        this.customerId = customerId;
        this.loanType = loanType;
        this.principalAmount = principalAmount;
        this.tenureMonths = tenureMonths;
        this.monthlyIncome = monthlyIncome;
        this.creditScore = creditScore;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }

    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public static class Builder {
        private Long customerId;
        private LoanType loanType;
        private BigDecimal principalAmount;
        private Integer tenureMonths;
        private BigDecimal monthlyIncome;
        private Integer creditScore;

        public Builder customerId(Long customerId) { this.customerId = customerId; return this; }
        public Builder loanType(LoanType loanType) { this.loanType = loanType; return this; }
        public Builder principalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; return this; }
        public Builder tenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; return this; }
        public Builder monthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; return this; }
        public Builder creditScore(Integer creditScore) { this.creditScore = creditScore; return this; }

        public LoanRequest build() {
            return new LoanRequest(customerId, loanType, principalAmount, tenureMonths, monthlyIncome, creditScore);
        }
    }
}
