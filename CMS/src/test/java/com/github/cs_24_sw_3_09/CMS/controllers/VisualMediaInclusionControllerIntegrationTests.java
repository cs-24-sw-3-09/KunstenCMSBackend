package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaInclusionDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

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
public class VisualMediaInclusionControllerIntegrationTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private VisualMediaInclusionService visualMediaInclusionService;
    private VisualMediaService visualMediaService;
	private SlideshowService slideshowService; 

    @Autowired
    public VisualMediaInclusionControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, 
	VisualMediaInclusionService visualMediaInclusionService, VisualMediaService visualMediaService,
	SlideshowService slideshowService) {
        this.mockMvc = mockMvc;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.objectMapper = objectMapper;
        this.visualMediaService = visualMediaService;
		this.slideshowService = slideshowService;
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatCreateVisualMediaInclusionReturnsHttpStatus201Created() throws Exception {
        VisualMediaInclusionDto visualMediaInclusion = TestDataUtil.createVisualMediaInclusionDto();
        String visualMediaInclusionJson = objectMapper.writeValueAsString(visualMediaInclusion);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/visual_media_inclusions") // Use multipart request
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visualMediaInclusionJson)                                  // Attach the file
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatCreateVisualMediaInclusionReturnsCreatedVisualMediaInclusion() throws Exception {
        VisualMediaInclusionDto visualMediaInclusion = TestDataUtil.createVisualMediaInclusionDto();
        String visualMediaInclusionJson = objectMapper.writeValueAsString(visualMediaInclusion);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/visual_media_inclusions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(visualMediaInclusionJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.slideshowPosition").value(visualMediaInclusion.getSlideshowPosition()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slideDuration").value(visualMediaInclusion.getSlideDuration()));

    }

    @Test
    @WithMockUser
    public void testThatGetVisualMediaInclusionSuccessfullyReturnsHttp200() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_media_inclusions")).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testThatGetVisualMediaInclusionsSuccessfullyReturnsListOfVisualMediaInclusions() throws Exception {
        VisualMediaInclusionEntity testVMIEntity = TestDataUtil.createVisualMediaInclusionWithVisualMediaEntity();
        visualMediaInclusionService.save(testVMIEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/visual_media_inclusions")).andExpect(
                        MockMvcResultMatchers.jsonPath("content.[0].id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("content.[0].slideDuration").value(testVMIEntity.getSlideDuration()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("content.[0].slideshowPosition")
                                .value(testVMIEntity.getSlideshowPosition()));
    }

    @Test
    @WithMockUser
    public void testThatGetVisualMediaInclusionReturnsStatus200WhenExists() throws Exception {
        VisualMediaInclusionEntity visualMediaInclusionEntity = TestDataUtil.createVisualMediaInclusionWithVisualMediaEntity();
        visualMediaInclusionService.save(visualMediaInclusionEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_media_inclusions/1")).andExpect(
                MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser
    public void testThatGetVisualMediaInclusionReturnsStatus404WhenNoVisualMediaInclusionExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_media_inclusions/99")).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testThatGetVisualMediaInclusionReturnsVisualMediaInclusionWhenVisualMediaInclusionExists() throws Exception {
        VisualMediaInclusionEntity visualMediaInclusion = TestDataUtil.createVisualMediaInclusionWithVisualMediaEntity();
        visualMediaInclusionService.save(visualMediaInclusion);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/visual_media_inclusions/" + visualMediaInclusion.getId())).andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.slideDuration").value(visualMediaInclusion.getSlideDuration()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.slideshowPosition").value(visualMediaInclusion.getSlideshowPosition()));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatFullUpdateVisualMediaInclusionReturnsStatus404WhenNoVisualMediaInclusionExists() throws Exception {
        VisualMediaInclusionDto visualMediaInclusionDto = TestDataUtil.createVisualMediaInclusionDto();
        String visualMediaInclusionDtoJson = objectMapper.writeValueAsString(visualMediaInclusionDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/visual_media_inclusions/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(visualMediaInclusionDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeleteVisualMediaInclusionReturnsStatus200() throws Exception {
        VisualMediaInclusionEntity visualMediaInclusionEntity = TestDataUtil.createVisualMediaInclusionWithVisualMediaEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusionEntity = visualMediaInclusionService.save(visualMediaInclusionEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_media_inclusions/" + savedVisualMediaInclusionEntity.getId())).andExpect(
                MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeleteVisualMediaInclusionReturnsStatus404() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_media_inclusions/99")).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatFullUpdateVisualMediaInclusionReturnsStatus200WhenVisualMediaInclusionExists() throws Exception {
        VisualMediaInclusionEntity visualMediaInclusionEntity = TestDataUtil.createVisualMediaInclusionWithVisualMediaEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusionEntity = visualMediaInclusionService.save(visualMediaInclusionEntity);

        VisualMediaInclusionDto visualMediaInclusionDto = TestDataUtil.createVisualMediaInclusionDto();
        String visualMediaInclusionDtoJson = objectMapper.writeValueAsString(visualMediaInclusionDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/visual_media_inclusions/" + savedVisualMediaInclusionEntity.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(visualMediaInclusionDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatPatchUpdateVisualMediaInclusionReturnsStatus200() throws Exception {
        VisualMediaInclusionEntity visualMediaInclusionEntity = TestDataUtil.createVisualMediaInclusionWithVisualMediaEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusionEntity = visualMediaInclusionService.save(visualMediaInclusionEntity);

        VisualMediaInclusionDto visualMediaInclusionDto = TestDataUtil.createVisualMediaInclusionDto();
        String visualMediaInclusionDtoJson = objectMapper.writeValueAsString(visualMediaInclusionDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/visual_media_inclusions/" + savedVisualMediaInclusionEntity.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(visualMediaInclusionDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.slideDuration").value(visualMediaInclusionDto.getSlideDuration()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.slideshowPosition").value(visualMediaInclusionDto.getSlideshowPosition()));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatPatchUpdateVisualMediaInclusionReturnsStatus404() throws Exception {
        VisualMediaInclusionDto visualMediaInclusionDto = TestDataUtil.createVisualMediaInclusionDto();
        String visualMediaInclusionDtoJson = objectMapper.writeValueAsString(visualMediaInclusionDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/visual_media_inclusions/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(visualMediaInclusionDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatSetVisualMediaOnVisualMediaInclusionReturnsVisualMediaInclusionWithVisualMediaSet() throws Exception {
        VisualMediaInclusionEntity visualMediaInclusionEntity = TestDataUtil.createVisualMediaInclusionWithVisualMediaEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusionEntity = visualMediaInclusionService.save(visualMediaInclusionEntity);

        VisualMediaEntity VisualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(VisualMediaEntity);
        String requestBodyJson = "{\"visualMediaId\":" + savedVisualMediaEntity.getId() + "}";

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/visual_media_inclusions/" + savedVisualMediaInclusionEntity.getId() + "/visual_media")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.visualMedia.name").value(savedVisualMediaEntity.getName()));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatUpdatesVisualMediaInclusionsPositionsInASlideshowAndReturns200() throws Exception {
		SlideshowEntity slideshowEntityToSave = TestDataUtil.createSlideshowWithMultipleVisualMediaEntities();
		SlideshowEntity savedSlideshowEntity = slideshowService.save(slideshowEntityToSave);

		savedSlideshowEntity.getVisualMediaInclusionCollection().stream().forEach(vmi -> {
			vmi.setSlideshowPosition(vmi.getId());
			visualMediaInclusionService.partialUpdate((long) vmi.getId(), vmi);
		});

		slideshowService.save(savedSlideshowEntity);

		assertTrue(visualMediaInclusionService.isExists(1l));
		assertTrue(visualMediaInclusionService.isExists(2l));
		assertTrue(visualMediaInclusionService.isExists(3l));
		
		String requestBodyJson = 
		"{"
		+	"\"visualMediaInclusion\":"
		+	"["
		+		"{"
		+			"\"id\":1,\"slideshowPosition\":3"
		+		"},"
		+		"{"
		+			"\"id\":2,\"slideshowPosition\":1"
		+		"},"
		+		"{"
		+			"\"id\":3,\"slideshowPosition\":2"
		+		"}"
		+	"]"
		+"}";

		mockMvc.perform(
			MockMvcRequestBuilders.patch("/api/visual_media_inclusions/positions")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBodyJson))
		.andExpect(
			MockMvcResultMatchers.status().isOk()
		).andExpect(
			MockMvcResultMatchers.jsonPath("$[0].id")
			.value(visualMediaInclusionService.findOne(1l).get().getId())
		).andExpect(
			MockMvcResultMatchers.jsonPath("$[0].slideshowPosition")
			.value(visualMediaInclusionService.findOne(1l).get().getSlideshowPosition())
		).andExpect(
			MockMvcResultMatchers.jsonPath("$[1].id")
			.value(visualMediaInclusionService.findOne(2l).get().getId())
		).andExpect(
			MockMvcResultMatchers.jsonPath("$[1].slideshowPosition")
			.value(visualMediaInclusionService.findOne(2l).get().getSlideshowPosition())
		);

		assertTrue(visualMediaInclusionService.isExists(1l));
		assertTrue(visualMediaInclusionService.isExists(2l));
		assertTrue(visualMediaInclusionService.isExists(3l));

		assertEquals(
			3,
			visualMediaInclusionService.findOne(1l).get().getSlideshowPosition()
		);
		assertEquals(
			1,
			visualMediaInclusionService.findOne(2l).get().getSlideshowPosition()
		);
		assertEquals(
			2,
			visualMediaInclusionService.findOne(3l).get().getSlideshowPosition()
		);
	}

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatTriesUpdatesVisualMediaInclusionsPositionsButNotAllAndReturns200() throws Exception {
		SlideshowEntity slideshowEntityToSave = TestDataUtil.createSlideshowWithMultipleVisualMediaEntities();
		SlideshowEntity savedSlideshowEntity = slideshowService.save(slideshowEntityToSave);

		savedSlideshowEntity.getVisualMediaInclusionCollection().stream().forEach(vmi -> {
			vmi.setSlideshowPosition(vmi.getId());
			visualMediaInclusionService.partialUpdate((long) vmi.getId(), vmi);
		});

		slideshowService.save(savedSlideshowEntity);
		/*for (VisualMediaInclusionEntity vmi : savedSlideshowEntity.getVisualMediaInclusionCollection()) {
			System.out.println("Visual Media Id: "+ vmi.getId()+"\nPosition: "+ vmi.getSlideshowPosition());
		}*/
		
		assertTrue(visualMediaInclusionService.isExists(1l));
		assertTrue(visualMediaInclusionService.isExists(2l));
		assertTrue(visualMediaInclusionService.isExists(3l));
		
		String requestBodyJson = 
		"{"
		+	"\"visualMediaInclusion\":"
		+	"["
		+		"{"
		+			"\"id\":1,\"slideshowPosition\":2"
		+		"},"
		+		"{"
		+			"\"id\":2,\"slideshowPosition\":1"
		+		"}"
		+	"]"
		+"}";

		mockMvc.perform(
			MockMvcRequestBuilders.patch("/api/visual_media_inclusions/positions")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBodyJson))
		.andExpect(
			MockMvcResultMatchers.status().isOk()
		).andExpect(
			MockMvcResultMatchers.jsonPath("$[0].id")
			.value(visualMediaInclusionService.findOne(1l).get().getId())
		).andExpect(
			MockMvcResultMatchers.jsonPath("$[0].slideshowPosition")
			.value(visualMediaInclusionService.findOne(1l).get().getSlideshowPosition())
		).andExpect(
			MockMvcResultMatchers.jsonPath("$[1].id")
			.value(visualMediaInclusionService.findOne(2l).get().getId())
		).andExpect(
			MockMvcResultMatchers.jsonPath("$[1].slideshowPosition")
			.value(visualMediaInclusionService.findOne(2l).get().getSlideshowPosition())
		);

		assertTrue(visualMediaInclusionService.isExists(1l));
		assertTrue(visualMediaInclusionService.isExists(2l));
		assertTrue(visualMediaInclusionService.isExists(3l));

		assertEquals(
			2,
			visualMediaInclusionService.findOne(1l).get().getSlideshowPosition()
		);
		assertEquals(
			1,
			visualMediaInclusionService.findOne(2l).get().getSlideshowPosition()
		);
		assertEquals(
			3,
			visualMediaInclusionService.findOne(3l).get().getSlideshowPosition()
		);
    }

	@Test
    @WithMockUser(roles = "PLANNER")
    public void testThatTriesUpdatesVisualMediaInclusionsPositionsNotInASlideShowReturns404() throws Exception {
		SlideshowEntity slideshowEntityToSave = TestDataUtil.createSlideshowWithVisualMediaEntity();
		SlideshowEntity savedSlideshowEntity = slideshowService.save(slideshowEntityToSave);

		savedSlideshowEntity.getVisualMediaInclusionCollection().stream().forEach(vmi -> {
			vmi.setSlideshowPosition(vmi.getId());
			visualMediaInclusionService.partialUpdate((long) vmi.getId(), vmi);
		});

		slideshowService.save(savedSlideshowEntity);

		assertTrue(visualMediaInclusionService.isExists(1l));
		assertFalse(visualMediaInclusionService.isExists(2l));
		
		String requestBodyJson = 
		"{"
		+	"\"visualMediaInclusion\":"
		+	"["
		+		"{"
		+			"\"id\":1,\"position\":2"
		+		"},"
		+		"{"
		+			"\"id\":2,\"position\":1"
		+		"}"
		+	"]"
		+"}";

		mockMvc.perform(
			MockMvcRequestBuilders.patch("/api/visual_media_inclusions/positions")
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBodyJson))
		.andExpect(
			MockMvcResultMatchers.status().isNotFound()
		);

		assertTrue(visualMediaInclusionService.isExists(1l));
		assertFalse(visualMediaInclusionService.isExists(2l));

		assertEquals(
			1,
			visualMediaInclusionService.findOne(1l).get().getSlideshowPosition()
		);
	}

}
