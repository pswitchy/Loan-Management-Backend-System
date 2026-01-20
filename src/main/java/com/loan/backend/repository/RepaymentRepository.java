package com.loan.backend.repository;

import com.loan.backend.entity.RepaymentSchedule;
import com.loan.backend.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepaymentRepository extends JpaRepository<RepaymentSchedule, Long> {
    List<RepaymentSchedule> findByLoanId(Long loanId);
    List<RepaymentSchedule> findByPaymentStatus(PaymentStatus status);
    List<RepaymentSchedule> findByPaymentStatusAndDueDateBefore(PaymentStatus status, LocalDate date);
}
