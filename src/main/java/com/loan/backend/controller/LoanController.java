package com.loan.backend.controller;

import com.loan.backend.dto.LoanRequest;
import com.loan.backend.dto.LoanResponse;
import com.loan.backend.enums.LoanStatus;
import com.loan.backend.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<LoanResponse> applyLoan(@Valid @RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanService.applyLoan(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }
    
    @GetMapping("/my-loans")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')") 
    public ResponseEntity<List<LoanResponse>> getMyLoans(@RequestParam Long customerId) {
        return ResponseEntity.ok(loanService.getLoansByCustomerId(customerId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LoanResponse>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponse> updateLoanStatus(
            @PathVariable Long id, 
            @RequestParam LoanStatus status,
            @RequestBody(required = false) Map<String, String> body) {
        String remark = (body != null) ? body.get("remark") : null;
        return ResponseEntity.ok(loanService.updateLoanStatus(id, status, remark));
    }
}
