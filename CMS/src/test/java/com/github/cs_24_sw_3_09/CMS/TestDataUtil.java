package com.github.cs_24_sw_3_09.CMS;

import java.time.LocalDateTime;

import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TagDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;

public class TestDataUtil {

    public static DisplayDeviceDto createDisplayDeviceDto() {

        return DisplayDeviceDto.builder()
                .displayOrientation("horizontal")
                .connectedState(false)
                .location("Esbjerg")
                .model("Samsung")
                .name("Skærm Esbjerg")
                .resolution("1920x1080")
                .build();
    }

    public static DisplayDeviceEntity createDisplayDeviceEntity() {

        return DisplayDeviceEntity.builder()
                .displayOrientation("horizontal")
                .connectedState(false)
                .location("Esbjerg")
                .model("Samsung")
                .name("Skærm Esbjerg")
                .resolution("1920x1080")
                .build();
    }

    public static TagDto createTagDto() {
        return TagDto.builder()
                .text("Test Tag")
                .build();
    }

    public static VisualMediaDto createVisualMediaDto() {
        return VisualMediaDto.builder()
                .description("dkaoidkao test descpt")
                .fileType("jpg")
                .lastDateModified("30/10/2003")
                .location("/djao/dhau")
                .name("Billede navn")
                .build();
    }

    public static VisualMediaEntity createVisualMediaEntity() {
        return VisualMediaEntity.builder()
                .description("dkaoidkao test descpt")
                .fileType("jpg")
                .lastDateModified("30/10/2003")
                .location("/djao/dhau")
                .name("Billede navn")
                .build();
    }

    public static UserDto createUserDto() {
        return UserDto.builder()
                .firstName("FirstTestName")
                .lastName("LastTestName")
                .email("test@test.com")
                .password("testtest1234")
                .notificationState(true)
                .mediaPlanner(true)
                .admin(true)
                .build();
    }

    public static UserEntity createUserEntity() {
        return UserEntity.builder()
                .firstName("FirstTestName")
                .lastName("LastTestName")
                .email("test@test.com")
                .password("testtest123")
                .notificationState(true)
                .mediaPlanner(true)
                .admin(true)
                .build();
    }

    public static TimeSlotEntity createTimeSlotEntityWithCurrentTime() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Adjust time ±2 hours
        LocalDateTime startDateTime = now.minusHours(2); // Current time - 2 hours
        LocalDateTime endDateTime = now.plusHours(2); // Current time + 2 hours

        return TimeSlotEntity.builder()
                .name("Test2 TimeSlot")
                .startDate(Date.valueOf(startDateTime.toLocalDate())) // Convert to java.sql.Date
                .endDate(Date.valueOf(endDateTime.toLocalDate())) // Convert to java.sql.Date
                .startTime(Time.valueOf(startDateTime.toLocalTime())) // Convert to java.sql.Time
                .endTime(Time.valueOf(endDateTime.toLocalTime())) // Convert to java.sql.Time
                .weekdaysChosen(3)
                .displayContent(assignedSlideshow())
                .displayDevices(assignDisplayDevice())
                .build();
    }

}
