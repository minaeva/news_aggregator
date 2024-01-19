package com.nfa.service;

import lombok.extern.slf4j.Slf4j;
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

    private final JavaMailSender javaMailSender;
    private final String mailFrom;
    private final String mailSubject;
    private final String mailText;

    public EmailServiceImpl(JavaMailSender javaMailSender,
                            @Value("${mail.from}") String mailFrom,
                            @Value("${mail.subject}") String mailSubject,
                            @Value("${mail.text}") String mailText) {
        this.javaMailSender = javaMailSender;
        this.mailFrom = mailFrom;
        this.mailSubject = mailSubject;
        this.mailText = mailText;
    }

    @Override
    public void sendEmailAsync(Set<String> emails) {
        if (mailFrom == null || mailSubject == null || mailText == null) {
            log.error("Mail properties are not properly set.");
            return;
        }
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
