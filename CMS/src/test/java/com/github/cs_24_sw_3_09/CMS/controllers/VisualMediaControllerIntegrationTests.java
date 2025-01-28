package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import com.github.cs_24_sw_3_09.CMS.services.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class VisualMediaControllerIntegrationTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private VisualMediaService visualMediaService;
    private SlideshowService slideshowService;
    private TagService tagService;
    private VisualMediaInclusionService visualMediaInclusionService;
    private DisplayDeviceService displayDeviceService;

    @Autowired
    public VisualMediaControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper,
            VisualMediaService visualMediaService, TagService tagService, SlideshowService slideshowService,
            VisualMediaInclusionService visualMediaInclusionService, DisplayDeviceService displayDeviceService) {
        this.mockMvc = mockMvc;
        this.visualMediaService = visualMediaService;
        this.objectMapper = objectMapper;
        this.tagService = tagService;
        this.objectMapper = objectMapper;
        this.slideshowService = slideshowService;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.displayDeviceService = displayDeviceService;

    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatCreateVisualMediaReturnsHttpStatus201Created() throws Exception {

        MockMultipartFile file = TestDataUtil.createVisualMediaFile();

        mockMvc.perform(
                multipart("/api/visual_medias") // Use multipart request
                        .file(file) // Attach the file
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatCreateVisualMediaReturnsCreatedVisualMedia() throws Exception {
        MockMultipartFile file = TestDataUtil.createVisualMediaFile();

        mockMvc.perform(
                        multipart("/api/visual_medias")
                                .file(file)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test-image.jpeg"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileType").value(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location").value("/files/visual_media/1.jpg"));
    }

    @Test
    @WithMockUser
    public void testThatFindAllVisualMediaReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias")).andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testThatFindAllVisualMediaReturnsVisualMedia() throws Exception {
        VisualMediaEntity testVisualMediaEntity = TestDataUtil.createVisualMediaEntity();
        visualMediaService.save(testVisualMediaEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias")).andExpect(
                        MockMvcResultMatchers.jsonPath("content.[0].id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("content.[0].name").value(testVisualMediaEntity.getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("content.[0].location")
                                .value(testVisualMediaEntity.getLocation()));
    }

    @Test
    @WithMockUser
    public void testThatFindAllVisualMediaReturnsHttpStatus200WhenVisualMediaExists() throws Exception {
        VisualMediaEntity testVisualMediaEntity = TestDataUtil.createVisualMediaEntity();
        visualMediaService.save(testVisualMediaEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias/" + testVisualMediaEntity.getId())).andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testThatFindAllVisualMediaReturnsHttpStatus404WhenNoVisualMediaExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias/1")).andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testThatVisualMediaPartOfSlideshowsReturnsSlideshows() throws Exception {
        SlideshowEntity testSlideshowEntity = TestDataUtil.createSlideshowEntity();
        SlideshowEntity savedSlideshow = slideshowService.save(testSlideshowEntity);

        VisualMediaInclusionEntity testVisualMediaInclusionEntity = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusionEntity = visualMediaInclusionService
                .save(testVisualMediaInclusionEntity).get();

        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);

        visualMediaInclusionService.setVisualMedia((long) savedVisualMediaInclusionEntity.getId(),
                (long) savedVisualMediaEntity.getId());

        SlideshowEntity updatedSlideshow = slideshowService.addVisualMediaInclusion((long) savedSlideshow.getId(),
                (long) savedVisualMediaInclusionEntity.getId(), true).getOk();
        Set<VisualMediaInclusionEntity> inclusions = updatedSlideshow.getVisualMediaInclusionCollection();
        //convert to List so that indexing can be used
        List<VisualMediaInclusionEntity> inclusionList = new ArrayList<>(inclusions);

        long visualMediaId = inclusionList.get(0).getVisualMedia().getId();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias/" + visualMediaId + "/slideshows"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].id").value(updatedSlideshow.getId()));
    }

    @Test
    @WithMockUser
    public void testThatVisualMediaPartOfSlideshowsReturnsEmptySetWhenNoSlideshowUsesIt() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        visualMediaService.save(visualMediaEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias/" + visualMediaEntity.getId() + "/slideshows"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatFullUpdateVisualMediaReturnsStatus200WhenVisualMediaExists() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);

        VisualMediaDto visualMediaDto = TestDataUtil.createVisualMediaDto();
        String visualMediaDtoJson = objectMapper.writeValueAsString(visualMediaDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/visual_medias/" + savedVisualMediaEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visualMediaDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatFullUpdateVisualMediaReturnsStatus404WhenNoVisualMediaExists() throws Exception {
        VisualMediaDto visualMediaDto = TestDataUtil.createVisualMediaDto();
        String visualMediaDtoJson = objectMapper.writeValueAsString(visualMediaDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/visual_medias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visualMediaDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatPatchUpdateVisualMediaReturnsStatus200() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);

        VisualMediaDto visualMediaDto = TestDataUtil.createVisualMediaDto();
        String visualMediaDtoJson = objectMapper.writeValueAsString(visualMediaDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/visual_medias/" + savedVisualMediaEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visualMediaDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value(visualMediaDto.getName()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.location").value(visualMediaDto.getLocation()));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatPatchUpdateVisualMediaReturnsStatus404() throws Exception {
        VisualMediaDto visualMediaDto = TestDataUtil.createVisualMediaDto();
        String visualMediaDtoJson = objectMapper.writeValueAsString(visualMediaDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/visual_medias/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(visualMediaDtoJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeleteVisualMediaReturnsStatus200() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_medias/" + savedVisualMediaEntity.getId())).andExpect(
                        MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeleteVisualMediaWithTagsReturnsStatus200() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        

        visualMediaEntity.setTags(new HashSet<>());
        visualMediaEntity.addTag(TestDataUtil.createTagEntity());
        visualMediaEntity.addTag(TestDataUtil.createTagEntity2());

        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);

        assertTrue(visualMediaService.isExists(1L));
        assertTrue(tagService.isExists(1L));
        assertTrue(tagService.isExists(2L));

        //System.out.println(objectMapper.writeValueAsString(savedVisualMediaEntity));
        //visualMediaService.findOne(1l).get().getTags().stream().forEach(tagEx -> System.out.println(tagEx.getId()));
        



        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_medias/" + savedVisualMediaEntity.getId())).andExpect(
                        MockMvcResultMatchers.status().isNoContent());

        assertFalse(visualMediaService.isExists(1l));
        assertTrue(tagService.isExists(1L));
        assertTrue(tagService.isExists(2L));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeleteVisualMediaReturnsStatus404() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_medias/99")).andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatAddTagToVisualMediaReturnsVisualMediaWithAddedTag() throws Exception {
        TagEntity tagToSave = TestDataUtil.createTagEntity();
        TagEntity savedTagEntity = tagService.save(tagToSave);
        assertTrue(tagService.isExists((long) 1));

        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);
        assertTrue(visualMediaService.isExists((long) 1));

        String requestBodyJson = "{\"tagText\": \"" + savedTagEntity.getText() + "\"}";

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/visual_medias/" + savedVisualMediaEntity.getId() + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.tags[0].text").value(savedTagEntity.getText()));

        visualMediaService.findOne((long) 1).get().getTags().stream()
                .allMatch(tag -> tagService.isExists((long) tag.getId()));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatAddTagToVisualMediaWhenTagDoesntExistAndReturnsVisualMediaWithTag() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);

        String requestBodyJson = "{\"tagText\": \"" + "IDontExistAlready" + "\"}";

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/visual_medias/" + savedVisualMediaEntity.getId() + "/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.tags[0].text").value("IDontExistAlready"));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatAddTagToVisualMediaWhenVisualMediaDoesntExistAndReturns404() throws Exception {
        TagEntity tag = TestDataUtil.createTagEntity();
        tagService.save(tag);
        assertTrue(tagService.isExists((long) 1));

        String requestBodyJson = "{\"tagText\": \"" + "IDontExistAlready" + "\"}";

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/visual_medias/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatGetDisplayDevicesVisualMediaIsPartOfReturnsDisiplayDevices() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);

        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity, true).getOk();

        DisplayDeviceEntity updatedDisplayDeviceEntity = displayDeviceService.setFallbackContent(
                (long) savedDisplayDeviceEntity.getId(), (long) savedVisualMediaEntity.getId(), "VisualMediaEntity");

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias/" + savedVisualMediaEntity.getId() + "/display_devices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$[0].name").value(savedDisplayDeviceEntity.getName()));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeletesAssociationBetweenVisualMediaAndTag() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntityWithTags();
        visualMediaService.save(visualMediaEntity);

        assertTrue(visualMediaService.isExists((long) 1));
        assertTrue(visualMediaService.findOne((long) 1).get().getTags().stream().allMatch(
                tag -> tagService.isExists((long) tag.getId())));
        assertTrue(tagService.isExists((long) 1));
        assertTrue(tagService.isExists((long) 2));

        String body = "{\"tagId\": 1 }";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_medias/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)

        ).andExpect(
                MockMvcResultMatchers.status().isNoContent());

        assertTrue(visualMediaService.isExists((long) 1));
        assertTrue(visualMediaService.findOne((long) 1).get().getTags().stream().noneMatch(
                tag -> tag.getId() == 1));
        assertTrue(tagService.isExists((long) 1));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeletesAssociationBetweenVisualMediaAndTagWhenTagDoesntExist() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        visualMediaService.save(visualMediaEntity);

        assertTrue(visualMediaService.isExists((long) 1));
        assertFalse(tagService.isExists((long) 1));
        assertEquals(
                0,
                visualMediaService.findOne((long) 1).get().getTags().size());

        String body = "{\"tagId\": 1 }";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_medias/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)

        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());

        assertTrue(visualMediaService.isExists((long) 1));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeletesAssociationBetweenVisualMediaAndTagWhenAssociationDoesntExist() throws Exception {
        VisualMediaEntity visualMediaEntity = TestDataUtil.createVisualMediaEntity();
        visualMediaService.save(visualMediaEntity);

        TagEntity tagToSave = TestDataUtil.createTagEntity();
        tagService.save(tagToSave);

        assertTrue(visualMediaService.isExists((long) 1));
        assertTrue(tagService.isExists((long) 1));
        assertEquals(
                0,
                visualMediaService.findOne((long) 1).get().getTags().size());

        String body = "{\"tagId\": 1 }";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_medias/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)

        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());

        assertTrue(visualMediaService.isExists((long) 1));
        assertTrue(tagService.isExists((long) 1));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatDeletesAssociationBetweenVisualMediaAndTagWhenVisualMediaDoesntExist() throws Exception {
        TagEntity tagToSave = TestDataUtil.createTagEntity();
        tagService.save(tagToSave);

        assertFalse(visualMediaService.isExists((long) 1));
        assertTrue(tagService.isExists((long) 1));
        assertEquals(
                0,
                tagService.findOne((long) 1).get().getVisualMedias().size());

        String body = "{\"tagId\": 1 }";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/visual_medias/1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)

        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());

        assertFalse(visualMediaService.isExists((long) 1));
        assertTrue(tagService.isExists((long) 1));
    }

    @Test
    @WithMockUser(roles = "PLANNER")
    public void testThatReplaceFileReplacesFileIfFileExists() throws Exception {

        MockMultipartFile newFile1 = new MockMultipartFile(
                "file1",
                "new-file1.jpg",
                "image/jpeg",
                "test content".getBytes());

        MockMultipartFile newFile2 = new MockMultipartFile(
                "file2",
                "new-file2.jpg",
                "image/jpeg",
                "test content".getBytes());

        //Post image to db and folder
        // Step 1: Upload the first file (simulate storing the original file)
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/visual_medias")
                .file("file", newFile1.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        VisualMediaEntity vm = visualMediaService.findOne(1L).get();
        
        // Step 2: Replace the file
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/visual_medias/1/file")
                .file("file", newFile2.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = { "PLANNER" })
    public void getAllVisualMedias() throws Exception {
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());

        assertTrue(visualMediaService.isExists(1L));
        assertTrue(visualMediaService.isExists(2L));
        assertTrue(visualMediaService.isExists(3L));
        assertTrue(visualMediaService.isExists(4L));
        assertTrue(visualMediaService.isExists(5L));
        assertTrue(visualMediaService.isExists(6L));
        assertTrue(visualMediaService.isExists(7L));
        assertTrue(visualMediaService.isExists(8L));
        assertTrue(visualMediaService.isExists(9L));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/visual_medias/all")).andExpect(
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
                MockMvcRequestBuilders.get("/api/visual_medias/all")).andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(roles = { "PLANNER" })
    public void getVisualMediaStatesWithNoRecordsInDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/visual_medias/states"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(roles = { "PLANNER" })
    public void getVisualMediaStatesWithOneGreyRecordInDatabase() throws Exception {
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        assertTrue(visualMediaService.isExists(1L));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/visual_medias/states"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].visualMediaId").value(1))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].color").value("red"));
    }

    @Test
    @WithMockUser(roles = { "PLANNER" })
    public void getVisualMediaStatesWithOneRedAndGreyRecordInDatabase() throws Exception {
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        assertTrue(visualMediaService.isExists(1L));
        visualMediaService.save(TestDataUtil.createVisualMediaEntity());
        assertTrue(visualMediaService.isExists(2L));

        slideshowService.save(TestDataUtil.createSlideshowEntity());
        assertTrue(slideshowService.isExists(3L));

        VisualMediaInclusionEntity testVisualMediaInclusionEntity = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusionEntity = visualMediaInclusionService
                .save(testVisualMediaInclusionEntity).get();

        visualMediaInclusionService.setVisualMedia((long) savedVisualMediaInclusionEntity.getId(), (long) 2);

        slideshowService.addVisualMediaInclusion((long) 3,
                (long) savedVisualMediaInclusionEntity.getId(), true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/visual_medias/states"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].visualMediaId").value(1))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].color").value("red"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].visualMediaId").value(2))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].color").value("yellow"));
    }

}
