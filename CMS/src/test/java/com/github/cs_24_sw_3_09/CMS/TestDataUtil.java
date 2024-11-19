package com.github.cs_24_sw_3_09.CMS;

import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TagDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;

public class TestDataUtil {

    public static DisplayDeviceDto createDisplayDeviceDto() {
        return new DisplayDeviceDto.Builder().setDisplayOrientation("horizontal")
                .setConnectedState(false)
                .setLocation("Esbjerg")
                .setModel("Samsung")
                .setName("Skærm Esbjerg")
                .setResolution("1920x1080")
                .build();
    }

    public static DisplayDeviceEntity createDisplayDeviceEntity() {

        return new DisplayDeviceEntity.Builder()
                .setDisplayOrientation("horizontal")
                .setConnectedState(false)
                .setLocation("Esbjerg")
                .setModel("Samsung")
                .setName("Skærm Esbjerg")
                .setResolution("1920x1080")
                .build();
    }

    public static TagDto createTagDto() {
        return new TagDto.Builder()
                .setText("Test Tag")
                .build();
    }

    public static VisualMediaDto createVisualMediaDto() {
        return new VisualMediaDto.Builder()
                .setDescription("dkaoidkao test descpt")
                .setFileType("jpg")
                .setLastDateModified("30/10/2003")
                .setLocation("/djao/dhau")
                .setName("Billede navn")
                .build();
    }

    public static VisualMediaEntity createVisualMediaEntity() {
        return new VisualMediaEntity.Builder()
                .setDescription("dkaoidkao test descpt")
                .setFileType("jpg")
                .setLastDateModified("30/10/2003")
                .setLocation("/djao/dhau")
                .setName("Billede navn")
                .build();
    }

    public static UserDto createUserDto() {
        return new UserDto.Builder()
                .setFirstName("FirstTestName")
                .setLastName("LastTestName")
                .setEmail("test@test.com")
                .setPassword("testtest1234")
                .setNotificationState(true)
                .setMediaPlanner(true)
                .setAdmin(true)
                .build();
    }

    public static UserEntity createUserEntity() {
        return new UserEntity.Builder()
                .setFirstName("FirstTestName")
                .setLastName("LastTestName")
                .setEmail("test@test.com")
                .setPassword("testtest123")
                .setNotificationState(true)
                .setMediaPlanner(true)
                .setAdmin(true)
                .build();
    }


}
