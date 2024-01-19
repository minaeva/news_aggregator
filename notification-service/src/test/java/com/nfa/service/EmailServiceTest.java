package com.nfa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

   @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailServiceImpl(javaMailSender,
                "from@gmail.com",
                "Subject",
                "Email Text");
    }

    @Test
    public void testSendEmailAsync() throws InterruptedException {

        Set<String> emails = Set.of("test1@example.com", "test2@example.com");
        CountDownLatch latch = new CountDownLatch(emails.size());

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendEmailAsync(emails);

        boolean await = latch.await(5, TimeUnit.SECONDS);
        assertTrue(await, "Timeout waiting for email sending tasks to complete");

        verify(javaMailSender, times(emails.size())).send(mailMessageCaptor.capture());

    }

}
