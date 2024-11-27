package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.TagDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TagDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import com.github.cs_24_sw_3_09.CMS.services.TagService;
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
public class TagControllerIntegrationTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private TagService tagService;

    @Autowired
    public TagControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, TagService tagService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.tagService = tagService;
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatCreateTagReturnsHttpStatus201Created() throws Exception {
        TagDto tagDto = TestDataUtil.createTagDto();
        String createdTagJson = objectMapper.writeValueAsString(tagDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/tags")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createdTagJson))
                .andExpect(
                        MockMvcResultMatchers.status().isCreated());
    }


    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCreateTagReturnsCreatedTag() throws Exception {
        TagDto tag = TestDataUtil.createTagDto();
        String tagJson = objectMapper.writeValueAsString(tag);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/tags")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(tagJson)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(tag.getText()));

    }

    @Test
    @WithMockUser
    public void testThatGetTagSuccessfullyReturnsHttp200() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/tags")).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testThatGetTagsSuccessfullyReturnsListOfTags() throws Exception {
        TagEntity testTagEntity = TestDataUtil.createTagEntity();
        tagService.save(testTagEntity);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/tags")).andExpect(
                        MockMvcResultMatchers.jsonPath("content.[0].id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("content.[0].text").value(testTagEntity.getText()));
    }

    @Test
    @WithMockUser
    public void testThatGetTagReturnsStatus200WhenExists() throws Exception {
        TagEntity tagEntity = TestDataUtil.createTagEntity();
        tagService.save(tagEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/tags/1")).andExpect(
                MockMvcResultMatchers.status().isOk());
    }


    @Test
    @WithMockUser
    public void testThatGetTagReturnsStatus404WhenNoTagExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/tags/99")).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testThatGetTagReturnsTagWhenTagExists() throws Exception {
        TagEntity tag = TestDataUtil.createTagEntity();
        tagService.save(tag);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/tags/" + tag.getId())).andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.text").value(tag.getText()));
    }


    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDeleteTagReturnsStatus200() throws Exception {
        TagEntity tagEntity = TestDataUtil.createTagEntity();
        TagEntity savedTagEntity = tagService.save(tagEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/tags/" + savedTagEntity.getId())).andExpect(
                MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatDeleteTagReturnsStatus404() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/tags/99")).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatPatchUpdateTagReturnsStatus200() throws Exception {
        TagEntity tagEntity = TestDataUtil.createTagEntity();
        TagEntity savedTagEntity = tagService.save(tagEntity);

        TagDto tagDto = TestDataUtil.createTagDto();
        String tagDtoJson = objectMapper.writeValueAsString(tagDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/tags/" + savedTagEntity.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(tagDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.text").value(tagDto.getText()));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatPatchUpdateTagReturnsStatus404() throws Exception {
        TagDto tagDto = TestDataUtil.createTagDto();
        String tagDtoJson = objectMapper.writeValueAsString(tagDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/tags/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(tagDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

}
