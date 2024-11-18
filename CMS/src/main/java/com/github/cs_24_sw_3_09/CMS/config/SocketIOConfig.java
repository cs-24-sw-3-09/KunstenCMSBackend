package com.github.cs_24_sw_3_09.CMS.config;

import com.github.cs_24_sw_3_09.CMS.socketConnection.SocketIOModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    private final SocketIOModule module;

    public SocketIOConfig() {
        module = new SocketIOModule("0.0.0.0", 3051);
    }

    @Bean
    public SocketIOModule socketIOModule() {
        return this.module;
    }
}
