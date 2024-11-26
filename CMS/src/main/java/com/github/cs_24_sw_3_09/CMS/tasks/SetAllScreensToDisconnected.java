package com.github.cs_24_sw_3_09.CMS.tasks;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;

import jakarta.annotation.PostConstruct;

@Component
@Profile("!test")
public class SetAllScreensToDisconnected {

    private final DisplayDeviceService displayDeviceService;

    public SetAllScreensToDisconnected(DisplayDeviceService displayDeviceService) {
        this.displayDeviceService = displayDeviceService;
        System.out.println("Disconncted all screens");
    }

    @PostConstruct
    public void init() {
        displayDeviceService.disconnectAllScreens();
    }
}
