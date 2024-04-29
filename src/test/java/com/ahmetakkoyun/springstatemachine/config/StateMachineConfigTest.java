package com.ahmetakkoyun.springstatemachine.config;

import com.ahmetakkoyun.springstatemachine.domain.EPaymentEvent;
import com.ahmetakkoyun.springstatemachine.domain.EPaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<EPaymentState, EPaymentEvent> factory;

    @Test
    void testNewStateMachine() {
        StateMachine<EPaymentState, EPaymentEvent> sm = factory.getStateMachine(UUID.randomUUID());

        sm.start();

        System.out.println(sm.getState().toString());

        sm.sendEvent(EPaymentEvent.PRE_AUTHORIZE);

        System.out.println(sm.getState().toString());

        sm.sendEvent(EPaymentEvent.PRE_AUTH_APPROVED);

        System.out.println(sm.getState().toString());

        sm.sendEvent(EPaymentEvent.PRE_AUTH_DECLINED);

        System.out.println(sm.getState().toString());
    }

}