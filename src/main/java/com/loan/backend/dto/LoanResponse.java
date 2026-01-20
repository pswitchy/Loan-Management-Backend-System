package com.loan.backend.dto;

import com.loan.backend.enums.LoanStatus;
import com.loan.backend.enums.LoanType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private LoanType loanType;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emiAmount;
    private LoanStatus status;
    private LocalDate applicationDate;
    private LocalDate approvedDate;
    private String remark;

    public LoanResponse() {}

    public LoanResponse(Long id, Long customerId, String customerName, LoanType loanType, BigDecimal principalAmount, BigDecimal interestRate, Integer tenureMonths, BigDecimal emiAmount, LoanStatus status, LocalDate applicationDate, LocalDate approvedDate, String remark) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.loanType = loanType;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.emiAmount = emiAmount;
        this.status = status;
        this.applicationDate = applicationDate;
        this.approvedDate = approvedDate;
        this.remark = remark;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }

    public BigDecimal getEmiAmount() { return emiAmount; }
    public void setEmiAmount(BigDecimal emiAmount) { this.emiAmount = emiAmount; }

    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }

    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }

    public LocalDate getApprovedDate() { return approvedDate; }
    public void setApprovedDate(LocalDate approvedDate) { this.approvedDate = approvedDate; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public static class Builder {
        private Long id;
        private Long customerId;
        private String customerName;
        private LoanType loanType;
        private BigDecimal principalAmount;
        private BigDecimal interestRate;
        private Integer tenureMonths;
        private BigDecimal emiAmount;
        private LoanStatus status;
        private LocalDate applicationDate;
        private LocalDate approvedDate;
        private String remark;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder customerId(Long customerId) { this.customerId = customerId; return this; }
        public Builder customerName(String customerName) { this.customerName = customerName; return this; }
        public Builder loanType(LoanType loanType) { this.loanType = loanType; return this; }
        public Builder principalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; return this; }
        public Builder interestRate(BigDecimal interestRate) { this.interestRate = interestRate; return this; }
        public Builder tenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; return this; }
        public Builder emiAmount(BigDecimal emiAmount) { this.emiAmount = emiAmount; return this; }
        public Builder status(LoanStatus status) { this.status = status; return this; }
        public Builder applicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; return this; }
        public Builder approvedDate(LocalDate approvedDate) { this.approvedDate = approvedDate; return this; }
        public Builder remark(String remark) { this.remark = remark; return this; }

        public LoanResponse build() {
            return new LoanResponse(id, customerId, customerName, loanType, principalAmount, interestRate, tenureMonths, emiAmount, status, applicationDate, approvedDate, remark);
        }
    }
}
