package com.loan.backend.service;

import com.loan.backend.dto.LoanRequest;
import com.loan.backend.dto.LoanResponse;
import com.loan.backend.entity.Customer;
import com.loan.backend.entity.Loan;
import com.loan.backend.enums.LoanStatus;
import com.loan.backend.exception.BusinessException;
import com.loan.backend.exception.ResourceNotFoundException;
import com.loan.backend.repository.CustomerRepository;
import com.loan.backend.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final LoanEligibilityService eligibilityService;
    private final RepaymentService repaymentService;

    public LoanService(LoanRepository loanRepository, CustomerRepository customerRepository, LoanEligibilityService eligibilityService, RepaymentService repaymentService) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.eligibilityService = eligibilityService;
        this.repaymentService = repaymentService;
    }

    @Transactional
    public LoanResponse applyLoan(LoanRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (!customer.isKycVerified()) {
            throw new BusinessException("Customer KYC not verified. Cannot apply for loan.");
        }

        if (!eligibilityService.checkEligibility(request)) {
            throw new BusinessException("Loan eligibility criteria not met (Credit Score or Income).");
        }

        BigDecimal interestRate = eligibilityService.getInterestRate(request.getLoanType());

        Loan loan = Loan.builder()
                .customer(customer)
                .loanType(request.getLoanType())
                .principalAmount(request.getPrincipalAmount())
                .interestRate(interestRate)
                .tenureMonths(request.getTenureMonths())
                .status(LoanStatus.APPLIED) // Default
                .applicationDate(LocalDate.now())
                .build();

        Loan savedLoan = loanRepository.save(loan);
        return mapToResponse(savedLoan);
    }
    
    public List<LoanResponse> getLoansByCustomerId(Long customerId) {
        return loanRepository.findByCustomerId(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public LoanResponse getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        return mapToResponse(loan);
    }

    @Transactional
    public LoanResponse updateLoanStatus(Long id, LoanStatus status, String remark) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        
        if (status == LoanStatus.APPROVED) {
            if (loan.getStatus() == LoanStatus.APPROVED) {
                 throw new BusinessException("Loan is already approved");
            }
            loan.setApprovedDate(LocalDate.now());
            // Calculate EMI to pass to schedule
            BigDecimal emi = calculateEMI(loan.getPrincipalAmount(), loan.getInterestRate(), loan.getTenureMonths());
            repaymentService.generateRepaymentSchedule(loan, emi);
        }
        if (remark != null) {
            loan.setRemark(remark);
        }
        
        loan.setStatus(status); // Ensure status is updated
        
        Loan saved = loanRepository.save(loan);
        return mapToResponse(saved);
    }

    private LoanResponse mapToResponse(Loan loan) {
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
    
    // EMI = P * r * (1+r)^n / ((1+r)^n - 1)
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
