package com.loan.backend.service;

import com.loan.backend.entity.Loan;
import com.loan.backend.entity.RepaymentSchedule;
import com.loan.backend.enums.PaymentStatus;
import com.loan.backend.exception.BusinessException;
import com.loan.backend.exception.ResourceNotFoundException;
import com.loan.backend.repository.RepaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RepaymentService {

    private final RepaymentRepository repaymentRepository;

    public RepaymentService(RepaymentRepository repaymentRepository) {
        this.repaymentRepository = repaymentRepository;
    }

    @Transactional
    public void generateRepaymentSchedule(Loan loan, BigDecimal emiAmount) {
        List<RepaymentSchedule> schedules = new ArrayList<>();
        LocalDate dueDate = loan.getApprovedDate().plusMonths(1); // First EMI next month

        for (int i = 1; i <= loan.getTenureMonths(); i++) {
            RepaymentSchedule schedule = RepaymentSchedule.builder()
                    .loan(loan)
                    .installmentAmount(emiAmount)
                    .dueDate(dueDate)
                    .paymentStatus(PaymentStatus.PENDING)
                    .lateFee(BigDecimal.ZERO)
                    .build();
            schedules.add(schedule);
            dueDate = dueDate.plusMonths(1);
        }
        repaymentRepository.saveAll(schedules);
    }

    @Transactional
    public RepaymentSchedule payInstallment(Long scheduleId, BigDecimal amount) {
        RepaymentSchedule schedule = repaymentRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Installment not found"));

        if (schedule.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BusinessException("Installment already paid");
        }
        
        // Simple logic: Amount must cover installment + late fee
        BigDecimal totalDue = schedule.getInstallmentAmount().add(schedule.getLateFee());
        
        if (amount.compareTo(totalDue) < 0) {
            throw new BusinessException("Insufficient amount. Total due: " + totalDue);
        }

        schedule.setPaymentStatus(PaymentStatus.PAID);
        schedule.setPaidDate(LocalDate.now());
        
        return repaymentRepository.save(schedule);
    }
    
    public List<RepaymentSchedule> getSchedulesByLoanId(Long loanId) {
        return repaymentRepository.findByLoanId(loanId);
    }
}
