package com.github.cs_24_sw_3_09.CMS;

import java.util.HashSet;

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
                .model("Samsung")
                .name("Skærm Esbjerg1")
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

    public static TimeSlotDto createTimeSlotDto(){
        return TimeSlotDto.builder()
                .name("Test1 TimeSlot")                                      
                .startDate(java.sql.Date.valueOf("2024-11-20"))                                      
                .endDate(java.sql.Date.valueOf("2024-11-20"))  
                .startTime(java.sql.Time.valueOf("10:20:30"))                               
                .endTime(java.sql.Time.valueOf("11:21:31"))                                
                .weekdaysChosen(3)                                          
                .displayContent(new SlideshowEntity())                     
                .displayDevices(new  HashSet<DisplayDeviceEntity>())                            
                .build();
    }

    public static TimeSlotEntity createTimeSlotEntity(){
        return TimeSlotEntity.builder()
                .name("Test2 TimeSlot")                                      
                .startDate(java.sql.Date.valueOf("2024-11-20"))                                      
                .endDate(java.sql.Date.valueOf("2024-11-20"))  
                .startTime(java.sql.Time.valueOf("10:20:30"))                               
                .endTime(java.sql.Time.valueOf("11:21:31"))                                
                .weekdaysChosen(3)                                          
                .displayContent(new SlideshowEntity())                     
                .displayDevices(new  HashSet<DisplayDeviceEntity>())                            
                .build();
    }
    
}
