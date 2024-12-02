package com.github.cs_24_sw_3_09.CMS.config;

import com.github.cs_24_sw_3_09.CMS.socketConnection.SocketIOModule;
import com.github.cs_24_sw_3_09.CMS.tasks.MonitorGracePeriodForDisplayDevices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Bean
    public SocketIOModule socketIOModule(MonitorGracePeriodForDisplayDevices monitorGracePeriodForDisplayDevices) {
        return new SocketIOModule("0.0.0.0", 3051);
    }
}
