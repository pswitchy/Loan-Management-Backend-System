package com.loan.backend.entity;

import com.loan.backend.enums.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repayment_schedules")
public class RepaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column(nullable = false)
    private BigDecimal installmentAmount;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private LocalDate paidDate;

    private BigDecimal lateFee;

    public RepaymentSchedule() {}

    public RepaymentSchedule(Long id, Loan loan, BigDecimal installmentAmount, LocalDate dueDate, PaymentStatus paymentStatus, LocalDate paidDate, BigDecimal lateFee) {
        this.id = id;
        this.loan = loan;
        this.installmentAmount = installmentAmount;
        this.dueDate = dueDate;
        this.paymentStatus = paymentStatus;
        this.paidDate = paidDate;
        this.lateFee = lateFee;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Loan getLoan() { return loan; }
    public void setLoan(Loan loan) { this.loan = loan; }

    public BigDecimal getInstallmentAmount() { return installmentAmount; }
    public void setInstallmentAmount(BigDecimal installmentAmount) { this.installmentAmount = installmentAmount; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDate getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDate paidDate) { this.paidDate = paidDate; }

    public BigDecimal getLateFee() { return lateFee; }
    public void setLateFee(BigDecimal lateFee) { this.lateFee = lateFee; }

    public static class Builder {
        private Long id;
        private Loan loan;
        private BigDecimal installmentAmount;
        private LocalDate dueDate;
        private PaymentStatus paymentStatus;
        private LocalDate paidDate;
        private BigDecimal lateFee;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder loan(Loan loan) { this.loan = loan; return this; }
        public Builder installmentAmount(BigDecimal installmentAmount) { this.installmentAmount = installmentAmount; return this; }
        public Builder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public Builder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public Builder paidDate(LocalDate paidDate) { this.paidDate = paidDate; return this; }
        public Builder lateFee(BigDecimal lateFee) { this.lateFee = lateFee; return this; }

        public RepaymentSchedule build() {
            return new RepaymentSchedule(id, loan, installmentAmount, dueDate, paymentStatus, paidDate, lateFee);
        }
    }
}
