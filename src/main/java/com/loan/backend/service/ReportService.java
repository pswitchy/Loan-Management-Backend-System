package com.loan.backend.service;

import com.loan.backend.dto.LoanResponse;
import com.loan.backend.entity.Loan;
import com.loan.backend.entity.RepaymentSchedule;
import com.loan.backend.enums.LoanStatus;
import com.loan.backend.enums.PaymentStatus;
import com.loan.backend.repository.LoanRepository;
import com.loan.backend.repository.RepaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final LoanRepository loanRepository;
    private final RepaymentRepository repaymentRepository;
    
    public ReportService(LoanRepository loanRepository, RepaymentRepository repaymentRepository) {
        this.loanRepository = loanRepository;
        this.repaymentRepository = repaymentRepository;
    }

    public List<LoanResponse> getActiveLoans() {
        return loanRepository.findAll().stream()
                .filter(l -> l.getStatus() == LoanStatus.APPROVED || l.getStatus() == LoanStatus.DISBURSED)
                .map(this::mapToLoanResponse)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getOverdueEmis() {
        List<RepaymentSchedule> overdueList = repaymentRepository.findByPaymentStatusAndDueDateBefore(PaymentStatus.PENDING, LocalDate.now());
        
        return overdueList.stream()
                .map(schedule -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("scheduleId", schedule.getId());
                    map.put("loanId", schedule.getLoan().getId());
                    map.put("customerId", schedule.getLoan().getCustomer().getId());
                    map.put("customerName", schedule.getLoan().getCustomer().getFirstName() + " " + schedule.getLoan().getCustomer().getLastName());
                    map.put("installmentAmount", schedule.getInstallmentAmount());
                    map.put("dueDate", schedule.getDueDate());
                    map.put("lateFee", schedule.getLateFee());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public Map<Long, BigDecimal> getTotalOutstandingPerCustomer() {
        List<RepaymentSchedule> pending = repaymentRepository.findByPaymentStatus(PaymentStatus.PENDING);
        
        return pending.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getLoan().getCustomer().getId(),
                        Collectors.reducing(BigDecimal.ZERO, RepaymentSchedule::getInstallmentAmount, BigDecimal::add)
                ));
    }
    
    public Map<String, BigDecimal> getMonthlyDisbursement() {
        List<Loan> loans = loanRepository.findAll();
        
        return loans.stream()
                .filter(l -> l.getApprovedDate() != null)
                .collect(Collectors.groupingBy(
                        l -> l.getApprovedDate().getMonth().toString() + "-" + l.getApprovedDate().getYear(),
                        Collectors.reducing(BigDecimal.ZERO, Loan::getPrincipalAmount, BigDecimal::add)
                ));
    }
    
    private LoanResponse mapToLoanResponse(Loan loan) {
        BigDecimal emi = calculateEMI(loan.getPrincipalAmount(), loan.getInterestRate(), loan.getTenureMonths());
        
        return LoanResponse.builder()
                .id(loan.getId())
                .customerId(loan.getCustomer().getId())
                .customerName(loan.getCustomer().getFirstName() + " " + loan.getCustomer().getLastName())
                .loanType(loan.getLoanType())
                .principalAmount(loan.getPrincipalAmount())
                .interestRate(loan.getInterestRate())
                .tenureMonths(loan.getTenureMonths())
                .status(loan.getStatus())
                .applicationDate(loan.getApplicationDate())
                .approvedDate(loan.getApprovedDate())
                .remark(loan.getRemark())
                .emiAmount(emi)
                .build();
    }
    
    private BigDecimal calculateEMI(BigDecimal principal, BigDecimal annualRate, int months) {
        if (principal == null || annualRate == null || months <= 0) return BigDecimal.ZERO;
        
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal pow = onePlusR.pow(months);
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(pow);
        BigDecimal denominator = pow.subtract(BigDecimal.ONE);
        
        if (denominator.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
