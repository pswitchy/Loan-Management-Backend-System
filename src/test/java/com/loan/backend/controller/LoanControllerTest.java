package com.loan.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan.backend.LoanManagementApplication;
import com.loan.backend.dto.LoanRequest;
import com.loan.backend.dto.LoanResponse;
import com.loan.backend.enums.LoanStatus;
import com.loan.backend.enums.LoanType;
import com.loan.backend.service.LoanService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LoanManagementApplication.class)
@AutoConfigureMockMvc
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void testApplyLoan() throws Exception {
        LoanRequest request = LoanRequest.builder()
                .customerId(1L)
                .loanType(LoanType.PERSONAL)
                .principalAmount(BigDecimal.valueOf(50000))
                .tenureMonths(12)
                .monthlyIncome(BigDecimal.valueOf(40000))
                .creditScore(750)
                .build();

        LoanResponse response = LoanResponse.builder()
                .id(1L)
                .status(LoanStatus.APPLIED)
                .build();

        Mockito.when(loanService.applyLoan(any(LoanRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/loans/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
