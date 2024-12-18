package com.github.cs_24_sw_3_09.CMS.tasks;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.cs_24_sw_3_09.CMS.services.EmailService;

@EnableAsync
@Component
@Lazy
public class MonitorGracePeriodForDisplayDevices {

    @Autowired
    private EmailService emailService;

    private Map<Integer, Long> timeWhenDisconnected = new HashMap<>();
    private static final long GRACE_PERIOD_MS = 2 * 60 * 1000; // Send mail after two minutes

    @Async("asyncTaskExecutor")
    @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 60) // Runs every minute
    private void sendDisconnectMailWithGrace() {
        // Check if the device ID is present in the map
        for (Integer ddId : timeWhenDisconnected.keySet()) {
            long disconnectTime = timeWhenDisconnected.get(ddId);
            long currentTime = System.currentTimeMillis();
            // Has the screen been disconnected for long enough
            if (currentTime - disconnectTime < GRACE_PERIOD_MS) {
                continue;
            }
            // Remove the device such that no further emails is sent.
            timeWhenDisconnected.remove(ddId);

            // Proceed with sending the email
            emailService.sendDDDisconnectMail(ddId);
        }
    }

    public void displayDeviceDisconnected(Integer ddId) {
        timeWhenDisconnected.put(ddId, System.currentTimeMillis());
    }

    public void displayDeviceConnected(Integer ddId) {
        timeWhenDisconnected.remove(ddId);
    }
}
