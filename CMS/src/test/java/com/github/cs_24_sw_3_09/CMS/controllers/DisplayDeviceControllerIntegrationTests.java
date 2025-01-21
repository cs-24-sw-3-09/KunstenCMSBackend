package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import com.github.cs_24_sw_3_09.CMS.utils.Result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class DisplayDeviceControllerIntegrationTests {
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;
	private DisplayDeviceService displayDeviceService;
	private SlideshowService slideshowService;
	private VisualMediaService visualMediaService;
	private TimeSlotService timeSlotService;

	@Autowired
	public DisplayDeviceControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper,
			DisplayDeviceService displayDeviceService,
			SlideshowService slideshowService,
			VisualMediaService visualMediaService,
			TimeSlotService timeSlotService) {
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
		this.displayDeviceService = displayDeviceService;
		this.slideshowService = slideshowService;
		this.visualMediaService = visualMediaService;
		this.timeSlotService = timeSlotService;
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatCreateDisplayDeviceSuccessfullyReturnsHttp201Created() throws Exception {
		DisplayDeviceDto displayDeviceDto = TestDataUtil.createDisplayDeviceDto();
		String displayDeviceJson = objectMapper.writeValueAsString(displayDeviceDto);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/display_devices")
						.contentType(MediaType.APPLICATION_JSON)
						.content(displayDeviceJson))
				.andExpect(
						MockMvcResultMatchers.status().isCreated());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatCreateDisplayDeviceSuccessfullyReturnsSavedDisplayDevice() throws Exception {
		DisplayDeviceDto displayDeviceEntity = TestDataUtil.createDisplayDeviceDto();
		String displayDeviceJson = objectMapper.writeValueAsString(displayDeviceEntity);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/display_devices")
						.contentType(MediaType.APPLICATION_JSON)
						.content(displayDeviceJson))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.id").isNumber())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.name").value("Skærm Esbjerg1"))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.location").value("Aalborg"));

		assertTrue(displayDeviceService.isExists(1l));
	}

	@Test
	@WithMockUser
	public void testThatGetDisplayDeviceSuccessfullyReturnsHttp200() throws Exception {
		// DisplayDeviceEntity displayDeviceEntity =
		// TestDataUtil.createDisplayDeviceEntity();
		// String displayDeviceJson =
		// objectMapper.writeValueAsString(displayDeviceEntity);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/display_devices")).andExpect(
						MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser
	public void testThatGetDisplayDeviceSuccessfullyReturnsListOfDisplayDevices() throws Exception {
		DisplayDeviceEntity testDisplayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(testDisplayDeviceEntity, true);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/display_devices")).andExpect(
						MockMvcResultMatchers.jsonPath("content.[0].id").isNumber())
				.andExpect(
						MockMvcResultMatchers.jsonPath("content.[0].name").value(testDisplayDeviceEntity.getName()))
				.andExpect(
						MockMvcResultMatchers.jsonPath("content.[0].location")
								.value(testDisplayDeviceEntity.getLocation()));
	}

	@Test
	@WithMockUser
	public void testThatGetDisplayDeviceReturnsStatus200WhenDisplayDeviceExists() throws Exception {
		DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(displayDeviceEntity, true);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/display_devices/1")).andExpect(
						MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser
	public void testThatGetDisplayDeviceReturnsStatus404WhenNoDisplayDeviceExists() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/display_devices/99")).andExpect(
						MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	@WithMockUser
	public void testThatGetDisplayDeviceReturnsDisplayDeviceWhenDisplayDeviceExists() throws Exception {
		DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(displayDeviceEntity, true);

		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/display_devices/" + displayDeviceEntity.getId())).andExpect(
						MockMvcResultMatchers.jsonPath("$.id").isNumber())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.name").value(displayDeviceEntity.getName()))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.location").value(displayDeviceEntity.getLocation()));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatFullUpdateDisplayDeviceReturnsStatus404WhenNoDisplayDeviceExists() throws Exception {
		DisplayDeviceDto displayDeviceDto = TestDataUtil.createDisplayDeviceDto();
		String displayDeviceDtoJson = objectMapper.writeValueAsString(displayDeviceDto);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/api/display_devices/99")
						.contentType(MediaType.APPLICATION_JSON)
						.content(displayDeviceDtoJson))
				.andExpect(
						MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatDeleteDisplayDeviceReturnsStatus204() throws Exception {
		DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(displayDeviceEntity, true);
		assertTrue(displayDeviceService.isExists((long) 1));

		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/display_devices/1")
		).andExpect(
						MockMvcResultMatchers.status().isNoContent()
		);

		assertFalse(displayDeviceService.isExists((long) 1));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatDeleteDisplayDeviceAndTimeSlotWithOnlyOneAssociation() throws Exception {
		//Creates both a display Device and a time slot
		TimeSlotEntity timeSlotToSave = TestDataUtil.createTimeSlotEntity();
		timeSlotService.save(timeSlotToSave, true);

		
		assertTrue(displayDeviceService.isExists((long) 1));
		assertTrue(timeSlotService.isExists((long) 1));

		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getTimeSlots().size()
		);
		assertEquals(
			1, 
			timeSlotService.countDisplayDeviceAssociations((long) 1)
		);

		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/display_devices/1")
		).andExpect(
						MockMvcResultMatchers.status().isNoContent()
		);
		assertFalse(displayDeviceService.isExists((long) 1));
		assertFalse(timeSlotService.isExists((long) 1));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatDeleteDisplayDeviceAndNotTimeSlotWithMoreAssociations() throws Exception {
		//Creates both a display Device and a time slot
		TimeSlotEntity timeSlotToSave = TestDataUtil.createTimeSlotEntity();
		timeSlotToSave.getDisplayDevices().add(TestDataUtil.createDisplayDeviceEntity());
		timeSlotService.save(timeSlotToSave, true);


		
		assertTrue(displayDeviceService.isExists((long) 1));
		assertTrue(timeSlotService.isExists((long) 1));

		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getTimeSlots().size()
		);
		assertEquals(
			2, 
			timeSlotService.countDisplayDeviceAssociations((long) 1)
		);


		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/display_devices/1")
		).andExpect(
						MockMvcResultMatchers.status().isNoContent()
		);
		assertFalse(displayDeviceService.isExists((long) 1));
		assertTrue(timeSlotService.isExists((long) 1));
		assertEquals(
			1, 
			timeSlotService.countDisplayDeviceAssociations((long) 1)
		);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatTryDeleteDisplayDeviceAndReturns404() throws Exception {
		assertFalse(displayDeviceService.isExists((long) 1));
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/display_devices/1")
		).andExpect(
						MockMvcResultMatchers.status().isNotFound()
		);
		assertFalse(displayDeviceService.isExists((long) 1));
	}


	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatDeleteDisplayDeviceReturnsStatus404() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/api/display_devices/99")).andExpect(
						MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatFullUpdateDisplayDeviceReturnsStatus200WhenDisplayDeviceExists() throws Exception {
		DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity, true).getOk();

		DisplayDeviceDto displayDeviceDto = TestDataUtil.createDisplayDeviceDto();
		String displayDeviceDtoJson = objectMapper.writeValueAsString(displayDeviceDto);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/api/display_devices/" + savedDisplayDeviceEntity.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(displayDeviceDtoJson))
				.andExpect(
						MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatPatchUpdateDisplayDeviceReturnsStatus200() throws Exception {
		DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity, true).getOk();

		DisplayDeviceDto displayDeviceDto = TestDataUtil.createDisplayDeviceDto();
		String displayDeviceDtoJson = objectMapper.writeValueAsString(displayDeviceDto);

		mockMvc.perform(
				MockMvcRequestBuilders.patch("/api/display_devices/" + savedDisplayDeviceEntity.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(displayDeviceDtoJson))
				.andExpect(
						MockMvcResultMatchers.status().isOk())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.name").value(displayDeviceDto.getName()))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.location").value(displayDeviceDto.getLocation()));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	public void testThatPatchUpdateDisplayDeviceReturnsStatus404() throws Exception {
		DisplayDeviceDto displayDeviceDto = TestDataUtil.createDisplayDeviceDto();
		String displayDeviceDtoJson = objectMapper.writeValueAsString(displayDeviceDto);

		mockMvc.perform(
				MockMvcRequestBuilders.patch("/api/display_devices/99")
						.contentType(MediaType.APPLICATION_JSON)
						.content(displayDeviceDtoJson))
				.andExpect(
						MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "PLANNER")
	public void testThatSetFallbackContentToSlideShowSetsFallbackContentToSlideShow() throws Exception {
		SlideshowEntity slideshow = TestDataUtil.createSlideshowEntity();
		SlideshowEntity savedSlideshowEntity = slideshowService.save(slideshow);

		DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		String requestBodyJson = "{\"fallbackId\": " + savedSlideshowEntity.getId() + "}";
		DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity, true).getOk();

		mockMvc.perform(
				MockMvcRequestBuilders
						.patch("/api/display_devices/" + savedDisplayDeviceEntity.getId() + "/fallback_content")
						.param("forceDimensions", "true")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBodyJson))
				.andExpect(
						MockMvcResultMatchers.status().isOk())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.fallbackContent.name").value(savedSlideshowEntity.getName()))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.fallbackContent.isArchived")
								.value(savedSlideshowEntity.getIsArchived()));

	}

	@Test
	@WithMockUser(roles = "PLANNER")
	public void testThatSetFallbackContentToSlideShowReturn404WhenSlideShowNotExists() throws Exception {

		DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		String requestBodyJson = "{\"fallbackId\": 99}";
		DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity, true).getOk();

		mockMvc.perform(
				MockMvcRequestBuilders
						.patch("/api/display_devices/" + savedDisplayDeviceEntity.getId() + "/fallbackContent")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBodyJson))
				.andExpect(
						MockMvcResultMatchers.status().isNotFound());

	}

	@Test
	@WithMockUser(roles = "PLANNER")
	public void testThatSetFallbackContentToVisualMediaSetsFallbackContentToVisualMedia() throws Exception {
		VisualMediaEntity visualMedia = TestDataUtil.createVisualMediaEntity();
		VisualMediaEntity savedVisualMedia = visualMediaService.save(visualMedia);

		DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		String requestBodyJson = "{\"fallbackId\": " + savedVisualMedia.getId() + "}";
		DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity, true).getOk();

		mockMvc.perform(
				MockMvcRequestBuilders
						.patch("/api/display_devices/" + savedDisplayDeviceEntity.getId() + "/fallback_content")
						.param("forceDimensions", "true")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBodyJson))
				.andExpect(
						MockMvcResultMatchers.status().isOk())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.fallbackContent.name").value(savedVisualMedia.getName()))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.fallbackContent.location")
								.value(savedVisualMedia.getLocation()))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.fallbackContent.fileType")
								.value(savedVisualMedia.getFileType()));
	}

	@Test
	@WithMockUser(roles = "PLANNER")
	public void testThatSetFallbackContentToVisualMediaReturn404WhenVisualMediaNotExists() throws Exception {

		DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		String requestBodyJson = "{\"fallbackId\": 99}";
		DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity, true).getOk();

		mockMvc.perform(
				MockMvcRequestBuilders
						.patch("/api/display_devices/" + savedDisplayDeviceEntity.getId() + "/fallbackContent")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBodyJson))
				.andExpect(
						MockMvcResultMatchers.status().isNotFound());

	}

	@Test
	@WithMockUser(roles = "PLANNER")
	public void testThatAddTimeSlotToDisplayDeviceReturns200OkayWhenBothExists() throws Exception {
		TimeSlotEntity timeSlotEntity = TestDataUtil.createTimeSlotEntity();
		TimeSlotEntity savedTimeSlotEntity = timeSlotService.save(timeSlotEntity, true).getOk();

		DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
		DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity, true).getOk();

		String requestBodyJson = "{\"timeSlotId\": " + savedTimeSlotEntity.getId() + "}";

		mockMvc.perform(
				MockMvcRequestBuilders.patch("/api/display_devices/" + savedDisplayDeviceEntity.getId() + "/time_slots")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBodyJson))
				.andExpect(
						MockMvcResultMatchers.status().isOk())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.timeSlots[0].id").isNumber())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.timeSlots[0].name").value(savedTimeSlotEntity.getName()))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.timeSlots[0].weekdaysChosen")
								.value(savedTimeSlotEntity.getWeekdaysChosen()));
	}

	@Test
	@WithMockUser(roles = { "ADMIN" })
	public void testThatUploadesDisplayDeviceWithVisualMediaIdAndReturns201() throws Exception {
		VisualMediaEntity visualMediaToSave = TestDataUtil.createVisualMediaEntity();
		visualMediaService.save(visualMediaToSave);
		assertTrue(visualMediaService.isExists((long) 1));

		String fallbackId = 
		"{"
		+ 	"\"type\": \"visualMedia\","
		+ 	"\"id\": 1"
		+"}";

		String displayDeviceJson = 
		"{"
		+ 	"\"name\": \"Display Device Example\","
		+ 	"\"location\": \"Test Location\","
		+ 	"\"displayOrientation\": \"horizontal\","
		+ 	"\"resolution\": \"1920x1080\","
		+ 	"\"fallbackContent\":" 
		+ 	fallbackId
        +"}";

		mockMvc.perform(
                MockMvcRequestBuilders.post("/api/display_devices")
						.param("forceDimensions", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(displayDeviceJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

		assertTrue(displayDeviceService.isExists((long) 1));
		assertTrue(visualMediaService.isExists((long) 1));

		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);
	}
	
	@Test
	@WithMockUser(roles = { "ADMIN" })
	public void testThatUploadesDisplayDeviceWithSlideshowIdAndReturns201() throws Exception {
		SlideshowEntity slideshowToSave = TestDataUtil.createSlideshowEntity();
		slideshowService.save(slideshowToSave);
		assertTrue(slideshowService.isExists((long) 1));

		String fallbackId = 
		"{"
		+ 	"\"type\": \"slideshow\","
		+ 	"\"id\": 1"
		+"}";

		String displayDeviceJson = 
		"{"
		+ 	"\"name\": \"Display Device Example\","
		+ 	"\"location\": \"Test Location\","
		+ 	"\"displayOrientation\": \"horizontal\","
		+ 	"\"resolution\": \"1920x1080\","
		+ 	"\"fallbackContent\":" 
		+ 	fallbackId
        + 	"}";

		mockMvc.perform(
                MockMvcRequestBuilders.post("/api/display_devices")
						.param("forceDimensions", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(displayDeviceJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

		assertTrue(displayDeviceService.isExists((long) 1));
		assertTrue(slideshowService.isExists((long) 1));
		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);
	}

	@Test
	@WithMockUser(roles = { "ADMIN" })
	public void testThatUploadesDisplayDeviceWithInvalidContentIdAndReturns404() throws Exception {
		String fallbackId = 
		"{"
		+ 	"\"type\": \"slideshow\","
		+ 	"\"id\": 1"
		+"}";

		String displayDeviceJson = 
		"{"
		+ 	"\"name\": \"Display Device Example\","
		+ 	"\"location\": \"Test Location\","
		+ 	"\"displayOrientation\": \"horizontal\","
		+ 	"\"resolution\": \"1920x1080\","
		+ 	"\"fallbackContent\":" 
		+ 	fallbackId
        +"}";

		mockMvc.perform(
                MockMvcRequestBuilders.post("/api/display_devices")
						.param("forceDimensions", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(displayDeviceJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
		assertFalse(displayDeviceService.isExists((long) 1));
	}

	@Test
	@WithMockUser(roles = { "PLANNER" })
	public void testThatPatchDisplayDeviceWithVMIdAndReturns200() throws Exception {
		DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(displayDeviceToSave, true).getOk();
		assertTrue(displayDeviceService.isExists((long) 1)); 

		VisualMediaEntity visualMediaToSave = TestDataUtil.createVisualMediaEntity();
		visualMediaService.save(visualMediaToSave);
		assertTrue(visualMediaService.isExists((long) 1));

		String body = "{\"fallbackId\":1,\"type\":\"visualMedia\"}}";

		mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/display_devices/1/fallback_content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
						.param("forceDimensions", "true")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);
	}

	@Test
	@WithMockUser(roles = { "PLANNER" })
	public void testThatPatchDisplayDeviceWithSlideShowIdAndReturns200() throws Exception {
		DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(displayDeviceToSave, true).getOk();
		assertTrue(displayDeviceService.isExists((long) 1)); 

		SlideshowEntity slideshowToSave = TestDataUtil.createSlideshowEntity();
		slideshowService.save(slideshowToSave);
		assertTrue(slideshowService.isExists((long) 1)); 

		String body = "{\"fallbackId\":1,\"type\":\"slideshow\"}";

		mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/display_devices/1/fallback_content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
						.param("forceDimensions", "true")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);
	}

	@Test
	@WithMockUser(roles = { "PLANNER" })
	public void testThatPatchDisplayDeviceWithFallbackIdWhenDDDoesNotExistAndReturns404() throws Exception {
		DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(displayDeviceToSave, true).getOk();
		assertTrue(displayDeviceService.isExists((long) 1)); 

		String body = "{\"fallbackId\":1,\"type\":\"slideshow\"}";

		mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/display_devices/1/fallback_content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );

	}
	
	@Test
	@WithMockUser(roles = { "PLANNER" })
	public void testThatPatchDisplayDeviceWithFallbackIdWhenFallbackDoesNotExistAndReturns404() throws Exception {
		VisualMediaEntity visualMediaToSave = TestDataUtil.createVisualMediaEntity();
		visualMediaService.save(visualMediaToSave);
		assertTrue(visualMediaService.isExists((long) 1));

		String body = "{\"fallbackId\":1,\"type\":\"visualMedia\"}";

		mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/display_devices/1/fallback_content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );


	}

	@Test
	@WithMockUser(roles = { "PLANNER" })
	public void testThatPatchDisplayDeviceWithFallbackIdWhenFallbackIsAlreadyAssigned() throws Exception {
		DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(displayDeviceToSave, true).getOk();
		assertTrue(displayDeviceService.isExists((long) 1)); 

		VisualMediaEntity visualMediaToSave = TestDataUtil.createVisualMediaEntity();
		visualMediaService.save(visualMediaToSave);
		assertTrue(visualMediaService.isExists((long) 1));

		Result<DisplayDeviceEntity, String> dd =
		displayDeviceService.addFallback(displayDeviceToSave.getId().longValue(), visualMediaToSave.getId().longValue(), true);
		
		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);

		SlideshowEntity slideshowToSave = TestDataUtil.createSlideshowEntity();
		slideshowService.save(slideshowToSave);
		assertTrue(slideshowService.isExists((long) 2)); 

		String body = "{\"fallbackId\":2,\"type\":\"slideshow\"}";

		mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/display_devices/1/fallback_content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
						.param("forceDimensions", "true")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

		assertEquals(
			2, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);
	}

	@Test
	@WithMockUser(roles = { "PLANNER", "ADMIN" })
	public void testThatFullPatchDisplayDeviceWhereDisplayDeviceContainsAFallbackId() throws Exception {
		DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(displayDeviceToSave, true).getOk();
		assertTrue(displayDeviceService.isExists((long) 1)); 

		VisualMediaEntity visualMediaToSave = TestDataUtil.createVisualMediaEntity();
		visualMediaService.save(visualMediaToSave);
		assertTrue(visualMediaService.isExists((long) 1));

		displayDeviceService.addFallback(displayDeviceToSave.getId().longValue(), visualMediaToSave.getId().longValue(), true);
		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);

		SlideshowEntity slideshowToSave = TestDataUtil.createSlideshowEntity();
		slideshowService.save(slideshowToSave);
		assertTrue(slideshowService.isExists((long) 2)); 

		DisplayDeviceEntity ddEntity = displayDeviceService.findOne(1L).get();
		ddEntity.setFallbackContent(null);
		String json = objectMapper.writeValueAsString(ddEntity);
		json = TestDataUtil.createDDJsonWithFBCIds(json, "2", "slideshow");

		mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/display_devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.fallbackContent.id").value(2)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.fallbackContent.name").value("testSS")
        );

		assertEquals(
			2, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);
	}
	@Test
	@WithMockUser(roles = { "PLANNER", "ADMIN" })
	public void testThatFullPatchDisplayDeviceWhereDisplayDeviceDoesntContainAFallback() throws Exception {
		DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(displayDeviceToSave, true).getOk();
		assertTrue(displayDeviceService.isExists((long) 1)); 

		VisualMediaEntity visualMediaToSave = TestDataUtil.createVisualMediaEntity();
		visualMediaService.save(visualMediaToSave);
		assertTrue(visualMediaService.isExists((long) 1));

		displayDeviceService.addFallback(displayDeviceToSave.getId().longValue(), visualMediaToSave.getId().longValue(), true);
		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);

		SlideshowEntity slideshowToSave = TestDataUtil.createSlideshowEntity();
		slideshowService.save(slideshowToSave);
		assertTrue(slideshowService.isExists((long) 2)); 

		DisplayDeviceEntity ddEntity = displayDeviceService.findOne(1L).get();
		ddEntity.setFallbackContent(null);
		String json = objectMapper.writeValueAsString(ddEntity);

		mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/display_devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.fallbackContent.id").value(1)
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.fallbackContent.name").value("Test_name")
        );

		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);
	}
	@Test
	@WithMockUser(roles = { "PLANNER", "ADMIN" })
	public void testThatFullPatchDisplayDeviceWhereDisplayDeviceDoesntContainAFallbackId() throws Exception {
		DisplayDeviceEntity displayDeviceToSave = TestDataUtil.createDisplayDeviceEntity();
		displayDeviceService.save(displayDeviceToSave, true).getOk();
		assertTrue(displayDeviceService.isExists(1L)); 

		VisualMediaEntity visualMediaToSave = TestDataUtil.createVisualMediaEntity();
		visualMediaService.save(visualMediaToSave);
		assertTrue(visualMediaService.isExists(1L));

		displayDeviceService.addFallback(displayDeviceToSave.getId().longValue(), visualMediaToSave.getId().longValue(), true);
		assertEquals(
			1, 
			displayDeviceService.findOne((long) 1).get().getFallbackContent().getId()
		);

		SlideshowEntity slideshowToSave = TestDataUtil.createSlideshowEntity();
		slideshowService.save(slideshowToSave);
		assertTrue(slideshowService.isExists(2L)); 

		DisplayDeviceEntity ddEntity = displayDeviceService.findOne(1L).get();
		ddEntity.setFallbackContent(null);
		String json = objectMapper.writeValueAsString(ddEntity);
		json = TestDataUtil.createDDJsonWithFBCIds(json, "0", "visualMedia");
		mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/display_devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

		//Should save a new fallback
		assertEquals(
			null,
			displayDeviceService.findOne((long) 1).get().getFallbackContent()
		);
	}

	


	@WithMockUser(roles = { "PLANNER" })
	public void getAllDisplayDevices() throws Exception {
		displayDeviceService.save(TestDataUtil.createDisplayDeviceEntity(), true);
		displayDeviceService.save(TestDataUtil.createDisplayDeviceEntity(), true);
		displayDeviceService.save(TestDataUtil.createDisplayDeviceEntity(), true);
		displayDeviceService.save(TestDataUtil.createDisplayDeviceEntity(), true);
		displayDeviceService.save(TestDataUtil.createDisplayDeviceEntity(), true);
		displayDeviceService.save(TestDataUtil.createDisplayDeviceEntity(), true);
		displayDeviceService.save(TestDataUtil.createDisplayDeviceEntity(), true);
		displayDeviceService.save(TestDataUtil.createDisplayDeviceEntity(), true);
		displayDeviceService.save(TestDataUtil.createDisplayDeviceEntity(), true);

		assertTrue(displayDeviceService.isExists(1L));
		assertTrue(displayDeviceService.isExists(2L));
		assertTrue(displayDeviceService.isExists(3L));
		assertTrue(displayDeviceService.isExists(4L));
		assertTrue(displayDeviceService.isExists(5L));
		assertTrue(displayDeviceService.isExists(6L));
		assertTrue(displayDeviceService.isExists(7L));
		assertTrue(displayDeviceService.isExists(8L));
		assertTrue(displayDeviceService.isExists(9L));
		
		mockMvc.perform(
                MockMvcRequestBuilders.get("/api/display_devices/all")
        ).andExpect(
			MockMvcResultMatchers.status().isOk())
		.andExpect(
			MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(
			MockMvcResultMatchers.jsonPath("$.length()").value(9));
	}

	@Test
	@WithMockUser(roles = { "PLANNER" })
	public void getAllDisplayDevicesWithNoDevicesInDatabase() throws Exception {
		mockMvc.perform(
                MockMvcRequestBuilders.get("/api/display_devices/all")
        ).andExpect(
			MockMvcResultMatchers.status().isOk())
		.andExpect(
			MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(
			MockMvcResultMatchers.jsonPath("$.length()").value(0));
	}

}
