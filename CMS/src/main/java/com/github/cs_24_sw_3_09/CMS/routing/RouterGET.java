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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/api/schedule/week")
    Map<String, List<Map<String, Object>>> getScheduleWeek() throws SQLException {
        Connection db = HikariCPDataSource.getConnection();

        String query = """
                SELECT display_devices.id AS device_id, display_devices.name AS device_name,
                time_slots.id AS time_slot_id, time_slots.name AS time_slot_name, time_slots.start_date, time_slots.end_date, time_slots.start_time, time_slots.end_time,
                weekdays.monday, weekdays.tuesday, weekdays.wednesday, weekdays.thursday, weekdays.friday, weekdays.saturday, weekdays.sunday
                FROM display_devices
                LEFT OUTER JOIN time_slot_display_devices ON time_slot_display_devices.display_device_id = display_devices.id
                LEFT OUTER JOIN time_slots ON time_slot_display_devices.time_slot_id = time_slots.id
                LEFT OUTER JOIN weekdays ON time_slots.weekdays_chosen_id = weekdays.id;""";

        try (PreparedStatement statement = db.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();

                while (resultSet.next()) {
                    String name = resultSet.getString("device_name");

                    Map<String, Object> row = new HashMap<>();
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = resultSet.getMetaData().getColumnLabel(i);
                        Object value = resultSet.getObject(columnName);
                        row.put(columnName, value);
                    }

                    resultMap.computeIfAbsent(name, _ -> new ArrayList<>());
                    // Only add row if time_slot_id is not null
                    if(row.containsKey("time_slot_id") && row.get("time_slot_id") != null) resultMap.get(name).add(row);
                }
                return resultMap;
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all objects: " + e.getMessage());
            return null;
        }
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