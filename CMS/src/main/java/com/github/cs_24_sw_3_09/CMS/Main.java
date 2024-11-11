package com.github.cs_24_sw_3_09.CMS;

import org.springframework.boot.SpringApplication;

import com.github.cs_24_sw_3_09.CMS.modelClasses.DisplayDevice;

public class Main {
    public static void main(String[] args) {
        SpringApplication.run(CmsApplication.class, args); // Start springboot server

        DisplayDevice.DisplayDeviceBuilder ddBuilder = new DisplayDevice.DisplayDeviceBuilder();
        ddBuilder.setName("hej");
        DisplayDevice d = ddBuilder.getDisplayDevice();
        System.out.println(d);

    }
}
