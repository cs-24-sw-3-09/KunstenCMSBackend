package com.github.cs_24_sw_3_09.CMS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

import com.github.cs_24_sw_3_09.CMS.model.dto.*;
import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class TestDataUtil {

    public static DisplayDeviceDto createDisplayDeviceDto() {

        return DisplayDeviceDto.builder()
                .displayOrientation("horizontal")
                .location("Aalborg")
                .name("Skærm Esbjerg1")
                .resolution("1920x1080")
                .build();
    }

    public static DisplayDeviceDto createDisplayDeviceWithVisualMediaDto() {

        return DisplayDeviceDto.builder()
                .displayOrientation("horizontal")
                .location("Aalborg")
                .name("Skærm Esbjerg1")
                .resolution("1920x1080")
                .build();
    }

    public static DisplayDeviceEntity createDisplayDeviceEntity() {

        return DisplayDeviceEntity.builder()
                .displayOrientation("horizontal")
                .location("Esbjerg")
                .name("Skærm Esbjerg")
                .resolution("1920x1080")
                .timeSlots(new ArrayList<>())
                .build();
    }

    public static DisplayDeviceEntity createSecDisplayDeviceEntity() {

        return DisplayDeviceEntity.builder()
                .displayOrientation("vertical")
                .location("Esbjerg2")
                .name("Skærm Esbjerg2")
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
                // .lastDateModified("30/10/2003")
                .location("/djao/dhau1")
                .name("Billede navn1")
                .build();
    }

    public static VisualMediaEntity createVisualMediaEntity() {
        return VisualMediaEntity.builder()
                .description("dkaoidkao test descpt")
                .fileType("jpg")
                // .lastDateModified("30/10/2003")
                .location("/djao/dhau")
                .name("Billede navn")
                .build();
    }

    public static VisualMediaEntity createVisualMediaEntityWithTags() {
        return VisualMediaEntity.builder()
                .description("dkaoidkao test descpt")
                .fileType("jpg")
                // .lastDateModified("30/10/2003")
                .location("/djao/dhau")
                .name("Billede navn")
                .tags(createTagEntitySet())
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

    public static HashSet<DisplayDeviceEntity> createDisplayDeviceWithOnlyId() {
        HashSet<DisplayDeviceEntity> displayDevices = new HashSet<>();
        displayDevices.add(
            DisplayDeviceEntity.builder().id(1).build()
        );
        return displayDevices;
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
                .displayDevices(new HashSet<>())
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
                .location("Aalborg")
                .name("Skærm Esbjerg1")
                .resolution("1920x1080")
                .build();
        displayDevices.add(dd);
        return displayDevices;
    }

    public static TimeSlotEntity createTimeSlotEntityWithCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = now.minusMinutes(2);
        LocalDateTime endDateTime = now.plusMinutes(2);

        return TimeSlotEntity.builder()
                .name("Test2 TimeSlot")
                .startDate(Date.valueOf(startDateTime.toLocalDate()))
                .endDate(Date.valueOf(endDateTime.toLocalDate()))
                .startTime(Time.valueOf(startDateTime.toLocalTime()))
                .endTime(Time.valueOf(endDateTime.toLocalTime()))
                .weekdaysChosen(127)
                .displayContent(assignedSlideshow())
                .displayDevices(assignDisplayDevice())
                .build();
    }

    public static MockMultipartFile createVisualMediaFile() {
        return new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Fake JPEG file content".getBytes()
        );
    }


    public static VisualMediaInclusionDto createVisualMediaInclusionDto() {
        return VisualMediaInclusionDto.builder()
                .visualMedia(new VisualMediaEntity())
                .slideDuration(5)
                .slideshowPosition(1)
                .build();
    }

    public static VisualMediaInclusionDto createVisualMediaInclusionDtoWitVMThaOnlyContainsId(Integer id) {
        VisualMediaEntity vm = new VisualMediaEntity();
        vm.setId(id);

        return VisualMediaInclusionDto.builder()
                .visualMedia(vm)
                .slideDuration(5)
                .slideshowPosition(1)
                .build();
    }

    public static VisualMediaInclusionEntity createVisualMediaInclusionWithVisualMediaEntity() {
        return VisualMediaInclusionEntity.builder()
                .visualMedia(createVisualMediaEntity())
                .id(1)
                .slideDuration(10)
                .slideshowPosition(2)
                .build();
    }

    public static VisualMediaInclusionEntity createVisualMediaInclusionEntity() {
        return VisualMediaInclusionEntity.builder()
                .visualMedia(null)
                .id(1)
                .slideDuration(10)
                .slideshowPosition(2)
                .build();
    }

    public static SlideshowDto createSlideshowDto() {
        return SlideshowDto.builder()
                .name("slideshow 1")
                .isArchived(false)
                .build();
    }

    public static SlideshowEntity createSlideshowEntity() {
        return SlideshowEntity.builder()
                .name("testSS")
                .isArchived(false)
                .visualMediaInclusionCollection(null)
                .build();
    }

    public static TagEntity createTagEntity() {
        return TagEntity.builder()
                .text("Cool image")
                .build();
    }

    public static TagEntity createTagEntity2() {
        return TagEntity.builder()
                .text("Cool image")
                .build();
    }

    public static Set<TagEntity> createTagEntitySet() {
        Set<TagEntity> tag = new HashSet<>(); 

        tag.add(createTagEntity());
        tag.add(createTagEntity2());

        return tag;         
    }

    public static SlideshowEntity createSlideshowWithVisualMediaEntity() {

        HashSet<VisualMediaInclusionEntity> visualMediaInclusionEntities = new HashSet<>();
        visualMediaInclusionEntities.add(createVisualMediaInclusionEntity());

        return SlideshowEntity.builder()
                .name("testSS")
                .isArchived(false)
                .visualMediaInclusionCollection(visualMediaInclusionEntities)
                .build();
    }
}
