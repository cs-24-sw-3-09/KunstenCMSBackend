package com.github.cs_24_sw_3_09.CMS.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Service
@EnableAsync
public class FunctionComponent {

    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = 3 * 1000) // Runs every 3 minutes
    public void getDataFromDB() {
        System.out.println("Hej");
        // Your code to get data from the DB
    }
}
