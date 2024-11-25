package com.github.cs_24_sw_3_09.CMS.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.cs_24_sw_3_09.CMS.services.CleanUpDataBaseService;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Component
@Profile("!test")
public class CleanUpDataBase {

    private final CleanUpDataBaseService cleanUpDateBaseService;

    public CleanUpDataBase(CleanUpDataBaseService cleanUpDateBaseService) {
        this.cleanUpDateBaseService = cleanUpDateBaseService;
    }

    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = 1000 * 60 * 60 * 4) // Runs every 4 hour
    public void deleteTSWithoutDD() {
        cleanUpDateBaseService.deleteTSWithoutDD();
    }
}
