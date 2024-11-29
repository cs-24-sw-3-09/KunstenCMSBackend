package com.github.cs_24_sw_3_09.CMS.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;

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
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;

import ch.qos.logback.core.net.SyslogOutputStream;

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

    @Autowired
    public void TimeSlotControllerIntegration(
        MockMvc mockMvc, ObjectMapper objectMapper, 
        TimeSlotService timeSlotService, DisplayDeviceRepository displayDeviceRepository,
		DisplayDeviceService displayDeviceService, VisualMediaRepository visualMediaRepository,
		SlideshowRepository slideshowRepository
	){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.timeSlotService = timeSlotService;
        this.displayDeviceRepository = displayDeviceRepository;
		this.displayDeviceService = displayDeviceService;
		this.visualMediaRepository = visualMediaRepository;
		this.slideshowRepository = slideshowRepository;
    }

    @Test
    @WithMockUser(roles="PLANNER")
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
    public void testThatGetTimeSlotsSuccessfullyReturnsListOfTimeSlots() throws Exception{
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
    public void testThatGetTimeSlotAlsoReturnsDisplayDevicesAndDisplayContent() throws Exception{
        TimeSlotEntity testTimeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity s = timeSlotService.save(testTimeSlotEntity);
        System.out.println(s.getDisplayDevices().toArray()[0]);


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
    @WithMockUser(roles="PLANNER")
    public void testThatDeleteTimeSlotReturnsStatus200() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/time_slots/" + savedTimeSlotEntitiy.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDeleteTimeSlotReturnsStatus404() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/time_slots/99")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(roles="PLANNER")
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
    @WithMockUser(roles="PLANNER")
    public void testThatFullUpdateTimeSlotReturnsStatus200WhenTimeSlotExists() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity);

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
    @WithMockUser(roles="PLANNER")
    public void testThatPatchUpdateTimeSlotReturnsStatus200() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity);

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
    @WithMockUser(roles="PLANNER")
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
    @WithMockUser(roles={"PLANNER"}) 
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
        +"}";

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
    @WithMockUser(roles={"PLANNER"}) 
    public void testThatTriesToUploadTimeSlotButReturns404() throws Exception {

        String timeSlot = "{"
        + "\"name\": \"Time slot Example\","
        + "\"startDate\": \"2024-11-25\","
        + "\"endDate\": \"2024-11-26\","
        + "\"startTime\": \"12:00:00\","
        + "\"endTime\": \"16:00:00\","
        + "\"weekdaysChosen\": 1,"
        + "\"displayDevices\": [{\"id\": 1}]"
        +"}";

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
    @WithMockUser(roles={"PLANNER"}) 
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
		+"{\"id\": 1},"
		+"{\"id\": 2}"
		+"]"
        +"}";


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
    @WithMockUser(roles={"PLANNER"}) 
    public void testThatUploadesTimeSlotWithVisualMediaThatOnlyHasId() throws Exception {
		//Create a Display Device and visual media such that the id exists
		DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceRepository.save(displayDeviceToSave);
		VisualMediaEntity visualMediaToSave = TestDataUtil.createVisualMediaEntity();
		VisualMediaEntity visualMediaToCompare = visualMediaRepository.save(visualMediaToSave);
		assertTrue(displayDeviceRepository.findById(1).isPresent());
		assertTrue(visualMediaRepository.findById(1).isPresent());

		String timeSlot = 
		"{"
		+ 	"\"name\": \"Time slot Example\","
		+ 	"\"startDate\": \"2024-11-25\","
		+ 	"\"endDate\": \"2024-11-26\","
		+ 	"\"startTime\": \"12:00:00\","
		+ 	"\"endTime\": \"16:00:00\","
		+ 	"\"weekdaysChosen\": 1,"
		+ 	"\"displayContent\": {"
		+ 		"\"id\": 1" 
		+	"},"
		+ 	"\"displayDevices\": [" 
				+"{\"id\": 1}"
			+"]"
        +"}";

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
    @WithMockUser(roles={"PLANNER"}) 
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
		+ 	"\"name\": \"Time slot Example\","
		+ 	"\"startDate\": \"2024-11-25\","
		+ 	"\"endDate\": \"2024-11-26\","
		+ 	"\"startTime\": \"12:00:00\","
		+ 	"\"endTime\": \"16:00:00\","
		+ 	"\"weekdaysChosen\": 1,"
		+ 	"\"displayContent\": {"
		+ 		"\"id\": 1" 
		+	"},"
		+ 	"\"displayDevices\": [" 
				+"{\"id\": 1}"
			+"]"
        +"}";

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
    @WithMockUser(roles={"PLANNER"}) 
    public void testThatUploadesTimeSlotWithInvalidVisualMediaAndReturns404() throws Exception {
		//Create a Display Device that the id exists
		DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceRepository.save(displayDeviceToSave);
		assertTrue(displayDeviceRepository.findById(1).isPresent());

		String timeSlot = 
		"{"
		+ 	"\"name\": \"Time slot Example\","
		+ 	"\"startDate\": \"2024-11-25\","
		+ 	"\"endDate\": \"2024-11-26\","
		+ 	"\"startTime\": \"12:00:00\","
		+ 	"\"endTime\": \"16:00:00\","
		+ 	"\"weekdaysChosen\": 1,"
		+ 	"\"displayContent\": {"
		+ 		"\"id\": 1" 
		+	"},"
		+ 	"\"displayDevices\": [" 
				+"{\"id\": 1}"
			+"]"
        +"}";

		mockMvc.perform(
                MockMvcRequestBuilders.post("/api/time_slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(timeSlot)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

		assertFalse(timeSlotService.isExists((long) 1));
	}
}