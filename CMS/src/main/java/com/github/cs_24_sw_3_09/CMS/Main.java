package com.github.cs_24_sw_3_09.CMS;

import com.github.cs_24_sw_3_09.CMS.config.SocketIOConfig;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // Start springboot server
        ApplicationContext context = SpringApplication.run(Main.class, args);
        // Start SocketIO server
        SocketIOConfig socketConfig = context.getBean(SocketIOConfig.class);
        socketConfig.socketIOModule().start(); // SocketIO will automatically gracefully shutdown due to PreDestroy
                                               // annotation.
    }
}
