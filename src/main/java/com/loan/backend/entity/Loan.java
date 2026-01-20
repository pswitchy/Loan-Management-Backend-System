package com.loan.backend.entity;

import com.loan.backend.enums.LoanStatus;
import com.loan.backend.enums.LoanType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanType loanType;

    @Column(nullable = false)
    private BigDecimal principalAmount;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer tenureMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    private LocalDate applicationDate;
    
    private LocalDate approvedDate;

    private String remark;
    
    @PrePersist
    protected void onCreate() {
        if (applicationDate == null) {
            applicationDate = LocalDate.now();
        }
        if (status == null) {
            status = LoanStatus.APPLIED;
        }
    }

    public Loan() {}

    public Loan(Long id, Customer customer, LoanType loanType, BigDecimal principalAmount, BigDecimal interestRate, Integer tenureMonths, LoanStatus status, LocalDate applicationDate, LocalDate approvedDate, String remark) {
        this.id = id;
        this.customer = customer;
        this.loanType = loanType;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.status = status;
        this.applicationDate = applicationDate;
        this.approvedDate = approvedDate;
        this.remark = remark;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }

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
        private Customer customer;
        private LoanType loanType;
        private BigDecimal principalAmount;
        private BigDecimal interestRate;
        private Integer tenureMonths;
        private LoanStatus status;
        private LocalDate applicationDate;
        private LocalDate approvedDate;
        private String remark;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder customer(Customer customer) { this.customer = customer; return this; }
        public Builder loanType(LoanType loanType) { this.loanType = loanType; return this; }
        public Builder principalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; return this; }
        public Builder interestRate(BigDecimal interestRate) { this.interestRate = interestRate; return this; }
        public Builder tenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; return this; }
        public Builder status(LoanStatus status) { this.status = status; return this; }
        public Builder applicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; return this; }
        public Builder approvedDate(LocalDate approvedDate) { this.approvedDate = approvedDate; return this; }
        public Builder remark(String remark) { this.remark = remark; return this; }

        public Loan build() {
            return new Loan(id, customer, loanType, principalAmount, interestRate, tenureMonths, status, applicationDate, approvedDate, remark);
        }
    }
}
