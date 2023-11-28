package com.nfa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmailAsync(Set<String> emails) {
        log.info("Sending email to {}", emails);

        ExecutorService service = Executors.newFixedThreadPool(10);

        for (String email : emails) {
            service.submit(() -> {
                sendEmail(email);
            });
        }

        service.shutdownNow();
    }


    @Override
    public void sendEmail(String email) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("s.minaeva@gmail.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("There's new articles fitting keywords you subscribed to");
        javaMailSender.send(simpleMailMessage);
        log.info("sending...");
        } catch (MessagingException mex) {
            log.error("send failed, exception: " + mex);
        }
    }
}
