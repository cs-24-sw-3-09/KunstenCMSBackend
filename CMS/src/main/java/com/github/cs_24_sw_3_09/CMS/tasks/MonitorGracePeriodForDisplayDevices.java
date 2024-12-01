package com.github.cs_24_sw_3_09.CMS.tasks;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.cs_24_sw_3_09.CMS.services.EmailService;

@Component
public class MonitorGracePeriodForDisplayDevices {

    @Autowired
    private EmailService emailService;

    private Map<Integer, Long> lastEmailSentMap = new HashMap<>();
    private static final long GRACE_PERIOD_MS = 2 * 60 * 1000; // Send only with two min spaceing

    public void sendDisconnectMailWithGrace(int ddId) {
        // Check if the device ID is present in the map and if the grace period has passed
        if (lastEmailSentMap.containsKey(ddId)) {
            long lastSentTime = lastEmailSentMap.get(ddId);
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSentTime < GRACE_PERIOD_MS) {
                System.out.println("Email not sent for screen " + ddId + " due to grace period.");
                return;
            }
        }

        // Proceed with sending the email
        System.out.println(emailService.sendDDDisconnectMail(ddId));

        // Update the last sent time in the map
        lastEmailSentMap.put(ddId, System.currentTimeMillis());
    }
}
