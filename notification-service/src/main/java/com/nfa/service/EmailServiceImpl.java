package com.nfa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mail.from}")
    private String mailFrom;

    @Value("${mail.subject}")
    private String mailSubject;

    @Value("${mail.text}")
    private String mailText;


    @Override
    public void sendEmailAsync(Set<String> emails) {
        log.info("Sending email to {}", emails);

        ExecutorService service = Executors.newFixedThreadPool(10);

        for (String email : emails) {
            service.submit(() -> sendEmail(email));
        }

        service.shutdownNow();
    }

    private void sendEmail(String email) {
        try {
            log.info("sending email to {}", email);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(mailFrom);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject(mailSubject);
            simpleMailMessage.setText(String.format(mailText, email));
            javaMailSender.send(simpleMailMessage);
        } catch (Exception mex) {
            log.error("send failed, exception: " + mex);
        }
    }
}
