package com.ahmetakkoyun.springstatemachine.config;

import com.ahmetakkoyun.springstatemachine.domain.EPaymentEvent;
import com.ahmetakkoyun.springstatemachine.domain.EPaymentState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Slf4j
@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<EPaymentState, EPaymentEvent> {
    @Override
    public void configure(StateMachineStateConfigurer<EPaymentState, EPaymentEvent> states) throws Exception {
        states.withStates()
                .initial(EPaymentState.NEW)
                .states(EnumSet.allOf(EPaymentState.class))
                .end(EPaymentState.AUTH)
                .end(EPaymentState.PRE_AUTH_ERROR)
                .end(EPaymentState.AUTH_ERROR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EPaymentState, EPaymentEvent> transitions) throws Exception {
        transitions.withExternal().source(EPaymentState.NEW).target(EPaymentState.NEW).event(EPaymentEvent.PRE_AUTHORIZE)
                .and()
                .withExternal().source(EPaymentState.NEW).target(EPaymentState.PRE_AUTH).event(EPaymentEvent.PRE_AUTH_APPROVED)
                .and()
                .withExternal().source(EPaymentState.NEW).target(EPaymentState.PRE_AUTH_ERROR).event(EPaymentEvent.PRE_AUTH_DECLINED);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<EPaymentState, EPaymentEvent> config) throws Exception {
        StateMachineListenerAdapter<EPaymentState, EPaymentEvent> adapter = new StateMachineListenerAdapter<>(){
            @Override
            public void stateChanged(State<EPaymentState, EPaymentEvent> from, State<EPaymentState, EPaymentEvent> to) {
                log.info(String.format("state changed from %s to %s", from, to));
            }
        };

        config.withConfiguration()
                .listener(adapter);
    }
}
