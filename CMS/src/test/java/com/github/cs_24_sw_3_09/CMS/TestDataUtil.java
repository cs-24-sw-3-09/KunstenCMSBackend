package com.github.cs_24_sw_3_09.CMS;

import java.util.HashSet;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TagDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;

public class TestDataUtil {

    public static DisplayDeviceDto createDisplayDeviceDto() {

        return DisplayDeviceDto.builder()
                .displayOrientation("horizontal")
                .connectedState(false)
                .location("Aalborg")
                .name("Skærm Esbjerg1")
                .resolution("1920x1080")
                .build();
    }

    public static DisplayDeviceEntity createDisplayDeviceEntity() {

        return DisplayDeviceEntity.builder()
                .displayOrientation("horizontal")
                .connectedState(false)
                .location("Esbjerg")
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
                .location("/djao/dhau1")
                .name("Billede navn1")
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
                .firstName("FirstTestName1")
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

    public static TimeSlotDto createTimeSlotDto() {
        return TimeSlotDto.builder()
                .name("Test1 TimeSlot")
                .startDate(java.sql.Date.valueOf("2024-11-20"))
                .endDate(java.sql.Date.valueOf("2024-11-20"))
                .startTime(java.sql.Time.valueOf("10:20:30"))
                .endTime(java.sql.Time.valueOf("11:21:31"))
                .weekdaysChosen(3)
                .displayContent(new SlideshowEntity())
                .displayDevices(new HashSet<DisplayDeviceEntity>())
                .build();
    }

    public static TimeSlotEntity createTimeSlotEntity() {
        return TimeSlotEntity.builder()
                .name("Test2 TimeSlot")
                .startDate(java.sql.Date.valueOf("2024-11-20"))
                .endDate(java.sql.Date.valueOf("2024-11-20"))
                .startTime(java.sql.Time.valueOf("10:20:30"))
                .endTime(java.sql.Time.valueOf("11:21:31"))
                .weekdaysChosen(3)
                .displayContent(assignedSlideshow())
                .displayDevices(assignDisplayDevice())
                .build();
    }

    public static TimeSlotEntity createTimeSlotEntityWithOutDisplayDevice() {
        return TimeSlotEntity.builder()
                .name("Test2 TimeSlot")
                .startDate(java.sql.Date.valueOf("2024-11-20"))
                .endDate(java.sql.Date.valueOf("2024-11-20"))
                .startTime(java.sql.Time.valueOf("10:20:30"))
                .endTime(java.sql.Time.valueOf("11:21:31"))
                .weekdaysChosen(3)
                .displayContent(assignedSlideshow())
                .build();
    }

    public static SlideshowEntity assignedSlideshow() {
        return SlideshowEntity.builder()
                .name("test1")
                .build();
    }

    public static HashSet<DisplayDeviceEntity> assignDisplayDevice() {
        HashSet<DisplayDeviceEntity> displayDevices = new HashSet<>();

        DisplayDeviceEntity dd = DisplayDeviceEntity.builder()
                .displayOrientation("horizontal")
                .connectedState(false)
                .location("Aalborg")
                .name("Skærm Esbjerg1")
                .resolution("1920x1080")
                .build();
        displayDevices.add(dd);
        return displayDevices;
    }

    public static TimeSlotEntity createTimeSlotEntityWithCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.minusHours(2);
        LocalDateTime endDateTime = now.plusHours(2);

        return TimeSlotEntity.builder()
                .name("Test2 TimeSlot")
                .startDate(Date.valueOf(startDateTime.toLocalDate()))
                .endDate(Date.valueOf(endDateTime.toLocalDate()))
                .startTime(Time.valueOf(startDateTime.toLocalTime()))
                .endTime(Time.valueOf(endDateTime.toLocalTime()))
                .weekdaysChosen(3)
                .displayContent(assignedSlideshow())
                .displayDevices(assignDisplayDevice())
                .build();
    }

}
