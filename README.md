# Loan Management Backend System

A production-quality, monolithic Loan Management System built with Java 25 and Spring Boot. This application manages the entire loan lifecycle from customer registration to repayment.

## Architecture

- **Style:** Layered Monolithic (Controller → Service → Repository → Database)
- **Tech Stack:**
  - Java 25
  - Spring Boot 3.4.x
  - Spring Data JPA
  - Spring Security
  - H2 Database (Oracle Compatibility Mode)
  - Maven

## Features

1.  **Customer Management**: Registration, KYC verification, Profile fetching.
2.  **Loan Application**: Apply for Home, Personal, or Auto loans with eligibility checks (Credit Score, Income).
3.  **Approval Workflow**: Admin approval process with status tracking (APPLIED -> APPROVED -> DISBURSED).
4.  **EMI & Repayment**: Auto-calculation of EMIs, Schedule generation, and Payment tracking.
5.  **Reporting**: Dashboards for active loans, overdue EMIs, and monthly disbursements.

## Setup & Running

1.  **Prerequisites**:
    - Java 25 or later
    - Maven installed

2.  **Run the Application**:
    ```bash
    mvn spring-boot:run
    ```

3.  **Run Tests**:
    ```bash
    mvn test
    ```

## API Endpoints

### Authentication
- `GET /api/auth/**` - Public access (if implemented)
- **Note:** Uses HTTP Basic Auth.
  - Admin: `admin` / `admin123`
  - Customer: `customer` / `user123`

### Customers
- `POST /api/customers/register` - Register new customer (Admin)
- `GET /api/customers/{id}` - Get customer details
- `PUT /api/customers/{id}/kyc` - Verify KYC (Admin)

### Loans
- `POST /api/loans/apply` - Apply for a loan
- `GET /api/loans/my-loans?customerId={id}` - Get customer loans
- `PUT /api/loans/{id}/status?status={status}` - Approve/Reject loan (Admin)

### Repayments
- `GET /api/repayments/loan/{loanId}` - Get EMI schedule
- `POST /api/repayments/{scheduleId}/pay?amount={amount}` - Pay EMI

### Reports (Admin Only)
- `GET /api/reports/active-loans`
- `GET /api/reports/overdue-emis`
- `GET /api/reports/outstanding-per-customer`
- `GET /api/reports/monthly-disbursements`

## Database
Uses H2 in-memory database. 
Console enabled at: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:loandb`
- **User**: `sa`
- **Password**: `password`
