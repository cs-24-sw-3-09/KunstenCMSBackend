package com.github.cs_24_sw_3_09.CMS.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration.DslContextConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.TimeSlotRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TimeSlotControllerIntegrationTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TimeSlotService timeSlotService;
    private DisplayDeviceRepository displayDeviceRepository;
    private DisplayDeviceService displayDeviceService;
    private VisualMediaRepository visualMediaRepository;
    private SlideshowRepository slideshowRepository;
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    public void TimeSlotControllerIntegration(
            MockMvc mockMvc, ObjectMapper objectMapper,
            TimeSlotService timeSlotService, DisplayDeviceRepository displayDeviceRepository,
            DisplayDeviceService displayDeviceService, VisualMediaRepository visualMediaRepository,
            SlideshowRepository slideshowRepository, TimeSlotRepository timeSlotRepository
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.timeSlotService = timeSlotService;
        this.displayDeviceRepository = displayDeviceRepository;
        this.displayDeviceService = displayDeviceService;
        this.visualMediaRepository = visualMediaRepository;
        this.slideshowRepository = slideshowRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatCreateTimeSlotSuccessfullyReturnsHttp201Created() throws Exception {
        TimeSlotDto timeSlotDto = TestDataUtil.createTimeSlotDto();
        String timeSlotJson = objectMapper.writeValueAsString(timeSlotDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/time_slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(timeSlotJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser
    public void testThatGetTimeSlotsSuccessfullyReturnsHttp200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/time_slots")
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testThatGetTimeSlotsSuccessfullyReturnsListOfTimeSlots() throws Exception {
        TimeSlotEntity testTimeSlotEntity = TestDataUtil.createTimeSlotEntity();
        timeSlotService.save(testTimeSlotEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/time_slots")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].startDate").value(testTimeSlotEntity.getStartDate().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].startTime").value(testTimeSlotEntity.getStartTime().toString())
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testThatGetTimeSlotsBasedOnTimeFrame() throws Exception {
        TimeSlotEntity ts = TestDataUtil.createTimeSlotEntity();
        ts.setStartDate(Date.valueOf(LocalDate.of(2024, 11, 25)));
        ts.setEndDate(Date.valueOf(LocalDate.of(2025, 11, 25)));
        timeSlotService.save(ts);
        ts = TestDataUtil.createTimeSlotEntity();
        ts.setStartDate(Date.valueOf(LocalDate.of(2024, 12, 2)));
        ts.setEndDate(Date.valueOf(LocalDate.of(2025, 11, 25)));
        timeSlotService.save(ts);
        ts = TestDataUtil.createTimeSlotEntity();
        ts.setStartDate(Date.valueOf(LocalDate.of(2025, 2, 25)));
        ts.setEndDate(Date.valueOf(LocalDate.of(2025, 11, 25)));
        timeSlotService.save(ts);
        ts = TestDataUtil.createTimeSlotEntity();
        ts.setStartDate(Date.valueOf(LocalDate.of(2024, 9, 25)));
        ts.setEndDate(Date.valueOf(LocalDate.of(2024, 12, 2)));
        timeSlotService.save(ts);
        ts = TestDataUtil.createTimeSlotEntity();
        ts.setStartDate(Date.valueOf(LocalDate.of(2024, 12, 1)));
        ts.setEndDate(Date.valueOf(LocalDate.of(2024, 12, 2)));
        timeSlotService.save(ts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/time_slots?start=2024-11-30&end=2024-12-04"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("numberOfElements").value(4))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());

        MockMvcResultMatchers.status().isOk();
    }

    @Test
    @WithMockUser
    public void testThatGetTimeSlotReturnsStatus200WhenTimeSlotsExists() throws Exception {
        TimeSlotEntity testTimeSlotEntity = TestDataUtil.createTimeSlotEntity();
        timeSlotService.save(testTimeSlotEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/time_slots/1")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("startDate").value(testTimeSlotEntity.getStartDate().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("startTime").value(testTimeSlotEntity.getStartTime().toString())
        );
    }


    @Test
    @WithMockUser
    public void testThatGetTimeSlotAlsoReturnsDisplayDevicesAndDisplayContent() throws Exception {
        TimeSlotEntity testTimeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity s = timeSlotService.save(testTimeSlotEntity).get();


        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/time_slots/1")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("displayContent").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("displayContent.name").value("test1")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("displayDevices").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("displayDevices[0].name").value("Skærm Esbjerg1")
        );
    }

    @Test
    @WithMockUser
    public void testThatGetTimeSlotReturnsStatus404WhenNoTimeSlotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/time_slots/100000")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeleteTimeSlotReturnsStatus204() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity).get();
        assertTrue(timeSlotService.isExists((long) 1));
        assertTrue(displayDeviceService.isExists((long) 1));

        TimeSlotEntity tsToCompare = timeSlotService.findOne((long) 1).get();
        assertNotEquals(
                null,
                tsToCompare.getDisplayDevices()
        );
        assertNotEquals(
                null,
                tsToCompare.getDisplayContent()
        );

        Long id = Long.valueOf(savedTimeSlotEntitiy.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/time_slots/" + savedTimeSlotEntitiy.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        assertFalse(timeSlotService.isExists(id));
        assertTrue(
                displayDeviceService.findOne((long) 1).get()
                        .getTimeSlots().stream()
                        .noneMatch(timeSlot -> timeSlotService.isExists((long) timeSlot.getId()))
        );
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeleteTimeSlotReturnsStatus404() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/time_slots/99")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatFullUpdateTimeSlotReturnsStatus404WhenNoTimeSlotExists() throws Exception {
        TimeSlotDto timeSlotDto = TestDataUtil.createTimeSlotDto();
        String timeDtoJson = objectMapper.writeValueAsString(timeSlotDto);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/time_slots/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatFullUpdateTimeSlotReturnsStatus200WhenTimeSlotExists() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity).get();

        TimeSlotDto timeSlotDto = TestDataUtil.createTimeSlotDto();
        String timeSlotDtoToJson = objectMapper.writeValueAsString(timeSlotDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/time_slots/" + savedTimeSlotEntitiy.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeSlotDtoToJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatPatchUpdateTimeSlotReturnsStatus200() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity).get();

        TimeSlotDto timeSlotDto = TestDataUtil.createTimeSlotDto();
        String timeSlotDtoToJson = objectMapper.writeValueAsString(timeSlotDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/time_slots/" + savedTimeSlotEntitiy.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeSlotDtoToJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(timeSlotDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.startDate").value(timeSlotDto.getStartDate().toString())
        );
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatPatchUpdateTimeSlotReturnsStatus404() throws Exception {
        TimeSlotDto timeSlotDto = TestDataUtil.createTimeSlotDto();
        String timeSlotDtoToJson = objectMapper.writeValueAsString(timeSlotDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/time_slots/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeSlotDtoToJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeletesAssociationBetweenTSAndDDWithMoreAssociationsThanOne() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        timeSlotEntity.getDisplayDevices().add(TestDataUtil.createDisplayDeviceEntity());
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity).get();
        assertTrue(timeSlotService.isExists((long) 1));
        assertTrue(timeSlotEntity.getDisplayDevices().stream().allMatch(
                displayDevice -> displayDeviceService.isExists(displayDevice.getId().longValue())
        ));
        assertNotEquals(timeSlotEntity.getDisplayDevices(), null);
        assertEquals(2, timeSlotService.countDisplayDeviceAssociations((long) 1));

        Integer ddId = savedTimeSlotEntitiy.getDisplayDevices().toArray(new DisplayDeviceEntity[0])[0].getId();
        String body = "{\"ddId\":" + ddId + "}";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/time_slots/" + savedTimeSlotEntitiy.getId() + "/display_devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        Long tsId = Long.valueOf(savedTimeSlotEntitiy.getId());
        assertTrue(timeSlotService.isExists(tsId));
        for (DisplayDeviceEntity dd : timeSlotService.findOne(tsId).get().getDisplayDevices()) {
            assertNotEquals(ddId, dd.getId());
        }

        assertEquals(1, timeSlotService.countDisplayDeviceAssociations((long) 1));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeletesAssociationBetweenTSAndDDWithOnlyOneAssociation() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity).get();
        assertTrue(timeSlotService.isExists((long) 1));
        assertNotEquals(timeSlotEntity.getDisplayDevices(), null);
        assertEquals(1, timeSlotService.countDisplayDeviceAssociations((long) 1));
        assertTrue(displayDeviceService.isExists((long) 1));

        String body = "{\"ddId\": 1 }";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/time_slots/" + savedTimeSlotEntitiy.getId() + "/display_devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        assertFalse(timeSlotService.isExists((long) 1));
    }


    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatTriesToDeleteAssociationButDoesntContainAssociation() throws Exception {
        //Where Display Device does not exist
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity).get();
        assertTrue(timeSlotService.isExists((long) 1));
        assertNotEquals(timeSlotEntity.getDisplayDevices(), null);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/time_slots/" + savedTimeSlotEntitiy.getId() + "/display_devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ddId\":500}")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

        //Where Time Slot Does not exist
        Integer ddId = savedTimeSlotEntitiy.getDisplayDevices().toArray(new DisplayDeviceEntity[0])[0].getId();
        String json = "{\"ddId\":" + ddId + "}";
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/time_slots/500/display_devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test

    @WithMockUser(roles = {"PLANNER"})
    public void testThatUploadesTimeSlotWithDisplayDeviceThatOnlyHasId() throws Exception {

        DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity displayDeviceEntity = displayDeviceRepository.save(displayDeviceToSave);

        String timeSlot = "{"
                + "\"name\": \"Time slot Example\","
                + "\"startDate\": \"2024-11-25\","
                + "\"endDate\": \"2024-11-26\","
                + "\"startTime\": \"12:00:00\","
                + "\"endTime\": \"16:00:00\","
                + "\"weekdaysChosen\": 1,"
                + "\"displayDevices\": [{\"id\": 1}]"
                + "}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/time_slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeSlot)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        assertTrue(timeSlotService.isExists((long) 1));

        TimeSlotEntity timeSlotEntity = timeSlotService.findOne((long) 1).get();

        assertEquals(
                timeSlotEntity.getDisplayDevices().toArray(new DisplayDeviceEntity[0])[0].getId(),
                displayDeviceEntity.getId()
        );

        assertTrue(timeSlotEntity.getDisplayDevices().stream().allMatch(displayDevice ->
                displayDeviceService.isExists((long) displayDevice.getId())
        ));
    }

    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void testThatTriesToUploadTimeSlotButReturns404() throws Exception {

        String timeSlot = "{"
                + "\"name\": \"Time slot Example\","
                + "\"startDate\": \"2024-11-25\","
                + "\"endDate\": \"2024-11-26\","
                + "\"startTime\": \"12:00:00\","
                + "\"endTime\": \"16:00:00\","
                + "\"weekdaysChosen\": 1,"
                + "\"displayDevices\": [{\"id\": 1}]"
                + "}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/time_slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeSlot)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

        assertFalse(timeSlotService.isExists((long) 1));
    }

    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void testThatUploadesTimeSlotWithDisplayDevicesThatOnlyHasId() throws Exception {

        DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceRepository.save(displayDeviceToSave);

        DisplayDeviceEntity displayDevice2ToSave = TestDataUtil.createSecDisplayDeviceEntity();
        displayDeviceRepository.save(displayDevice2ToSave);

        String timeSlot = "{"
                + "\"name\": \"Time slot Example\","
                + "\"startDate\": \"2024-11-25\","
                + "\"endDate\": \"2024-11-26\","
                + "\"startTime\": \"12:00:00\","
                + "\"endTime\": \"16:00:00\","
                + "\"weekdaysChosen\": 1,"
                + "\"displayDevices\": ["
                + "{\"id\": 1},"
                + "{\"id\": 2}"
                + "]"
                + "}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/time_slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeSlot)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        assertTrue(timeSlotService.isExists((long) 1));

        TimeSlotEntity timeSlotEntity = timeSlotService.findOne((long) 1).get();

        assertTrue(timeSlotEntity.getDisplayDevices().stream().allMatch(displayDevice ->
                displayDeviceService.isExists((long) displayDevice.getId())
        ));
    }

    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void testThatUploadesTimeSlotWithVisualMediaThatOnlyHasId() throws Exception {
        //Create a Display Device and visual media such that the id exists
        DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceRepository.save(displayDeviceToSave);
        assertTrue(displayDeviceRepository.findById(1).isPresent());
        VisualMediaEntity visualMediaToSave = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity visualMediaToCompare = visualMediaRepository.save(visualMediaToSave);
        assertTrue(visualMediaRepository.findById(1).isPresent());

        String timeSlot =
                "{"
                        + "\"name\": \"Time slot Example\","
                        + "\"startDate\": \"2024-11-25\","
                        + "\"endDate\": \"2024-11-26\","
                        + "\"startTime\": \"12:00:00\","
                        + "\"endTime\": \"16:00:00\","
                        + "\"weekdaysChosen\": 1,"
                        + "\"displayContent\": {"
                        + "\"id\": 1,"
                        + "\"type\": \"visualMedia\""
                        + "},"
                        + "\"displayDevices\": ["
                        + "{\"id\": 1}"
                        + "]"
                        + "}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/time_slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeSlot)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        assertTrue(timeSlotService.isExists((long) 1));

        TimeSlotEntity timeSlotEntity = timeSlotService.findOne((long) 1).get();

        assertEquals(
                timeSlotEntity.getDisplayContent().getId(),
                visualMediaToCompare.getId()
        );
    }

    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void testThatUploadesTimeSlotWithSlideShowThatOnlyHasId() throws Exception {
        //Create a Display Device and visual media such that the id exists
        DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceRepository.save(displayDeviceToSave);
        SlideshowEntity slideshowToSave = TestDataUtil.createSlideshowEntity();
        SlideshowEntity slideshowToCompare = slideshowRepository.save(slideshowToSave);
        assertTrue(displayDeviceRepository.findById(1).isPresent());
        assertTrue(slideshowRepository.findById(1).isPresent());

        String timeSlot =
                "{"
                        + "\"name\": \"Time slot Example\","
                        + "\"startDate\": \"2024-11-25\","
                        + "\"endDate\": \"2024-11-26\","
                        + "\"startTime\": \"12:00:00\","
                        + "\"endTime\": \"16:00:00\","
                        + "\"weekdaysChosen\": 1,"
                        + "\"displayContent\": {"
                        + "\"id\": 1,"
                        + "\"type\": \"slideshow\""
                        + "},"
                        + "\"displayDevices\": ["
                        + "{\"id\": 1}"
                        + "]"
                        + "}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/time_slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeSlot)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

        assertTrue(timeSlotService.isExists((long) 1));

        TimeSlotEntity timeSlotEntity = timeSlotService.findOne((long) 1).get();

        assertEquals(
                timeSlotEntity.getDisplayContent().getId(),
                slideshowToCompare.getId()
        );
    }

    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void testThatUploadesTimeSlotWithInvalidVisualMediaAndReturns404() throws Exception {
        //Create a Display Device that the id exists
        DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceRepository.save(displayDeviceToSave);
        assertTrue(displayDeviceRepository.findById(1).isPresent());

        String timeSlot =
                "{"
                        + "\"name\": \"Time slot Example\","
                        + "\"startDate\": \"2024-11-25\","
                        + "\"endDate\": \"2024-11-26\","
                        + "\"startTime\": \"12:00:00\","
                        + "\"endTime\": \"16:00:00\","
                        + "\"weekdaysChosen\": 1,"
                        + "\"displayContent\": {"
                        + "\"id\": 1,"
                        + "\"type\": \"slideshow\""
                        + "},"
                        + "\"displayDevices\": ["
                        + "{\"id\": 1}"
                        + "]"
                        + "}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/time_slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeSlot)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

        assertFalse(timeSlotService.isExists((long) 1));
    }

    @Test
    public void testThatChecksIfTimeSlotsOverlap() throws Exception {

        //No weekdays overlap
        TimeSlotEntity ts1 = TestDataUtil.createTimeSlotEntityFromData(85, "2024-12-01",
                "2024-12-25", "12:00:00", "14:00:00");

        TimeSlotEntity ts2 = TestDataUtil.createTimeSlotEntityFromData(42, "2024-12-01",
                "2024-12-25", "12:00:00", "14:00:00");
        assertFalse(ts1.overlaps(ts2));

        //Period does not overlap
        ts1 = TestDataUtil.createTimeSlotEntityFromData(127, "2024-12-01",
                "2024-12-12", "12:00:00", "14:00:00");
        ts2 = TestDataUtil.createTimeSlotEntityFromData(127, "2024-12-13",
                "2024-12-25", "12:00:00", "14:00:00");
        assertFalse(ts1.overlaps(ts2));

        //No time within a day overlap
        ts1 = TestDataUtil.createTimeSlotEntityFromData(127, "2024-12-01",
                "2024-12-12", "08:00:00", "12:00:00");
        ts2 = TestDataUtil.createTimeSlotEntityFromData(127, "2024-12-01",
                "2024-12-12", "12:00:00", "19:00:00");
        assertFalse(ts1.overlaps(ts2));

        //Overlaps
        ts1 = TestDataUtil.createTimeSlotEntityFromData(127, "2024-12-01",
                "2024-12-12", "08:00:00", "12:00:00");
        ts2 = TestDataUtil.createTimeSlotEntityFromData(127, "2024-12-01",
                "2024-12-12", "8:00:00", "19:00:00");
        assertTrue(ts1.overlaps(ts2));
    }


    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void testThatGetOverlappingTimeslotsReturnsOverlappingTimeslots() throws Exception {

        DisplayDeviceEntity dd1 = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity dd2 = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity dd3 = TestDataUtil.createDisplayDeviceEntity();

        displayDeviceService.save(dd1).get();
        displayDeviceService.save(dd2).get();
        displayDeviceService.save(dd3).get();

        assertTrue(displayDeviceService.isExists(1L));
        assertTrue(displayDeviceService.isExists(2L));
        assertTrue(displayDeviceService.isExists(3L));

        int[] associations1 = {1, 2, 3};
        addTsHelper(11, "2024-12-02",
                "2024-12-05", "12:00:00", "14:00:00", associations1);
        assertTrue(timeSlotService.isExists(1L));

        int[] associations2 = {1};
        addTsHelper(3, "2024-12-02",
                "2024-12-03", "11:00:00", "13:00:00", associations2);

        int[] associations3 = {2};
        addTsHelper(1, "2024-12-02",
                "2024-12-02", "11:00:00", "15:00:00", associations3);

        int[] associations4 = {2};
        addTsHelper(2, "2024-12-03",
                "2024-12-03", "13:00:00", "15:00:00", associations4);

        int[] associations5 = {2};
        addTsHelper(8, "2024-12-05",
                "2024-12-05", "14:00:00", "15:00:00", associations5);

        int[] associations6 = {3};
        addTsHelper(4, "2024-12-04",
                "2024-12-04", "8:00:00", "15:00:00", associations6);

        int[] associations7 = {3};
        addTsHelper(1, "2024-12-02",
                "2024-12-02", "13:00:00", "13:30:00", associations7);

        assertTrue(timeSlotService.isExists(1L));
        assertTrue(timeSlotService.isExists(2L));
        assertTrue(timeSlotService.isExists(3L));
        assertTrue(timeSlotService.isExists(4L));
        assertTrue(timeSlotService.isExists(5L));
        assertTrue(timeSlotService.isExists(6L));
        assertTrue(timeSlotService.isExists(7L));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/time_slots/1/overlapping_time_slots")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isArray()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(4))
        );
    }

    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void testThatChecksOverlappingFindNotOverlap() throws Exception {
        DisplayDeviceEntity dd1 = TestDataUtil.createDisplayDeviceEntity("a");
        DisplayDeviceEntity dd2 = TestDataUtil.createDisplayDeviceEntity("b");
        DisplayDeviceEntity dd3 = TestDataUtil.createDisplayDeviceEntity("c");
        DisplayDeviceEntity dd4 = TestDataUtil.createDisplayDeviceEntity("d");

        displayDeviceService.save(dd1);
        displayDeviceService.save(dd2);
        displayDeviceService.save(dd3);
        displayDeviceService.save(dd4);

        assertTrue(displayDeviceService.isExists(1L));
        assertTrue(displayDeviceService.isExists(2L));
        assertTrue(displayDeviceService.isExists(3L));
        assertTrue(displayDeviceService.isExists(4L));

        int[] associations1 = {1, 2, 3};
        int[] associations2 = {2, 3};
        int[] associations3 = {1, 2, 4};
        int[] associations4 = {4};

        addTsHelper(
                127, "2024-12-01", "2024-12-25",
                "12:00:00", "17:00:00", associations1
        );
        addTsHelper(
                127, "2024-12-01", "2024-12-25",
                "10:00:00", "12:00:00", associations2
        );
        addTsHelper(
                127, "2024-12-01", "2024-12-25",
                "17:00:00", "19:00:00", associations3
        );
        addTsHelper(
                127, "2024-12-01", "2024-12-25",
                "10:00:00", "19:00:00", associations4
        );

        assertTrue(timeSlotService.isExists(1L));
        assertTrue(timeSlotService.isExists(2L));
        assertTrue(timeSlotService.isExists(3L));
        assertTrue(timeSlotService.isExists(4L));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/time_slots/1/overlapping_time_slots")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isArray()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(0))
        );
    }

    private void addTsHelper(int weekdaysChosen,
                             String startDate, String endDate, String startTime, String endTime, int[] associations) throws Exception {

        TimeSlotEntity ts4 = TestDataUtil.createTimeSlotEntityFromData(weekdaysChosen,
                startDate, endDate, startTime, endTime);
        String json = objectMapper.writeValueAsString(ts4);
        //int[] associationTs4 = {4};
        json = TestDataUtil.createDDJsonWithId(json, associations);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/time_slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void testThatChecksOverlapWhereTimeSlotDoesntExistAndReturns404() throws Exception {

        assertFalse(timeSlotService.isExists(1L));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/time_slots/1/overlapping_time_slots")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );


        assertFalse(timeSlotService.isExists((long) 1));
    }

    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void getAllTimeSlots() throws Exception {
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());

        assertTrue(timeSlotService.isExists(1L));
        assertTrue(timeSlotService.isExists(2L));
        assertTrue(timeSlotService.isExists(3L));
        assertTrue(timeSlotService.isExists(4L));
        assertTrue(timeSlotService.isExists(5L));
        assertTrue(timeSlotService.isExists(6L));
        assertTrue(timeSlotService.isExists(7L));
        assertTrue(timeSlotService.isExists(8L));
        assertTrue(timeSlotService.isExists(9L));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/time_slots/all")
                ).andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.length()").value(9));
    }

    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void getAllTimeSlotsWithNoDevicesInDatabase() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/time_slots/all")
                ).andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(roles = {"PLANNER"})
    public void testThatGetTSColorsReturnColors() throws Exception {
        //timeSlotService.save(TestDataUtil.createTimeSlotEntityFromData(3, "",));
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());
        timeSlotService.save(TestDataUtil.createTimeSlotEntity());

        assertTrue(timeSlotService.isExists(1L));
        assertTrue(timeSlotService.isExists(2L));
        assertTrue(timeSlotService.isExists(3L));
        assertTrue(timeSlotService.isExists(4L));
        assertTrue(timeSlotService.isExists(5L));
        assertTrue(timeSlotService.isExists(6L));
        assertTrue(timeSlotService.isExists(7L));
        assertTrue(timeSlotService.isExists(8L));
        assertTrue(timeSlotService.isExists(9L));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/time_slots/overlapping_time_slots")
                ).andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[3].color").value("red"));
    }

}