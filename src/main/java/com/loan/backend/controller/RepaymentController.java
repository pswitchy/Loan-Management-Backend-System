package com.loan.backend.controller;

import com.loan.backend.entity.RepaymentSchedule;
import com.loan.backend.service.RepaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/repayments")
public class RepaymentController {

    private final RepaymentService repaymentService;

    public RepaymentController(RepaymentService repaymentService) {
        this.repaymentService = repaymentService;
    }

    @GetMapping("/loan/{loanId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<List<RepaymentSchedule>> getSchedules(@PathVariable Long loanId) {
        return ResponseEntity.ok(repaymentService.getSchedulesByLoanId(loanId));
    }

    @PostMapping("/{scheduleId}/pay")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<RepaymentSchedule> payInstallment(
            @PathVariable Long scheduleId, 
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(repaymentService.payInstallment(scheduleId, amount));
    }
}
