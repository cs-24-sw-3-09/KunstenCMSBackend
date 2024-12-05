package com.github.cs_24_sw_3_09.CMS.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.TagService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;



@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class) 
@AutoConfigureMockMvc
public class DimensionCheckServiceTests {
    
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private VisualMediaService visualMediaService;
    private SlideshowService slideshowService;
    private TagService tagService;
    private VisualMediaInclusionService visualMediaInclusionService;
    private DisplayDeviceService displayDeviceService;
    private DimensionCheckService dimensionCheckService;


    @Autowired
    public DimensionCheckServiceTests(MockMvc mockMvc, ObjectMapper objectMapper, VisualMediaService visualMediaService, 
                                        TagService tagService, SlideshowService slideshowService, 
                                        VisualMediaInclusionService visualMediaInclusionService, 
                                        DisplayDeviceService displayDeviceService,
                                        DimensionCheckService dimensionCheckService) {
        this.mockMvc = mockMvc;
        this.visualMediaService = visualMediaService;
        this.objectMapper = objectMapper;
        this.tagService = tagService;
        this.objectMapper = objectMapper;
        this.slideshowService = slideshowService;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.displayDeviceService = displayDeviceService;
        this.dimensionCheckService = dimensionCheckService;
    }

    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturns1WhenVMDimensionsMatchDD() throws Exception {
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity savedDisplayDevice = displayDeviceService.save(displayDevice).get();
        
        //Visual Media 
        MockMultipartFile file = TestDataUtil.createHorizontalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();
    
        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(savedDisplayDevice.getId(), visualMediaContent);
        assertTrue(returnString.equals("1"));
    }
    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturns1WhenSSDimensionsMatchDD() throws Exception{
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity savedDisplayDevice = displayDeviceService.save(displayDevice).get();

        MockMultipartFile file1 = TestDataUtil.createHorizontalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file1
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent1 = visualMediaService.findOne(1L).get();

        //create visual media inclusion and link it to the ContentEntity
        VisualMediaInclusionEntity visualMediaInclusion1 = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion1 = visualMediaInclusionService.save(visualMediaInclusion1).get();
        visualMediaInclusionService.setVisualMedia(savedVisualMediaInclusion1.getId().longValue(), visualMediaContent1.getId().longValue());
        
        MockMultipartFile file2 = TestDataUtil.createHorizontalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file2
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
        
        assertTrue(visualMediaService.isExists(2L));
        ContentEntity visualMediaContent2 = visualMediaService.findOne(2L).get();

        VisualMediaInclusionEntity visualMediaInclusion2 = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion2 = visualMediaInclusionService.save(visualMediaInclusion2).get();
        visualMediaInclusionService.setVisualMedia(savedVisualMediaInclusion2.getId().longValue(), visualMediaContent2.getId().longValue());

        SlideshowEntity slideshow = TestDataUtil.createSlideshowEntity();
        SlideshowEntity savedSlideshow = slideshowService.save(slideshow);
        slideshowService.addVisualMediaInclusion(savedSlideshow.getId().longValue(), savedVisualMediaInclusion1.getId().longValue());
        slideshowService.addVisualMediaInclusion(savedSlideshow.getId().longValue(), savedVisualMediaInclusion2.getId().longValue());

        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(savedDisplayDevice.getId(), savedSlideshow);
        assertTrue(returnString.equals("1"));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturnsCorrectStringWhenVMDimensionsDoesNotMatchDD() throws Exception{
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity savedDisplayDevice = displayDeviceService.save(displayDevice).get();
        
        //Visual Media 
        MockMultipartFile file = TestDataUtil.createVerticalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();
    
        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(savedDisplayDevice.getId(), visualMediaContent);
        
        assertTrue(returnString.equals("The dimension do not match:\nDisplay Device orientation: horizontal" + 
                "\nFallback Visual Media orientation: vertical"));
    }
    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturnsCorrectStringWhenSSDimensionsDoesNotMatchDD() throws Exception{
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity savedDisplayDevice = displayDeviceService.save(displayDevice).get();

        //Case 1: slideshow and display device has different dimensions
        MockMultipartFile file1 = TestDataUtil.createVerticalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file1
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent1 = visualMediaService.findOne(1L).get();

        //create visual media inclusion and link it to the ContentEntity
        VisualMediaInclusionEntity visualMediaInclusion1 = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion1 = visualMediaInclusionService.save(visualMediaInclusion1).get();
        visualMediaInclusionService.setVisualMedia(savedVisualMediaInclusion1.getId().longValue(), visualMediaContent1.getId().longValue());
        
        MockMultipartFile file2 = TestDataUtil.createVerticalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file2
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
        
        assertTrue(visualMediaService.isExists(2L));
        ContentEntity visualMediaContent2 = visualMediaService.findOne(2L).get();

        VisualMediaInclusionEntity visualMediaInclusion2 = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion2 = visualMediaInclusionService.save(visualMediaInclusion2).get();
        visualMediaInclusionService.setVisualMedia(savedVisualMediaInclusion2.getId().longValue(), visualMediaContent2.getId().longValue());

        SlideshowEntity slideshow = TestDataUtil.createSlideshowEntity();
        SlideshowEntity savedSlideshow = slideshowService.save(slideshow);
        slideshowService.addVisualMediaInclusion(savedSlideshow.getId().longValue(), savedVisualMediaInclusion1.getId().longValue());
        slideshowService.addVisualMediaInclusion(savedSlideshow.getId().longValue(), savedVisualMediaInclusion2.getId().longValue());

        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(savedDisplayDevice.getId(), savedSlideshow);
        assertTrue(returnString.equals("The dimension do not match:\nDisplay Device orientation: horizontal" + 
                "\nFallback Slide show orientation: vertical"));


        //Case 2: the Visual Media Inclusions in the Slideshow has mixed dimensions
        MockMultipartFile file3 = TestDataUtil.createHorizontalImage();
        System.out.println("file: "+file3);
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file3
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
        
        assertTrue(visualMediaService.isExists(3L));
        ContentEntity visualMediaContent3 = visualMediaService.findOne(3L).get();

        VisualMediaInclusionEntity visualMediaInclusion3 = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion3 = visualMediaInclusionService.save(visualMediaInclusion3).get();
        visualMediaInclusionService.setVisualMedia(savedVisualMediaInclusion3.getId().longValue(), visualMediaContent3.getId().longValue());

        slideshowService.addVisualMediaInclusion(savedSlideshow.getId().longValue(), savedVisualMediaInclusion3.getId().longValue());
        returnString = dimensionCheckService.checkDimensionForAssignedFallback(savedDisplayDevice.getId(), savedSlideshow);
        
        System.out.println(returnString);
        assertTrue(returnString.equals("The media in the slideshow has mixed orientation"));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedVMToSSReturns1WhenMatch() throws Exception{
        MockMultipartFile file1 = TestDataUtil.createHorizontalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file1
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent1 = visualMediaService.findOne(1L).get();

        VisualMediaInclusionEntity visualMediaInclusion1 = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion1 = visualMediaInclusionService.save(visualMediaInclusion1).get();
        visualMediaInclusionService.setVisualMedia(savedVisualMediaInclusion1.getId().longValue(), visualMediaContent1.getId().longValue());
        
        MockMultipartFile file2 = TestDataUtil.createHorizontalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file2
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
        
        assertTrue(visualMediaService.isExists(2L));
        ContentEntity visualMediaContent2 = visualMediaService.findOne(2L).get();

        VisualMediaInclusionEntity visualMediaInclusion2 = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion2 = visualMediaInclusionService.save(visualMediaInclusion2).get();
        visualMediaInclusionService.setVisualMedia(savedVisualMediaInclusion2.getId().longValue(), visualMediaContent2.getId().longValue());

        SlideshowEntity slideshow = TestDataUtil.createSlideshowEntity();
        SlideshowEntity savedSlideshow = slideshowService.save(slideshow);
        slideshowService.addVisualMediaInclusion(savedSlideshow.getId().longValue(), savedVisualMediaInclusion1.getId().longValue());
        
        String resultString = dimensionCheckService.checkDimensionForAssignedVisualMediaToSlideshow(savedVisualMediaInclusion2.getId().longValue(), savedSlideshow.getId().longValue());
        System.out.println(resultString);
        assertTrue(resultString.equals("1"));
    }
    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedVMToSSReturnsCorrectStringWhenNoMatch() throws Exception{
        MockMultipartFile file1 = TestDataUtil.createHorizontalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file1
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent1 = visualMediaService.findOne(1L).get();

        VisualMediaInclusionEntity visualMediaInclusion1 = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion1 = visualMediaInclusionService.save(visualMediaInclusion1).get();
        visualMediaInclusionService.setVisualMedia(savedVisualMediaInclusion1.getId().longValue(), visualMediaContent1.getId().longValue());
        
        MockMultipartFile file2 = TestDataUtil.createVerticalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file2
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
        
        assertTrue(visualMediaService.isExists(2L));
        ContentEntity visualMediaContent2 = visualMediaService.findOne(2L).get();

        VisualMediaInclusionEntity visualMediaInclusion2 = TestDataUtil.createVisualMediaInclusionEntity();
        VisualMediaInclusionEntity savedVisualMediaInclusion2 = visualMediaInclusionService.save(visualMediaInclusion2).get();
        visualMediaInclusionService.setVisualMedia(savedVisualMediaInclusion2.getId().longValue(), visualMediaContent2.getId().longValue());

        SlideshowEntity slideshow = TestDataUtil.createSlideshowEntity();
        SlideshowEntity savedSlideshow = slideshowService.save(slideshow);
        slideshowService.addVisualMediaInclusion(savedSlideshow.getId().longValue(), savedVisualMediaInclusion1.getId().longValue());
        
        String resultString = dimensionCheckService.checkDimensionForAssignedVisualMediaToSlideshow(savedVisualMediaInclusion2.getId().longValue(),savedSlideshow.getId().longValue());
        assertTrue(resultString.equals("The dimension do not match:\nSlideshow orientation: horizontal" + 
            "\nVisual Media orientation: vertical"));
    }
}