package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
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
public class DisplayDeviceControllerIntegrationTests {
    //Mock is a powerful way of testing controllers.
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private DisplayDeviceService displayDeviceService;

    @Autowired
    public DisplayDeviceControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, DisplayDeviceService displayDeviceService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.displayDeviceService = displayDeviceService;
    }

    @Test
    public void testThatCreateDisplayDeviceSuccessfullyReturnsHttp201Created() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        String displayDeviceJson = objectMapper.writeValueAsString(displayDeviceEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/display_devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(displayDeviceJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateDisplayDeviceSuccessfullyReturnsSavedDisplayDevice() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        String displayDeviceJson = objectMapper.writeValueAsString(displayDeviceEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/display_devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(displayDeviceJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Skærm Esbjerg")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location").value("Esbjerg")
        );
    }

    @Test
    public void testThatGetDisplayDeviceSuccessfullyReturnsHttp200() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        String displayDeviceJson = objectMapper.writeValueAsString(displayDeviceEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/display_devices")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetDisplayDeviceSuccessfullyReturnsListOfVisualMedia() throws Exception {
        DisplayDeviceEntity testDisplayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(testDisplayDeviceEntity);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/display_devices")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].name").value(testDisplayDeviceEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].location").value(testDisplayDeviceEntity.getLocation())
        );
    }

    @Test
    public void testThatGetDisplayDeviceReturnsStatus200WhenDisplayDeviceExists() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDeviceEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/display_devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetDisplayDeviceReturnsStatus404WhenNoDisplayDeviceExists() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDeviceEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/display_devices/100000")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetDisplayDeviceReturnsDisplayDeviceWhenDisplayDeviceExists() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDeviceEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/display_devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(displayDeviceEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location").value(displayDeviceEntity.getLocation())
        );
    }

    @Test
    public void testThatFullUpdateDisplayDeviceReturnsStatus404WhenNoDisplayDeviceExists() throws Exception {
        DisplayDeviceDto displayDeviceDto = TestDataUtil.createDisplayDeviceDto();
        String displayDeviceDtoJson = objectMapper.writeValueAsString(displayDeviceDto);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/display_devices/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(displayDeviceDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateDisplayDeviceReturnsStatus200WhenDisplayDeviceExists() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity);

        DisplayDeviceDto displayDeviceDto = TestDataUtil.createDisplayDeviceDto();
        String displayDeviceDtoJson = objectMapper.writeValueAsString(displayDeviceDto);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/display_devices/" + savedDisplayDeviceEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(displayDeviceDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingDisplayDevice() throws Exception {
       // Not yet implemented.
    }

}
