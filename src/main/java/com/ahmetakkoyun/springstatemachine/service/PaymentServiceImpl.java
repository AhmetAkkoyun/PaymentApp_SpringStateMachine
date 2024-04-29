package com.ahmetakkoyun.springstatemachine.service;

import com.ahmetakkoyun.springstatemachine.domain.EPaymentEvent;
import com.ahmetakkoyun.springstatemachine.domain.EPaymentState;
import com.ahmetakkoyun.springstatemachine.domain.Payment;
import com.ahmetakkoyun.springstatemachine.repository.IPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.configuration.StateMachineConfigurationImportSelector;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements IPaymentService {
    private static final String PAYMENT_ID_HEADER = "payment_id";

    private final IPaymentRepository paymentRepository;
    private final StateMachineFactory<EPaymentState, EPaymentEvent> stateMachineFactory;
    private final StateMachineConfigurationImportSelector stateMachineConfigurationImportSelector;

    @Override
    public Payment newPayment(Payment payment) {
        payment.setState(EPaymentState.NEW);
        return paymentRepository.save(payment);
    }

    @Override
    public StateMachine<EPaymentState, EPaymentEvent> preAuth(Long paymentId) {
        StateMachine<EPaymentState, EPaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, EPaymentEvent.PRE_AUTHORIZE);
        return null;
    }

    @Override
    public StateMachine<EPaymentState, EPaymentEvent> authorizePayment(Long paymentId) {
        StateMachine<EPaymentState, EPaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, EPaymentEvent.AUTH_APPROVED);
        return null;
    }

    @Override
    public StateMachine<EPaymentState, EPaymentEvent> declineAuth(Long paymentId) {
        StateMachine<EPaymentState, EPaymentEvent> sm = build(paymentId);
        sendEvent(paymentId, sm, EPaymentEvent.AUTH_DECLINED);
        return null;
    }

    private void sendEvent(Long paymentId, StateMachine<EPaymentState, EPaymentEvent> sm, EPaymentEvent event) {
        Message msg = MessageBuilder.withPayload(event)
                .setHeader(PAYMENT_ID_HEADER, paymentId)
                .build();

        sm.sendEvent(msg);
    }

    private StateMachine<EPaymentState, EPaymentEvent> build(Long paymentId) {
        Payment payment = paymentRepository.getOne(paymentId);
        StateMachine<EPaymentState, EPaymentEvent> sm = stateMachineFactory.getStateMachine(Long.toString(payment.getId()));
        sm.stop();
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getState(), null, null, null));
                });
        sm.start();

        return sm;
    }
}
