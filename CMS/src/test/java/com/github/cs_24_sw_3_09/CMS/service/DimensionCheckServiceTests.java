package com.github.cs_24_sw_3_09.CMS.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

//make test for Null and optional

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
        displayDeviceService.save(displayDevice, true).getOk();
        
        createHorizontalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();

        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice, visualMediaContent);
        assertTrue(returnString.equals("1"));
    }

    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturns1WhenSSDimensionsMatchDD() throws Exception{
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDevice, true).getOk();
        
        SlideshowEntity slideshow = createHorizontalSlideshow();

        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice, slideshow);
        assertTrue(returnString.equals("1"));
    }

    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturnsCorrectStringWhenVMDimensionsDoesNotMatchDD() throws Exception{
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDevice, true).getOk();
        
        creatVerticalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();
    
        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice, visualMediaContent);
        
        assertEquals("The dimensions do not match:\n\tDisplay Device Skærm Esbjerg has resolution: 200x100"+
                "\n\tFallback Visual Media resolution: 100x200", 
                returnString
                );
    }
    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedFallbackReturnsCorrectStringWhenSSDimensionsDoesNotMatchDD() throws Exception{
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDevice, true).getOk();

        //Case 1: slideshow and display device has different dimensions
       SlideshowEntity slideshow = createVerticalSlideshow();

        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice, slideshow);
        assertEquals(
            "The dimensions do not match:\nDisplay Device Skærm Esbjerg has resolution: 200x100" + 
            "\nFallback Slideshow orientation: 100x200", 
            returnString
        );


        //Case 2: the Visual Media Inclusions in the Slideshow has mixed dimensions
        SlideshowEntity slideshow2 = createMixedSlideshow();

        returnString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice, slideshow2);
        assertEquals(
            "The dimensions of slideshow are mixed. The resolution of the first 10 visual medias are:\n"+
            "\t100x200\n"+
            "\t200x100\n",
            returnString
        );
        
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedVMToSSReturns1WhenMatch() throws Exception{
        SlideshowEntity slideshow = createHorizontalSlideshow();
        
        createHorizontalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(3L)); //the first Visual Media is in the slideshow
        ContentEntity visualMediaContent2 = visualMediaService.findOne(3L).get();

        VisualMediaInclusionEntity visualMediaInclusion2 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion2).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion2.getId().longValue(), visualMediaContent2.getId().longValue());
        
        String resultString = dimensionCheckService.checkDimensionForAssignedVisualMediaToSlideshow(visualMediaInclusion2.getId().longValue(), slideshow.getId().longValue());
        assertTrue(resultString.equals("1"));
    }
    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForAssignedVMToSSReturnsCorrectStringWhenNoMatch() throws Exception{
        //Case 1: Visual Media and Slideshow have different dimensions
        SlideshowEntity slideshow = createHorizontalSlideshow();
        assertTrue(visualMediaService.isExists(1L));
        
        creatVerticalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(3L));
        ContentEntity visualMediaContent2 = visualMediaService.findOne(3L).get();

        VisualMediaInclusionEntity visualMediaInclusion2 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion2).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion2.getId().longValue(), visualMediaContent2.getId().longValue());
        
        String resultString = dimensionCheckService.checkDimensionForAssignedVisualMediaToSlideshow(visualMediaInclusion2.getId().longValue(),slideshow.getId().longValue());
        assertTrue(resultString.equals("The dimensions do not match:\nVisual Media resolution: 100x200" 
                +"\nSlideshow orientation: 200x100"));

        //Case 2: the visual media inclusions in the slideshow already has mixed dimensions
        SlideshowEntity slideshow2 = createMixedSlideshow();
        
        createHorizontalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(4L));
        ContentEntity visualMediaContent3 = visualMediaService.findOne(4L).get();

        VisualMediaInclusionEntity visualMediaInclusion3 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion3).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion3.getId().longValue(), visualMediaContent3.getId().longValue());

        resultString = dimensionCheckService.checkDimensionForAssignedVisualMediaToSlideshow(visualMediaInclusion3.getId().longValue(), slideshow2.getId().longValue());
        assertEquals("The dimensions of slideshow are mixed. The resolution of the first 10 visual medias are:\n" +
                        "\t200x100\n"+"\t100x200\n", resultString);
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForDDInTSAndVMReturns1WhenMatch() throws Exception{
        TimeSlotEntity timeslot = TestDataUtil.createTimeSlotEntityWithoutContent();
        timeSlotService.save(timeslot,true );
        assertTrue(timeSlotService.isExists(1L));

        createHorizontalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();

        timeSlotService.setDisplayContent(timeslot.getId().longValue(), visualMediaContent.getId().longValue(), "visualMedia", false);
        TimeSlotEntity updatedTimeSlot = timeSlotService.findOne(1L).get();

        String resultString = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(visualMediaContent, updatedTimeSlot.getDisplayDevices());
        assertEquals("1", resultString);
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForDDInTSAndSSReturns1WhenMatch() throws Exception{
        TimeSlotEntity timeslot = TestDataUtil.createTimeSlotEntityWithoutContent();
        timeSlotService.save(timeslot,true);

        SlideshowEntity slideshow = createHorizontalSlideshow();
        timeSlotService.setDisplayContent(timeslot.getId().longValue(), slideshow.getId().longValue(), "slideshow", false);

        TimeSlotEntity updatedTimeSlot = timeSlotService.findOne(1L).get();
        
        String resultString = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(slideshow, updatedTimeSlot.getDisplayDevices());
        assertTrue(resultString.equals("1"));
    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForDDInTSAndVMReturnsCorrectStringWhenNoMatch() throws Exception{
        TimeSlotEntity timeslot = TestDataUtil.createTimeSlotEntityWithoutContent();
        timeSlotService.save(timeslot,true );

        creatVerticalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();

        timeSlotService.setDisplayContent(timeslot.getId().longValue(), visualMediaContent.getId().longValue(), "visualMedia", false);
        
        TimeSlotEntity updatedTimeSlot = timeSlotService.findOne(1L).get();
        String resultString = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(visualMediaContent, updatedTimeSlot.getDisplayDevices());

        assertEquals("The dimensions do not match:\n\tDisplay Device Skærm Esbjerg1 has resolution: 200x100" + 
                "\n\tVisual Media resolution: 100x200", resultString);

    }

    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCheckDimensionForDDInTSAndSSReturnsCorrectStringWhenNoMatch() throws Exception{
        TimeSlotEntity timeslot = TestDataUtil.createTimeSlotEntityWithoutContent();
        timeSlotService.save(timeslot, true);

        //case 1: display devices and slideshow have different dimensions
        SlideshowEntity slideshow = createVerticalSlideshow();
        timeSlotService.setDisplayContent(timeslot.getId().longValue(), slideshow.getId().longValue(), "slideshow", false);
        
        TimeSlotEntity updatedTimeSlot = timeSlotService.findOne(1L).get();
        String resultString = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(slideshow, updatedTimeSlot.getDisplayDevices());
        
        assertEquals(
            "The dimensions do not match:\n"+
            "Display Device Skærm Esbjerg1 has resolution: 200x100\n"+
            "Slideshow orientation: 100x200",
            resultString
        );

        //case 2: slideshow has mixed dimensions
        SlideshowEntity slideshow2 = createMixedSlideshow();
        timeSlotService.setDisplayContent(timeslot.getId().longValue(), slideshow2.getId().longValue(), "slideshow", false);
        
        updatedTimeSlot = timeSlotService.findOne(1L).get();
        resultString = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(slideshow2, updatedTimeSlot.getDisplayDevices());
        assertEquals("The dimensions of slideshow are mixed. The resolution of the first 10 visual medias are:\n\t" +
                      "100x200\n\t" +
                      "200x100\n", 
                      resultString);
    }
    
    @Test
    @WithMockUser(roles="PLANNER")
    public void testThatCorrectStringIsReturnedWhenNull() throws Exception{
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDevice, true).getOk();

        VisualMediaEntity visualMediaWithoutPath = TestDataUtil.createVisualMediaEntityWithoutFiletype();
        visualMediaService.save(visualMediaWithoutPath);
        
        String resultString = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice, visualMediaWithoutPath);
        assertTrue(resultString.equals("File not correctly configured"));
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
        slideshowService.addVisualMediaInclusion(slideshow.getId().longValue(), visualMediaInclusion1.getId().longValue(), true);
        
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
        slideshowService.addVisualMediaInclusion(slideshow.getId().longValue(), visualMediaInclusion1.getId().longValue(), true);
        return slideshow;
    }

    private SlideshowEntity createMixedSlideshow() throws Exception{
        creatVerticalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(1L));
        VisualMediaEntity visualMediaContent1 = (VisualMediaEntity) visualMediaService.findOne(1L).get();
        //create visual media inclusion and link it to the ContentEntity
        VisualMediaInclusionEntity visualMediaInclusion1 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion1).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion1.getId().longValue(), visualMediaContent1.getId().longValue());

        createHorizontalVisualMediaWithFile();
        assertTrue(visualMediaService.isExists(4L));
        VisualMediaEntity visualMediaContent2 = (VisualMediaEntity) visualMediaService.findOne(4L).get();
        
        VisualMediaInclusionEntity visualMediaInclusion2 = TestDataUtil.createVisualMediaInclusionEntity();
        visualMediaInclusionService.save(visualMediaInclusion2).get();
        visualMediaInclusionService.setVisualMedia(visualMediaInclusion2.getId().longValue(), visualMediaContent2.getId().longValue());

        SlideshowEntity slideshow = TestDataUtil.createSlideshowEntity();
        slideshowService.save(slideshow);
        slideshowService.addVisualMediaInclusion(slideshow.getId().longValue(), visualMediaInclusion1.getId().longValue(), true);
        slideshowService.addVisualMediaInclusion(slideshow.getId().longValue(), visualMediaInclusion2.getId().longValue(), true);
        return slideshow;
    }

    @Test
    @WithMockUser(roles = { "PLANNER" })
    public void testThatOrientationMessageWorksWhenPatchingAndChangingDisplayDevices() throws Exception {
            
    TimeSlotEntity timeSlot = TestDataUtil.createTimeSlotEntityWithoutContent();

    assertEquals(
            1, 
            timeSlot.getDisplayDevices().size()
    );
    
    creatVerticalVisualMediaWithFile();


    TimeSlotEntity tsToSend = timeSlotService.save(timeSlot, true).getOk();
    DisplayDeviceEntity dd = TestDataUtil.createSecDisplayDeviceEntity();
    dd.setDisplayOrientation("horizontal");
    displayDeviceService.save(dd, true).getOk();

    assertTrue(timeSlotService.isExists(1L));
    assertTrue(visualMediaService.isExists(1L));
    assertTrue(displayDeviceService.isExists(1L));
    assertTrue(displayDeviceService.isExists(2L));

    
    tsToSend.getDisplayDevices().clear();
    String json = TestDataUtil.createTSJsonWithDDIds(objectMapper.writeValueAsString(tsToSend), 2);
    json = TestDataUtil.createTSJsonWithDCIds(json, "1", "visualMedia");


    String res = mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/time_slots/1")
            .param("forceDimensions", "false")    
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
    ).andExpect(
            MockMvcResultMatchers.status().isConflict()
    ).andReturn().getResponse().getContentAsString();

    assert(!res.contains("null"));

    }

    @Test
    @WithMockUser(roles = { "PLANNER" })
    public void testMessageWhenDisplayDevicesDimensionsDontMatch() throws Exception {
            
        TimeSlotEntity timeSlot = TestDataUtil.createTimeSlotEntityWithOutDisplayDevice();
        DisplayDeviceEntity dd1 = TestDataUtil.createDisplayDeviceEntity();
        dd1.setDisplayOrientation("vertical");
        dd1.setName("screen1");
        timeSlot.getDisplayDevices().add(dd1);
        DisplayDeviceEntity dd2 = TestDataUtil.createDisplayDeviceEntity("screen2");
        timeSlot.getDisplayDevices().add(dd2);

        TimeSlotEntity tsToSend = timeSlotService.save(timeSlot, true).getOk();

        assertEquals(2, tsToSend.getDisplayDevices().size());
        assertTrue(timeSlotService.isExists(1L));
        assertTrue(displayDeviceService.isExists(1L));
        assertTrue(displayDeviceService.isExists(2L));
        assertTrue(slideshowService.isExists((long) timeSlot.getDisplayContent().getId()));

        String res = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(tsToSend.getDisplayContent(), tsToSend.getDisplayDevices());

        assertEquals("The dimensions of display devices are mixed:\n\tscreen1: 200x100\n\tscreen2: 1920x1080", res);
        

    }

    @Test
    @WithMockUser(roles = { "PLANNER" })
    public void testMessageWhenDisplayDevicesAndContentDimensionsDontMatch() throws Exception {
            
        TimeSlotEntity timeSlot = TestDataUtil.createTimeSlotEntityWithoutContent();

        creatVerticalVisualMediaWithFile();


        TimeSlotEntity tsToSend = timeSlotService.save(timeSlot, true).getOk();

        DisplayDeviceEntity dd1 = TestDataUtil.createDisplayDeviceEntity();
        dd1.setName("screen1");
        DisplayDeviceEntity dd2 = TestDataUtil.createDisplayDeviceEntity();
        dd2.setName("screen2");

        displayDeviceService.save(dd1, true);
        displayDeviceService.save(dd2, true);

        assertTrue(timeSlotService.isExists(1L));
        assertTrue(visualMediaService.isExists(1L));
        assertTrue(displayDeviceService.isExists(2L));
        assertTrue(displayDeviceService.isExists(3L));

        
        tsToSend.getDisplayDevices().clear();
        String json = TestDataUtil.createTSJsonWithDDIds(objectMapper.writeValueAsString(tsToSend), 2, 3);
        json = TestDataUtil.createTSJsonWithDCIds(json, "1", "visualMedia");

        String res = mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/time_slots/1")
                .param("forceDimensions", "false")    
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(
                MockMvcResultMatchers.status().isConflict()
        ).andReturn().getResponse().getContentAsString();

        assertEquals("The dimensions do not match:\n\tDisplay Device screen2 has resolution: 200x100\n\tVisual Media resolution: 100x200", res);
     
    }
}