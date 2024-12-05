package com.github.cs_24_sw_3_09.CMS.service;

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
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
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
    public void testThatCheckDimensionForAssignedFallbackReturns1WhenDimensionsMatch() throws Exception {
        DisplayDeviceEntity displayDevice = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity savedDisplayDevice = displayDeviceService.save(displayDevice).get();
        
        //Visual Media 
        MockMultipartFile horizontalFile = TestDataUtil.createHorizontalImage();
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/visual_medias")
            //.file("content", horizontalFile.getBytes())
            .file(
                "file", 
                horizontalFile.getBytes()
            ).param("name", "name1")
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        

        MockMultipartFile file = TestDataUtil.createVisualMediaFile(); 
        System.out.println(file.getOriginalFilename() + " " + file.getContentType());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/visual_medias")
        .file(
            file
            )
        )
        .andExpect(MockMvcResultMatchers.status().isCreated());
        
        assertTrue(visualMediaService.isExists(2L));
        ContentEntity vm = visualMediaService.findOne(2L).get();
        System.out.println("IMPORTANT: "+ ((VisualMediaEntity) vm).getName() + " " + ((VisualMediaEntity) vm).getFileType() + " "+((VisualMediaEntity) vm).getLocation());
        
        assertTrue(visualMediaService.isExists(1L));
        //VisualMediaEntity vm = visualMediaService.findOne(1L).get();

        //System.out.println(vm.getName() + " " + vm.getId() + " " + vm.getFileType());

        ContentEntity visualMediaContent = visualMediaService.findOne(1L).get();
        System.out.println(((VisualMediaEntity) visualMediaContent).getName() + " " + ((VisualMediaEntity) visualMediaContent).getFileType() + " "+((VisualMediaEntity) visualMediaContent).getLocation());
        /*for (VisualMediaEntity vm : visualMediaService.findAll()) {
            System.out.println(vm.getFileType() + " "+vm.getLocation());
        }*/
    
        String returnString = dimensionCheckService.checkDimensionForAssignedFallback(savedDisplayDevice.getId(), visualMediaContent);

        System.out.println(returnString);
        assertTrue(returnString.equals("1"));
    }
    
}