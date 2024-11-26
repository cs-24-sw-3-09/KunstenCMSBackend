package com.github.cs_24_sw_3_09.CMS.tasks;

import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.services.CleanUpDataBaseService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SetAllScreensToDisconnectedIntegreationTests {
    private DisplayDeviceService displayDeviceService;
    private MockMvc mockMvc;

    @Autowired
    public SetAllScreensToDisconnectedIntegreationTests(DisplayDeviceService displayDeviceService, MockMvc mockMvc) {
        this.displayDeviceService = displayDeviceService;
        this.mockMvc = mockMvc;
    }

    @Test
    public void testForTaskForSettingAllDDToNotConnected() throws Exception {
        DisplayDeviceEntity dd = TestDataUtil.createDisplayDeviceEntity();
        dd.setConnectedState(true);
        displayDeviceService.save(dd);

        displayDeviceService.disconnectAllScreens();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/display_devices/1")).andExpect(
                        MockMvcResultMatchers.jsonPath("connectedState").value(false))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }
}
