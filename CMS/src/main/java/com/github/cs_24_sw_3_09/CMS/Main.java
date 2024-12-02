package com.github.cs_24_sw_3_09.CMS;

import com.github.cs_24_sw_3_09.CMS.config.SocketIOConfig;
import com.github.cs_24_sw_3_09.CMS.socketConnection.SocketIOModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // Start Spring Boot application
        ApplicationContext context = SpringApplication.run(Main.class, args);
        // Start SocketIO server
        SocketIOModule socketModule = context.getBean(SocketIOModule.class);
        socketModule.start(); // SocketIO will automatically gracefully shutdown due to PreDestroy annotation.
    }
}
