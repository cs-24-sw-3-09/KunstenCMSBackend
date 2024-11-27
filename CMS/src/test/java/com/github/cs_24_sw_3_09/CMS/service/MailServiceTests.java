package com.github.cs_24_sw_3_09.CMS.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.cs_24_sw_3_09.CMS.model.entities.EmailDetailsEntity;
import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.MailServiceImpl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTests {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailServiceImpl mailService;

    public MailServiceTests() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendSimpleMailSuccess() {
        String mockSender = "sender@example.com";
        String mockReceiver = "receiver@example.com";

        mailService.setSender(mockSender);
        mailService.setEmailReceiver(mockReceiver);

        EmailDetailsEntity emailDetails = EmailDetailsEntity.builder()
                .recipient(mockReceiver)
                .msgBody("Test Message")
                .subject("Test Subject")
                .build();

        String result = mailService.sendSimpleMail(emailDetails);

        // Verifies the JavaMailSender was called
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

        // Assert
        assertEquals("Mail Sent Successfully...", result);
    }

    @Test
    void testSendSimpleMailFailure() {
        String mockSender = "sender@example.com";
        String mockReceiver = "receiver@example.com";

        mailService.setSender(mockSender);
        mailService.setEmailReceiver(mockReceiver);

        EmailDetailsEntity emailDetails = EmailDetailsEntity.builder()
                .recipient(mockReceiver)
                .msgBody("Test Message")
                .subject("Test Subject")
                .build();

        // Simulate exception when sending mail
        doThrow(new RuntimeException("Mail server is down")).when(javaMailSender).send(any(SimpleMailMessage.class));

        String result = mailService.sendSimpleMail(emailDetails);

        // Assert
        assertTrue(result.contains("Error while Sending Mail..."));
    }

    @Test
    void testSendDDDisconnectMail() {
        String mockReceiver = "receiver@example.com";
        String mockSender = "sender@example.com";

        mailService.setEmailReceiver(mockReceiver);
        mailService.setSender(mockSender);

        String result = mailService.sendDDDisconnectMail(123);

        assertEquals("Mail Sent Successfully...", result);
    }
}
