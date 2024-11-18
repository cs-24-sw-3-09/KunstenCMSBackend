package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class VisualMediaControllerIntegrationTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private VisualMediaService visualMediaService;

    @Autowired
    public VisualMediaControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, VisualMediaService visualMediaService) {
        this.mockMvc = mockMvc;
        this.visualMediaService = visualMediaService;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateVisualMediaReturnsHttpStatus201Created() throws Exception {
        VisualMediaDto visualMediaDto = TestDataUtil.createVisualMediaDto();
        String createdVisualMediaJson = objectMapper.writeValueAsString(visualMediaDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/visual_medias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createdVisualMediaJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateVisualMediaReturnsCreatedVisualMedia() throws Exception {
        VisualMediaDto visualMediaDto = TestDataUtil.createVisualMediaDto();
        String createdVisualMediaJson = objectMapper.writeValueAsString(visualMediaDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/visual_medias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createdVisualMediaJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(visualMediaDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location").value(visualMediaDto.getLocation())
        );
    }

    @Test
    public void testThatFindAllVisualMediaReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindAllVisualMediaReturnsVisualMedia() throws Exception {
        VisualMediaEntity testVisualMediaEntity = TestDataUtil.createVisualMediaEntity();
        visualMediaService.save(testVisualMediaEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].name").value(testVisualMediaEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].location").value(testVisualMediaEntity.getLocation())
        );
    }

    @Test
    public void testThatFindAllVisualMediaReturnsHttpStatus200WhenVisualMediaExists() throws Exception {
        VisualMediaEntity testVisualMediaEntity = TestDataUtil.createVisualMediaEntity();
        visualMediaService.save(testVisualMediaEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias/" + testVisualMediaEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindAllVisualMediaReturnsHttpStatus404WhenNoVisualMediaExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

}
