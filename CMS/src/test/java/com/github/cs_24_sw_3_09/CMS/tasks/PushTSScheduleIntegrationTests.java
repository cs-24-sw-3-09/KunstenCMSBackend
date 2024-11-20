package com.github.cs_24_sw_3_09.CMS.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
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

/*@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
public class PushTSScheduleIntegrationTests {

    private DisplayDeviceRepository displayDeviceRepository;
    private DisplayDeviceService displayDeviceService;

    @Autowired
    public PushTSScheduleIntegrationTests(DisplayDeviceRepository displayDeviceRepository,
            DisplayDeviceService displayDeviceService) {
        this.displayDeviceRepository = displayDeviceRepository;
        this.displayDeviceService = displayDeviceService;
    }*/
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class PushTSScheduleIntegrationTests {
    // Mock is a powerful way of testing controllers.
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private DisplayDeviceService displayDeviceService;
    private DisplayDeviceRepository displayDeviceRepository;

    @Autowired
    public PushTSScheduleIntegrationTests(DisplayDeviceRepository displayDeviceRepository, MockMvc mockMvc,
            ObjectMapper objectMapper,
            DisplayDeviceService displayDeviceService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.displayDeviceService = displayDeviceService;
        this.displayDeviceRepository = displayDeviceRepository;
    }

    @Test
    public void testThatOnlyGetTheDisplayDevicesThatIsConnected() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        displayDeviceService.save(displayDeviceEntity);
        displayDeviceEntity.setConnectedState(true);
        displayDeviceService.save(displayDeviceEntity);

        Iterable<DisplayDeviceEntity> ddDB = displayDeviceRepository.findConnectedDisplayDevices();
        List<DisplayDeviceEntity> connectedDevices = StreamSupport.stream(ddDB.spliterator(), false).toList();
        assertEquals(1, connectedDevices.size(), "There are not one connected screen in the DB");
    }
}
