package com.github.cs_24_sw_3_09.CMS;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.services.PushTSService;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Component
@Profile("!test")
public class PushTSSchedule {

    private final PushTSService pushTSService;

    public PushTSSchedule(PushTSService pushTSService) {
        this.pushTSService = pushTSService;
    }

    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = 60 * 1000) // Runs every 1 minutes
    public void pushTSSchedule() {
        pushTSService.updateDisplayDevicesToNewTimeSlots();
    }
}
