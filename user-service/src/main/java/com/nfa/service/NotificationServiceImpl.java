package com.nfa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;

    @Value("${mail.from}")
    private String mailFrom;

    @Value("${mail.to}")
    private String mailTo;

    @Value("${mail.subject}")
    private String mailSubject;

    @Override
    public void notifyAdminAboutError(String message) {
        try {
            log.info("sending email {} to {}", message, mailTo);
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(mailFrom);
            simpleMailMessage.setTo(mailTo);
            simpleMailMessage.setSubject(mailSubject);
            simpleMailMessage.setText(String.format(message, mailTo));
            javaMailSender.send(simpleMailMessage);
        } catch (Exception ex) {
            log.error("sending email {} to {} failed, exception: {}", message, mailTo, ex.getMessage());
        }
    }
}
