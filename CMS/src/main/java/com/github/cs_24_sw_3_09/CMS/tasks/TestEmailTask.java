package com.github.cs_24_sw_3_09.CMS.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.cs_24_sw_3_09.CMS.services.EmailService;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Component
@Profile("!test")
public class TestEmailTask {

    private final EmailService emailService;

    public TestEmailTask(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = 60 * 10000000) // Runs every 1 minutes
    public void pushTSSchedule() {

        System.out.println(emailService.sendDDDisconnectMail(5));
    }
}
