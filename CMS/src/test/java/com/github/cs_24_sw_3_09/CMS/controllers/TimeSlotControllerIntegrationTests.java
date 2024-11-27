package com.github.cs_24_sw_3_09.CMS.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TimeSlotControllerIntegrationTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TimeSlotService timeSlotService;

    @Autowired
    public void TimeSlotControllerIntegration(MockMvc mockMvc, ObjectMapper objectMapper, TimeSlotService timeSlotService){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.timeSlotService = timeSlotService;
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
    public void testThatDeleteTimeSlotReturnsStatus204() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity);
		Long id = Long.valueOf(savedTimeSlotEntitiy.getId());

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/time_slots/" + savedTimeSlotEntitiy.getId())
        ).andExpect(
            MockMvcResultMatchers.status().isNoContent()
        );

		assertFalse(timeSlotService.isExists(id));
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
    @WithMockUser(roles="PLANNER")
    public void testThatDeletesAssociationBetweenTSAndDDWithMoreAssociationsThanOne() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        timeSlotEntity.getDisplayDevices().add(TestDataUtil.createDisplayDeviceEntity());

        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity);

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
        
		for(DisplayDeviceEntity dd : timeSlotService.findOne(tsId).get().getDisplayDevices()) {
			assertNotEquals(ddId, dd.getId());
		}
    }

	@Test
    @WithMockUser(roles="PLANNER")
    public void testThatDeletesAssociationBetweenTSAndDDWithOnlyOneAssociation() throws Exception {
        TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        timeSlotEntity.getDisplayDevices().add(TestDataUtil.createDisplayDeviceEntity());

        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity);

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
		assertFalse(timeSlotService.isExists(tsId));
    }


	@Test
    @WithMockUser(roles="PLANNER")
    public void testThatDoesntContainAssociation() throws Exception {
		//Where Display Device does not exist
		TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
        TimeSlotEntity savedTimeSlotEntitiy = timeSlotService.save(timeSlotEntity);
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/time_slots/" + savedTimeSlotEntitiy.getId() + "/display_devices")
			.contentType(MediaType.APPLICATION_JSON)
			.content("{\"ddId\":500}")
        ).andExpect(
            MockMvcResultMatchers.status().isNotFound()
        );

		//Where Time Slot Does not exist
		Integer ddId = savedTimeSlotEntitiy.getDisplayDevices().toArray(new DisplayDeviceEntity[0])[0].getId();
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/time_slots/500/display_devices")
			.contentType(MediaType.APPLICATION_JSON)
			.content("{\"ddId\":" + ddId + "}")
        ).andExpect(
            MockMvcResultMatchers.status().isNotFound()
        );
    }

}
