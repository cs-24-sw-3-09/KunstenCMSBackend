package com.github.cs_24_sw_3_09.CMS.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.services.CleanUpDateBaseService;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Component
public class CleanUpDateBase {

    private final CleanUpDateBaseService cleanUpDateBaseService;

    public CleanUpDateBase(CleanUpDateBaseService cleanUpDateBaseService) {
        this.cleanUpDateBaseService = cleanUpDateBaseService;
    }

    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = 1000 * 60 * 60 * 4) // Runs every 4 hour
    public void deleteTSWithoutDD() {
        cleanUpDateBaseService.deleteTSWithoutDD();
    }
}
