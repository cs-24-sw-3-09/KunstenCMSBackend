package com.github.cs_24_sw_3_09.CMS.routing;

//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.github.cs_24_sw_3_09.CMS.modelClasses.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Router {

    @GetMapping("/api/display_devices")
    List<DisplayDevice> allDisplayDevices() {
        System.out.println("/api/display_devices");

        // TODO: Replace code below with db dds - you must create a new builder for
        // every device
        DisplayDevice.DisplayDeviceBuilder ddBuilder = new DisplayDevice.DisplayDeviceBuilder();
        ddBuilder.setName("hej");
        DisplayDevice d = ddBuilder.getDisplayDevice();
        System.out.println(d);
        ArrayList<DisplayDevice> ddList = new ArrayList<DisplayDevice>();
        ddList.add(d);
        ddBuilder.setName("2");
        ddList.add(ddBuilder.getDisplayDevice());

        return ddList;
    }

    @GetMapping("/api/time_slots")
    List<TimeSlot> allTimeSlots() {
        TimeSlot.TimeSlotBuilder tsBuilder = new TimeSlot.TimeSlotBuilder();
        tsBuilder.setName("new ts");
        TimeSlot ts = tsBuilder.getTimeSlot();
        ArrayList<TimeSlot> tsList = new ArrayList<TimeSlot>();
        tsList.add(ts);
        return tsList;
    }

    @GetMapping("/")
    String home() {
        System.out.println("/");
        // return "Hello World!";
        return "redirect:/index.jpg";
    }

    @GetMapping("/test")
    String test() {
        System.out.println("/test");
        // app.getModule().
        return "test123!";
    }

    // Example of how to make rest with varible path
    @GetMapping("/test/{testid}")
    String testID(@PathVariable String testid) {
        System.out.println("/test with ID " + testid);
        return "test your id:" + testid;
    }

    /*
     * @Override
     * public void addResourceHandlers(ResourceHandlerRegistry registry) {
     * registry
     * .addResourceHandler("/files/**")
     * .addResourceLocations("file://");
     * }
     */

}
