package com.ahmetakkoyun.springstatemachine.repository;

import com.ahmetakkoyun.springstatemachine.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPaymentRepository extends JpaRepository<Payment, Long> {
}
