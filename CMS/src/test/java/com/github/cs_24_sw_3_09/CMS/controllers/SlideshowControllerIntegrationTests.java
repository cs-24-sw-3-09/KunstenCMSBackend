package com.github.cs_24_sw_3_09.CMS.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.SlideshowDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class SlideshowControllerIntegrationTests {


    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private SlideshowService slideshowService;
    private VisualMediaInclusionService visualMediaInclusionService;
    private TimeSlotService timeSlotService;
    private VisualMediaRepository visualMediaRepository;

    @Autowired
    public SlideshowControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, SlideshowService slideshowService,
                                               VisualMediaInclusionService visualMediaInclusionService, TimeSlotService timeSlotService,
                                               VisualMediaRepository visualMediaRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.slideshowService = slideshowService;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.timeSlotService = timeSlotService;
        this.visualMediaRepository = visualMediaRepository;
    }


    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCreateSlideshowReturnsHttpStatus201Created() throws Exception {
        SlideshowDto slideshow = TestDataUtil.createSlideshowDto();
        String slideshowJson = objectMapper.writeValueAsString(slideshow);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/slideshows") // Use multipart request
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(slideshowJson)                                  // Attach the file
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCreateSlideshowReturnsCreatedSlideshow() throws Exception {
        SlideshowDto slideshow = TestDataUtil.createSlideshowDto();
        String slideshowJson = objectMapper.writeValueAsString(slideshow);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/slideshows")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(slideshowJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(slideshow.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isArchived").value(slideshow.getIsArchived()));

    }

    @Test
    @WithMockUser
    public void testThatGetSlideshowSuccessfullyReturnsHttp200() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/slideshows")).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testThatGetSlideshowsSuccessfullyReturnsListOfSlideshows() throws Exception {
        SlideshowEntity testSlideshowEntity = TestDataUtil.createSlideshowEntity();
        System.out.println(slideshowService.save(testSlideshowEntity));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/slideshows"))
                        .andExpect(
                                        MockMvcResultMatchers.jsonPath("[0].id").isNumber())
                        .andExpect(
                                        MockMvcResultMatchers.jsonPath("[0].name").value(testSlideshowEntity.getName()))
                        .andExpect(
                                        MockMvcResultMatchers.jsonPath("[0].isArchived")
                                .value(testSlideshowEntity.getIsArchived()));
    }

    @Test
    @WithMockUser
    public void testThatGetSlideshowReturnsStatus200WhenExists() throws Exception {
        SlideshowEntity slideshowEntity = TestDataUtil.createSlideshowEntity();
        slideshowService.save(slideshowEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/slideshows/1")).andExpect(
                MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser
    public void testThatGetSlideshowReturnsStatus404WhenNoSlideshowExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/slideshows/99")).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testThatGetSlideshowReturnsSlideshowWhenSlideshowExists() throws Exception {
        SlideshowEntity slideshow = TestDataUtil.createSlideshowEntity();
        slideshowService.save(slideshow);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/slideshows/" + slideshow.getId())).andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(slideshow.getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isArchived").value(slideshow.getIsArchived()));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatFullUpdateSlideshowReturnsStatus404WhenNoSlideshowExists() throws Exception {
        SlideshowDto slideshowDto = TestDataUtil.createSlideshowDto();
        String slideshowDtoJson = objectMapper.writeValueAsString(slideshowDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/slideshows/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(slideshowDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDeleteSlideshowReturnsStatus200() throws Exception {
        SlideshowEntity slideshowEntity = TestDataUtil.createSlideshowEntity();
        SlideshowEntity savedSlideshowEntity = slideshowService.save(slideshowEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/slideshows/" + savedSlideshowEntity.getId())).andExpect(
                MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDeleteSlideshowReturnsStatus404() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/slideshows/99")).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatFullUpdateSlideshowReturnsStatus200WhenSlideshowExists() throws Exception {
        SlideshowEntity slideshowEntity = TestDataUtil.createSlideshowEntity();
        SlideshowEntity savedSlideshowEntity = slideshowService.save(slideshowEntity);

        SlideshowDto slideshowDto = TestDataUtil.createSlideshowDto();
        String slideshowDtoJson = objectMapper.writeValueAsString(slideshowDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/slideshows/" + savedSlideshowEntity.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(slideshowDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatPatchUpdateSlideshowReturnsStatus200() throws Exception {
        SlideshowEntity slideshowEntity = TestDataUtil.createSlideshowEntity();
        SlideshowEntity savedSlideshowEntity = slideshowService.save(slideshowEntity);

        SlideshowDto slideshowDto = TestDataUtil.createSlideshowDto();
        String slideshowDtoJson = objectMapper.writeValueAsString(slideshowDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/slideshows/" + savedSlideshowEntity.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(slideshowDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(slideshowDto.getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.isArchived").value(slideshowDto.getIsArchived()));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatPatchUpdateSlideshowReturnsStatus404() throws Exception {
        SlideshowDto slideshowDto = TestDataUtil.createSlideshowDto();
        String slideshowDtoJson = objectMapper.writeValueAsString(slideshowDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/slideshows/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(slideshowDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatAddVisualMediaInclusionToSlideShowReturnsSlideshowWithVisualMediaInclusionAdded() throws Exception {
        VisualMediaEntity vm = TestDataUtil.createVisualMediaEntity();
        visualMediaRepository.save(vm);
        assertTrue(visualMediaRepository.findById(1).isPresent());
        
        VisualMediaInclusionEntity visualMediaInclusionEntity = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion = visualMediaInclusionService.save(visualMediaInclusionEntity).get();
        visualMediaInclusionService.setVisualMedia((long) savedVisualMediaInclusion.getId(), 1L);
        assertEquals(
            1,    
            visualMediaInclusionService.findOne(1L).get().getVisualMedia().getId()
        );
        
        SlideshowEntity slideshowEntity = TestDataUtil.createSlideshowEntity();
        SlideshowEntity savedSlideshow = slideshowService.save(slideshowEntity);

        String requestBodyJson = "{\"visualMediaInclusionId\": " + savedVisualMediaInclusion.getId() + "}";

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/slideshows/" + savedSlideshow.getId() + "/visual_media_inclusions?forceDimensions=true")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.visualMediaInclusionCollection[0].slideDuration").value(savedVisualMediaInclusion.getSlideDuration()));

    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDuplicatesSlideshowWithName() throws Exception {
        SlideshowEntity ssToCompare = TestDataUtil.createSlideshowEntity();
        ssToCompare = slideshowService.save(ssToCompare);

        String body = "{\"name\":\"New name\"}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/slideshows/1/duplicate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New name"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.isArchived").value(false));
		
		assertTrue(slideshowService.isExists(1l));
		assertTrue(slideshowService.isExists(2l));
		assertEquals(
			"New name",	
			slideshowService.findOne(2l).get().getName()
		);
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDuplicatesSlideshowWithNoNameGiven() throws Exception {
		SlideshowEntity ssToCompare = TestDataUtil.createSlideshowEntity();
        ssToCompare = slideshowService.save(ssToCompare);
		assertTrue(slideshowService.isExists(1l));

        String body = "{\"name\":null}";

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/slideshows/1/duplicate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testSS (Copy)"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.isArchived").value(false));
		
		assertTrue(slideshowService.isExists(1l));
		assertTrue(slideshowService.isExists(2l));
		assertEquals(
			"testSS (Copy)",	
			slideshowService.findOne(2l).get().getName()
		);
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDuplicatesSlideshowThatDoesNotExist() throws Exception {
        String body = "{\"name\":\"New name\"}";
		assertFalse(slideshowService.isExists(1l));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/slideshows/1/duplicate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());

		assertFalse(slideshowService.isExists(2l));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDuplicatesSlideshowWithVisualMediaInclusions() throws Exception {
		SlideshowEntity ssToCompare = TestDataUtil.createSlideshowWithMultipleVisualMediaEntities();
        ssToCompare = slideshowService.save(ssToCompare);
        String body = "{\"name\":\"New name\"}";

		assertTrue(slideshowService.isExists(1l));
		assertEquals(
			3,
			slideshowService.findOne(1l).get().getVisualMediaInclusionCollection().size()
		);
		assertTrue(
			visualMediaInclusionService.isExists(1l) &&
			visualMediaInclusionService.isExists(2l) &&
			visualMediaInclusionService.isExists(3l)
		);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/slideshows/1/duplicate")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.visualMediaInclusionCollection").isArray());
		
		assertTrue(slideshowService.isExists(1l));
		assertTrue(slideshowService.isExists(2l));
		assertEquals(
			3,
			slideshowService.findOne(2l).get().getVisualMediaInclusionCollection().size()
		);
		assertTrue(
			visualMediaInclusionService.isExists(4l) &&
			visualMediaInclusionService.isExists(5l) &&
			visualMediaInclusionService.isExists(6l)
		);
    }

    @Test
	@WithMockUser(roles = { "PLANNER" })
	public void getAllSlideshows() throws Exception {
		slideshowService.save(TestDataUtil.createSlideshowEntity());
		slideshowService.save(TestDataUtil.createSlideshowEntity());
		slideshowService.save(TestDataUtil.createSlideshowEntity());
		slideshowService.save(TestDataUtil.createSlideshowEntity());
		slideshowService.save(TestDataUtil.createSlideshowEntity());
		slideshowService.save(TestDataUtil.createSlideshowEntity());
		slideshowService.save(TestDataUtil.createSlideshowEntity());
		slideshowService.save(TestDataUtil.createSlideshowEntity());
		slideshowService.save(TestDataUtil.createSlideshowEntity());

		assertTrue(slideshowService.isExists(1L));
		assertTrue(slideshowService.isExists(2L));
		assertTrue(slideshowService.isExists(3L));
		assertTrue(slideshowService.isExists(4L));
		assertTrue(slideshowService.isExists(5L));
		assertTrue(slideshowService.isExists(6L));
		assertTrue(slideshowService.isExists(7L));
		assertTrue(slideshowService.isExists(8L));
		assertTrue(slideshowService.isExists(9L));
		
		mockMvc.perform(
                MockMvcRequestBuilders.get("/api/slideshows")
        ).andExpect(
			MockMvcResultMatchers.status().isOk())
		.andExpect(
			MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(
			MockMvcResultMatchers.jsonPath("$.length()").value(9));
	}

	@Test
	@WithMockUser(roles = { "PLANNER" })
	public void getAllVisualMediasWithNoDevicesInDatabase() throws Exception {
		mockMvc.perform(
                MockMvcRequestBuilders.get("/api/slideshows")
        ).andExpect(
			MockMvcResultMatchers.status().isOk())
		.andExpect(
			MockMvcResultMatchers.jsonPath("$").isArray())
		.andExpect(
			MockMvcResultMatchers.jsonPath("$.length()").value(0));
	}

}
