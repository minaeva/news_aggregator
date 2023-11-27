package com.nfa.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmailClientImpl implements EmailClient {

    @Override
    public void sendEmail(String email) {
        log.info("Sending email to {}", email);
    }
}
