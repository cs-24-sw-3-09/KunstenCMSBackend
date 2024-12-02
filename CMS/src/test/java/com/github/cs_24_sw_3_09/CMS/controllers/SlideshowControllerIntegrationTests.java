package com.github.cs_24_sw_3_09.CMS.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.SlideshowDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
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
public class SlideshowControllerIntegrationTests {


    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private SlideshowService slideshowService;
    private VisualMediaInclusionService visualMediaInclusionService;

    @Autowired
    public SlideshowControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, SlideshowService slideshowService,
                                               VisualMediaInclusionService visualMediaInclusionService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.slideshowService = slideshowService;
        this.visualMediaInclusionService = visualMediaInclusionService;

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
        VisualMediaInclusionEntity visualMediaInclusionEntity = TestDataUtil.createVisualMediaInclusionWithVisualMediaEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion = visualMediaInclusionService.save(visualMediaInclusionEntity);

        SlideshowEntity slideshowEntity = TestDataUtil.createSlideshowEntity();
        SlideshowEntity savedSlideshow = slideshowService.save(slideshowEntity);

        String requestBodyJson = "{\"visualMediaInclusionId\": " + savedVisualMediaInclusion.getId() + "}";

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/slideshows/" + savedSlideshow.getId() + "/visual_media_inclusions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.visualMediaInclusionCollection[0].slideDuration").value(savedVisualMediaInclusion.getSlideDuration()));


    }


}
