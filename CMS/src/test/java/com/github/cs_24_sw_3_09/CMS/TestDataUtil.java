package com.github.cs_24_sw_3_09.CMS;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.github.cs_24_sw_3_09.CMS.model.dto.*;
import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class TestDataUtil {

    static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    static DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

    public static DisplayDeviceDto createDisplayDeviceDto() {

        return DisplayDeviceDto.builder()
                .displayOrientation("horizontal")
                .location("Aalborg")
                .name("Skærm Esbjerg1")
                .resolution("1920x1080")
                .timeSlots(new ArrayList<>())
                .build();
    }

    public static DisplayDeviceDto createDisplayDeviceWithVisualMediaDto() {

        return DisplayDeviceDto.builder()
                .displayOrientation("horizontal")
                .location("Aalborg")
                .name("Skærm Esbjerg1")
                .resolution("1920x1080")
                .timeSlots(new ArrayList<>())
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


    public static DisplayDeviceEntity createDisplayDeviceEntity(String name) {
        return DisplayDeviceEntity.builder()
                .displayOrientation("horizontal")
                .location("Esbjerg")
                .name(name)
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
                .timeSlots(new ArrayList<>())
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
                //.lastDateModified("30/10/2003")
                .location("/djao/dhau.jpg")
                .name("Test_name")
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
        .displayContent(null)
        .displayDevices(new HashSet<DisplayDeviceEntity>())
        .build();
    }

    public static TimeSlotDto createTimeSlotDtoWithAssignedContentAndDD(){
        return TimeSlotDto.builder()
        .name("Test1 TimeSlot")
        .startDate(java.sql.Date.valueOf("2024-11-20"))
        .endDate(java.sql.Date.valueOf("2024-11-20"))
        .startTime(java.sql.Time.valueOf("10:20:30"))
        .endTime(java.sql.Time.valueOf("11:21:31"))
        .weekdaysChosen(3)
        .displayContent(assignedSlideshow())
        .displayDevices(assignDisplayDevice())
        .build();
    }

    public static TimeSlotEntity createTimeSlotEntityWithoutContent(){
        return TimeSlotEntity.builder()
        .name("Test2 TimeSlot")
        .startDate(java.sql.Date.valueOf("2025-11-20"))
        .endDate(java.sql.Date.valueOf("2026-11-20"))
        .startTime(java.sql.Time.valueOf("10:20:30"))
        .endTime(java.sql.Time.valueOf("11:21:31"))
        .weekdaysChosen(3)
        .displayDevices(assignDisplayDevice())
        .build();
    }

    public static TimeSlotEntity createTimeSlotEntity() {
        return TimeSlotEntity.builder()
        .name("Test2 TimeSlot")
        .startDate(java.sql.Date.valueOf("2025-11-20"))
        .endDate(java.sql.Date.valueOf("2026-11-20"))
        .startTime(java.sql.Time.valueOf("10:20:30"))
        .endTime(java.sql.Time.valueOf("11:21:31"))
        .weekdaysChosen(3)
        .displayContent(assignedSlideshow())
        .displayDevices(assignDisplayDevice())
        .build();
    }

    public static TimeSlotEntity createTimeSlotEntityWithOnlyId(Integer id) {
        return TimeSlotEntity.builder()
                .id(id)
                .build();
    }


    


    

    public static HashSet<DisplayDeviceEntity> createDisplayDeviceWithOnlyId() {
        HashSet<DisplayDeviceEntity> displayDevices = new HashSet<>();
        displayDevices.add(
                DisplayDeviceEntity.builder().id(1).build()
        );
        return displayDevices;
    }

    public static DisplayDeviceEntity createDisplayDeviceWithOnlyId(Integer id) {
        return DisplayDeviceEntity.builder().id(id).build();
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
                "test-image.jpeg",
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
                .slideDuration(10)
                .slideshowPosition(2)
                .build();
    }

    public static VisualMediaInclusionEntity createVisualMediaInclusionEntityWithPos(Integer pos) {
        return VisualMediaInclusionEntity.builder()
        .slideDuration(10)
        .slideshowPosition(pos)
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

    public static SlideshowEntity createSlideshowWithMultipleVisualMediaEntities() {

        HashSet<VisualMediaInclusionEntity> visualMediaInclusionEntities = new HashSet<>();
        visualMediaInclusionEntities.add(createVisualMediaInclusionEntityWithPos(1));
        visualMediaInclusionEntities.add(createVisualMediaInclusionEntityWithPos(2));
        visualMediaInclusionEntities.add(createVisualMediaInclusionEntityWithPos(3));

        return SlideshowEntity.builder()
                .name("testSS")
                .isArchived(false)
                .visualMediaInclusionCollection(visualMediaInclusionEntities)
                .build();
    }



    public static TimeSlotEntity createTimeSlotEntityFromData(int weekdaysChosen, String startDate, String endDate, String startTime, String endTime) throws ParseException {

        return TimeSlotEntity.builder()
                .name("Hello Darkness my old friend")

                .displayDevices(new HashSet<>())
                .weekdaysChosen(weekdaysChosen)
                .startDate(Date.valueOf(startDate)) // Requires the format yyyy-MM-dd
                .endDate(Date.valueOf(endDate))   // Requires the format yyyy-MM-dd
                .startTime(Time.valueOf(startTime)) // Requires the format HH:mm:ss
                .endTime(Time.valueOf(endTime))   // Requires the format HH:mm:ss
                .build();
    }

    public static TimeSlotEntity createTimeSlotEntityFromData(int weekdaysChosen, String startDate, String endDate, String startTime, String endTime, Set<DisplayDeviceEntity> displayDeviceEntities) throws ParseException {

        return TimeSlotEntity.builder().weekdaysChosen(weekdaysChosen)
                .weekdaysChosen(weekdaysChosen)
                .startDate(Date.valueOf(startDate)) // Requires the format yyyy-MM-dd
                .endDate(Date.valueOf(endDate))   // Requires the format yyyy-MM-dd
                .startTime(Time.valueOf(startTime)) // Requires the format HH:mm:ss
                .endTime(Time.valueOf(endTime))   // Requires the format HH:mm:ss
                .displayDevices(displayDeviceEntities)
                .build();
    }    

    public static String createTSJsonWithDDIds(String json, int[] ids) {
        String res = "\"displayDevices\":[";
        for (int i = 0; i < ids.length; i++) {
            res += "{\"id\":"+ids[i]+"}";

            if (i != ids.length - 1) {
                res += ",";
            }
        }
        res += "]";

        json = json.replace("\"displayDevices\":[]", res);
        return json;
    }

    public static String createDDJsonWithFBCIds(String json, String id, String type) {
        String res = "\"fallbackContent\":{\"id\":"+id+",\"type\":\""+type+"\"}";
        return json.replace("\"fallbackContent\":null", res);
    }

    public static String createTSJsonWithDCIds(String json, String id, String type) {
        String res = "\"displayContent\":{\"id\":"+id+",\"type\":\""+type+"\"}";
        return json.replace("\"displayContent\":null", res);
    }



    public static MockMultipartFile createHorizontalImage() throws IOException {
        int width = 200;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Write the image to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);

        // Create a MockMultipartFile using the image bytes
        MockMultipartFile mockImageFile = new MockMultipartFile(
                "file",                 // Form field name
                "test-image.jpeg",       // File name
                "image/jpg",           // MIME type
                baos.toByteArray()      // File content
        );
        return mockImageFile;
    }

    public static MockMultipartFile createVerticalImage() throws IOException {
        int width = 100;
        int height = 200;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Write the image to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);

        // Create a MockMultipartFile using the image bytes
        MockMultipartFile mockImageFile = new MockMultipartFile(
                "file",                 // Form field name
                "test-image.jpeg",       // File name
                "image/jpg",           // MIME type
                baos.toByteArray()      // File content
        );
        return mockImageFile;
    }

    
}
