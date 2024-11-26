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
import org.springframework.mock.web.MockMultipartFile;
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
    @WithMockUser(roles="PLANNER")
    public void testThatCreateVisualMediaReturnsHttpStatus201Created() throws Exception {


        MockMultipartFile file = TestDataUtil.createVisualMediaFile();

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/api/visual_medias") // Use multipart request
                        .file(file)                                   // Attach the file
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCreateVisualMediaReturnsCreatedVisualMedia() throws Exception {
        MockMultipartFile file = TestDataUtil.createVisualMediaFile();

        mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/api/visual_medias")
                                .file(file)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test-image.jpg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileType").value(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location").value("/visual_media/1"));
    }

    @Test
    @WithMockUser
    public void testThatFindAllVisualMediaReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser
    public void testThatFindAllVisualMediaReturnsVisualMedia() throws Exception {
        VisualMediaEntity testVisualMediaEntity = TestDataUtil.createVisualMediaEntity();
        visualMediaService.save(testVisualMediaEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].name").value(testVisualMediaEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].location").value(testVisualMediaEntity.getLocation())
        );
    }

    @Test
    @WithMockUser
    public void testThatFindAllVisualMediaReturnsHttpStatus200WhenVisualMediaExists() throws Exception {
        VisualMediaEntity testVisualMediaEntity = TestDataUtil.createVisualMediaEntity();
        visualMediaService.save(testVisualMediaEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias/" + testVisualMediaEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser
    public void testThatFindAllVisualMediaReturnsHttpStatus404WhenNoVisualMediaExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }


    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatFullUpdateVisualMediaReturnsStatus200WhenVisualMediaExists() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);

        VisualMediaDto visualMediaDto = TestDataUtil.createVisualMediaDto();
        String visualMediaDtoJson = objectMapper.writeValueAsString(visualMediaDto);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/visual_medias/" + savedVisualMediaEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visualMediaDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatFullUpdateVisualMediaReturnsStatus404WhenNoVisualMediaExists() throws Exception {
        VisualMediaDto visualMediaDto = TestDataUtil.createVisualMediaDto();
        String visualMediaDtoJson = objectMapper.writeValueAsString(visualMediaDto);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/visual_medias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visualMediaDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatPatchUpdateVisualMediaReturnsStatus200() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);

        VisualMediaDto visualMediaDto = TestDataUtil.createVisualMediaDto();
        String visualMediaDtoJson = objectMapper.writeValueAsString(visualMediaDto);


        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/visual_medias/" + savedVisualMediaEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visualMediaDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(visualMediaDto.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location").value(visualMediaDto.getLocation())
        );
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatPatchUpdateVisualMediaReturnsStatus404() throws Exception {
        VisualMediaDto visualMediaDto = TestDataUtil.createVisualMediaDto();
        String visualMediaDtoJson = objectMapper.writeValueAsString(visualMediaDto);


        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/visual_medias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visualMediaDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }


    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDeleteVisualMediaReturnsStatus200() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_medias/" + savedVisualMediaEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }


    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDeleteVisualMediaReturnsStatus404() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_medias/99")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }
}


