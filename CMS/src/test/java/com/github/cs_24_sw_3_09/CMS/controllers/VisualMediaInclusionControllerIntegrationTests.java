package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaInclusionDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
    private VisualMediaService visualMediaService;

    @Autowired
    public VisualMediaInclusionControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, VisualMediaService visualMediaService) {
        this.mockMvc = mockMvc;
        this.visualMediaService = visualMediaService;
        this.objectMapper = objectMapper;
    }

    @Test
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
    public void testThatCreateVisualMediaInclusionReturnsCreatedVisualMedia() throws Exception {
        VisualMediaInclusionDto visualMediaInclusion = TestDataUtil.createVisualMediaInclusionDto();
        String visualMediaInclusionJson = objectMapper.writeValueAsString(visualMediaInclusion);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/visual_media_inclusions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(visualMediaInclusionJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(visualMediaInclusion.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slideshowPosition").value(visualMediaInclusion.getSlideshowPosition()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slideshowDuration").value(visualMediaInclusion.getSlideDuration()));

    }

}
