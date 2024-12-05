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
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.TagService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
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
    private TimeSlotService timeSlotService;


    @Autowired
    public DimensionCheckServiceTests(MockMvc mockMvc, ObjectMapper objectMapper, VisualMediaService visualMediaService, 
                                        TagService tagService, SlideshowService slideshowService, 
                                        VisualMediaInclusionService visualMediaInclusionService, 
                                        DisplayDeviceService displayDeviceService,
                                        DimensionCheckService dimensionCheckService,
                                        TimeSlotService timeSlotService) {
        this.mockMvc = mockMvc;
        this.visualMediaService = visualMediaService;
        this.objectMapper = objectMapper;
        this.tagService = tagService;
        this.objectMapper = objectMapper;
        this.slideshowService = slideshowService;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.displayDeviceService = displayDeviceService;
        this.dimensionCheckService = dimensionCheckService;
        this.timeSlotService = timeSlotService;
    }

    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturns1WhenVMDimensionsMatchDD() throws Exception {
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDevice).get();
        
        createHorizontalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();

        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice.getId(), visualMediaContent);
        assertTrue(returnString.equals("1"));
    }

    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturns1WhenSSDimensionsMatchDD() throws Exception{
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDevice).get();
        
        SlideshowEntity slideshow = createHorizontalSlideshow();

        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice.getId(), slideshow);
        assertTrue(returnString.equals("1"));
    }

    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturnsCorrectStringWhenVMDimensionsDoesNotMatchDD() throws Exception{
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDevice).get();
        
        creatVerticalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();
    
        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice.getId(), visualMediaContent);
        
        assertTrue(returnString.equals("The dimension do not match:\nDisplay Device orientation: horizontal" + 
                "\nFallback Visual Media orientation: vertical"));
    }
    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturnsCorrectStringWhenSSDimensionsDoesNotMatchDD() throws Exception{
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDevice).get();

        //Case 1: slideshow and display device has different dimensions
       SlideshowEntity slideshow = createVerticalSlideshow();

        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice.getId(), slideshow);
        assertTrue(returnString.equals("The dimension do not match:\nDisplay Device orientation: horizontal" + 
                "\nFallback Slide show orientation: vertical"));


        //Case 2: the Visual Media Inclusions in the Slideshow has mixed dimensions
        SlideshowEntity slideshow2 = createMixedSlideshow();

        returnString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice.getId(), slideshow2);
        System.out.println(returnString);
        assertTrue(returnString.equals("The media in the slideshow has mixed orientation"));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedVMToSSReturns1WhenMatch() throws Exception{
        SlideshowEntity slideshow = createHorizontalSlideshow();
        
        createHorizontalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(2L)); //the first Visual Media is in the slideshow
        ContentEntity visualMediaContent2 = visualMediaService.findOne(2L).get();

        VisualMediaInclusionEntity visualMediaInclusion2 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion2).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion2.getId().longValue(), visualMediaContent2.getId().longValue());
        
        String resultString = dimensionCheckService.checkDimensionForAssignedVisualMediaToSlideshow(visualMediaInclusion2.getId().longValue(), slideshow.getId().longValue());
        System.out.println(resultString);
        assertTrue(resultString.equals("1"));
    }
    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedVMToSSReturnsCorrectStringWhenNoMatch() throws Exception{
        //Case 1: Visual Media and Slideshow have different dimensions
        SlideshowEntity slideshow = createHorizontalSlideshow();
        
        creatVerticalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(2L));
        ContentEntity visualMediaContent2 = visualMediaService.findOne(2L).get();

        VisualMediaInclusionEntity visualMediaInclusion2 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion2).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion2.getId().longValue(), visualMediaContent2.getId().longValue());
        
        String resultString = dimensionCheckService.checkDimensionForAssignedVisualMediaToSlideshow(visualMediaInclusion2.getId().longValue(),slideshow.getId().longValue());
        assertTrue(resultString.equals("The dimension do not match:\nSlideshow orientation: horizontal" + 
            "\nVisual Media orientation: vertical"));

        //Case 2: the visual media inclusions in the slideshow already has mixed dimensions
        SlideshowEntity slideshow2 = createMixedSlideshow();
        
        createHorizontalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(3L));
        ContentEntity visualMediaContent3 = visualMediaService.findOne(3L).get();

        VisualMediaInclusionEntity visualMediaInclusion3 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion3).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion3.getId().longValue(), visualMediaContent3.getId().longValue());

        resultString = dimensionCheckService.checkDimensionForAssignedVisualMediaToSlideshow(visualMediaInclusion3.getId().longValue(), slideshow2.getId().longValue());
        assertTrue(resultString.equals("The media in the slideshow has mixed orientation"));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForDDInTSAndVMReturns1WhenMatch() throws Exception{
        TimeSlotEntity timeslot = TestDataUtil.createTimeSlotEntityWithoutContent();
        timeSlotService.save(timeslot);

        createHorizontalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();

        timeSlotService.setDisplayContent(timeslot.getId().longValue(), visualMediaContent.getId().longValue(), "visualMedia");
        
        String resultString = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(timeslot.getId().longValue());
        assertTrue(resultString.equals("1"));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForDDInTSAndSSReturns1WhenMatch() throws Exception{
        TimeSlotEntity timeslot = TestDataUtil.createTimeSlotEntityWithoutContent();
        timeSlotService.save(timeslot);

        SlideshowEntity slideshow = createHorizontalSlideshow();
        timeSlotService.setDisplayContent(timeslot.getId().longValue(), slideshow.getId().longValue(), "slideshow");
        
        String resultString = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(timeslot.getId().longValue());
        assertTrue(resultString.equals("1"));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForDDInTSAndVMReturnsCorrectStringWhenNoMatch() throws Exception{
        TimeSlotEntity timeslot = TestDataUtil.createTimeSlotEntityWithoutContent();
        timeSlotService.save(timeslot);

        creatVerticalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();

        timeSlotService.setDisplayContent(timeslot.getId().longValue(), visualMediaContent.getId().longValue(), "visualMedia");
        
        String resultString = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(timeslot.getId().longValue());
        System.out.println(resultString);
        assertTrue(resultString.equals("The dimension do not match:\nDisplay Device orientation: [horizontal]" + 
                "\nthe visual media orientation: vertical"));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForDDInTSAndSSReturnsCorrectStringWhenNoMatch() throws Exception{
        TimeSlotEntity timeslot = TestDataUtil.createTimeSlotEntityWithoutContent();
        timeSlotService.save(timeslot);

        //case 1: display devices and slideshow have different dimensions
        SlideshowEntity slideshow = createVerticalSlideshow();
        timeSlotService.setDisplayContent(timeslot.getId().longValue(), slideshow.getId().longValue(), "slideshow");
        
        String resultString = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(timeslot.getId().longValue());
        assertTrue(resultString.equals("The dimension do not match:\nDisplay Device orientation: [horizontal]" + 
                "\nthe visual media orientation: vertical"));

        //case 2: slideshow has mixed dimensions
        SlideshowEntity slideshow2 = createMixedSlideshow();
        timeSlotService.setDisplayContent(timeslot.getId().longValue(), slideshow2.getId().longValue(), "slideshow");
        
        resultString = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(timeslot.getId().longValue());
        assertTrue(resultString.equals("The dimensions of slideshow are mixed"));

    }

    private void createHorizontalVisualMediaWithFile() throws Exception{
        MockMultipartFile file = TestDataUtil.createHorizontalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private void creatVerticalVisualMediaWithFile() throws Exception{
        MockMultipartFile file = TestDataUtil.createVerticalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private SlideshowEntity createHorizontalSlideshow() throws Exception{
        createHorizontalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        VisualMediaEntity visualMediaContent1 = (VisualMediaEntity) visualMediaService.findOne(1L).get();
        //create visual media inclusion and link it to the ContentEntity
        VisualMediaInclusionEntity visualMediaInclusion1 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion1).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion1.getId().longValue(), visualMediaContent1.getId().longValue());

        SlideshowEntity slideshow = TestDataUtil.createSlideshowEntity();
        slideshowService.save(slideshow);
        slideshowService.addVisualMediaInclusion(slideshow.getId().longValue(), visualMediaInclusion1.getId().longValue());
        
        return slideshow;
    }

    private SlideshowEntity createVerticalSlideshow() throws Exception{
        creatVerticalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        VisualMediaEntity visualMediaContent1 = (VisualMediaEntity) visualMediaService.findOne(1L).get();
        //create visual media inclusion and link it to the ContentEntity
        VisualMediaInclusionEntity visualMediaInclusion1 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion1).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion1.getId().longValue(), visualMediaContent1.getId().longValue());

        SlideshowEntity slideshow = TestDataUtil.createSlideshowEntity();
        slideshowService.save(slideshow);
        slideshowService.addVisualMediaInclusion(slideshow.getId().longValue(), visualMediaInclusion1.getId().longValue());
        return slideshow;
    }

    private SlideshowEntity createMixedSlideshow() throws Exception{
        creatVerticalVisualMediaWithFile();
        MockMultipartFile file = TestDataUtil.createVerticalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
        assertTrue(visualMediaService.isExists(1L));
        VisualMediaEntity visualMediaContent1 = (VisualMediaEntity) visualMediaService.findOne(1L).get();
        //create visual media inclusion and link it to the ContentEntity
        VisualMediaInclusionEntity visualMediaInclusion1 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion1).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion1.getId().longValue(), visualMediaContent1.getId().longValue());

        createHorizontalVisualMediaWithFile();
        MockMultipartFile file2 = TestDataUtil.createHorizontalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            .file(file2
            )
        ).andExpect(MockMvcResultMatchers.status().isCreated());
        assertTrue(visualMediaService.isExists(2L));
        VisualMediaEntity visualMediaContent2 = (VisualMediaEntity) visualMediaService.findOne(2L).get();
        
        VisualMediaInclusionEntity visualMediaInclusion2 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion2).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion2.getId().longValue(), visualMediaContent2.getId().longValue());

        SlideshowEntity slideshow = TestDataUtil.createSlideshowEntity();
        slideshowService.save(slideshow);
        slideshowService.addVisualMediaInclusion(slideshow.getId().longValue(), visualMediaInclusion1.getId().longValue());
        slideshowService.addVisualMediaInclusion(slideshow.getId().longValue(), visualMediaInclusion2.getId().longValue());
        return slideshow;
    }

}