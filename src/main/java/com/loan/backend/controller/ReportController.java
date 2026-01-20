package com.loan.backend.controller;

import com.loan.backend.dto.LoanResponse;
import com.loan.backend.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/active-loans")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanResponse>> getActiveLoans() {
        return ResponseEntity.ok(reportService.getActiveLoans());
    }

    @GetMapping("/overdue-emis")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getOverdueEmis() {
        return ResponseEntity.ok(reportService.getOverdueEmis());
    }

    @GetMapping("/outstanding-per-customer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<Long, BigDecimal>> getOutstandingPerCustomer() {
        return ResponseEntity.ok(reportService.getTotalOutstandingPerCustomer());
    }

    @GetMapping("/monthly-disbursements")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyDisbursement() {
        return ResponseEntity.ok(reportService.getMonthlyDisbursement());
    }
}
