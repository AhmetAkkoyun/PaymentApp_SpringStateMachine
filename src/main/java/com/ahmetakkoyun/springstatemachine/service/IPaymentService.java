package com.ahmetakkoyun.springstatemachine.service;

import com.ahmetakkoyun.springstatemachine.domain.EPaymentEvent;
import com.ahmetakkoyun.springstatemachine.domain.EPaymentState;
import com.ahmetakkoyun.springstatemachine.domain.Payment;
import org.springframework.statemachine.StateMachine;

public interface IPaymentService {

    Payment newPayment(Payment payment);
    StateMachine<EPaymentState, EPaymentEvent> preAuth(Long paymentId);
    StateMachine<EPaymentState, EPaymentEvent> authorizePayment(Long paymentId);
    StateMachine<EPaymentState, EPaymentEvent> declineAuth(Long paymentId);
}
