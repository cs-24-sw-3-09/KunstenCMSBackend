package com.github.cs_24_sw_3_09.CMS.routing;

//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.github.cs_24_sw_3_09.CMS.DB.GetAllObj;
import com.github.cs_24_sw_3_09.CMS.DB.GetSingleObj;
import com.github.cs_24_sw_3_09.CMS.DB.HikariCPDataSource;
import com.github.cs_24_sw_3_09.CMS.modelClasses.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RouterGET {

    @GetMapping("/api/display_devices")
    List<DisplayDevice> allDisplayDevices() throws SQLException {
        System.out.println("GET /api/display_devices");
        List<DisplayDevice> ddList = GetAllObj.buildDisplayDeviceAll();
        return ddList;
    }

    @GetMapping("/api/display_devices/{id}")
    DisplayDevice getsingleDisplayDevice(@PathVariable int id) throws SQLException {
        System.out.println("GET /api/display_devices/" + id);
        DisplayDevice dd = GetSingleObj.buildDisplayDeviceById(id);
        return dd;
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
}

/*
 * 
 * @GetMapping("/")
 * String home() {
 * System.out.println("/");
 * // return "Hello World!";
 * return "redirect:/index.jpg";
 * }
 * 
 * @GetMapping("/test")
 * String test() {
 * System.out.println("/test");
 * // app.getModule().
 * return "test123!";
 * }
 * 
 * // Example of how to make rest with varible path
 * 
 * @GetMapping("/test/{testid}")
 * String testID(@PathVariable String testid) {
 * System.out.println("/test with ID " + testid);
 * return "test your id:" + testid;
 * }
 * 
 */