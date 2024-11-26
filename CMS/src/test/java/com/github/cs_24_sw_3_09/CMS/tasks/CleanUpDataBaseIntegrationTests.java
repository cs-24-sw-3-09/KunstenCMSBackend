package com.github.cs_24_sw_3_09.CMS.tasks;

import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.services.CleanUpDataBaseService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
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
public class CleanUpDataBaseIntegrationTests {
    private CleanUpDataBaseService cleanUpDataBaseService;
    private TimeSlotService timeSlotService;
    private MockMvc mockMvc;

    @Autowired
    public CleanUpDataBaseIntegrationTests(CleanUpDataBaseService cleanUpDataBaseService,
            TimeSlotService timeSlotService, MockMvc mockMvc) {
        this.cleanUpDataBaseService = cleanUpDataBaseService;
        this.timeSlotService = timeSlotService;
        this.mockMvc = mockMvc;
    }

    @Test
    @WithMockUser
    public void testThatDeleteTSWithoutDDDoesNotDeleteValidTS() throws Exception {
        TimeSlotEntity testTimeSlotEntity = TestDataUtil.createTimeSlotEntity();
        timeSlotService.save(testTimeSlotEntity);

        int i = cleanUpDataBaseService.deleteTSWithoutDD();
        assertEquals(0, i, "Did not not delete TS");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/time_slots")).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].id").isNumber()).andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testThatDeleteTSWithoutDDDoesDeleteValidTS() throws Exception {
        TimeSlotEntity testTimeSlotEntity = TestDataUtil.createTimeSlotEntityWithOutDisplayDevice();
        timeSlotService.save(testTimeSlotEntity);

        int i = cleanUpDataBaseService.deleteTSWithoutDD();
        assertEquals(1, i, "Did not not delete TS");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/time_slots")).andExpect(
                MockMvcResultMatchers.jsonPath("numberOfElements").value(0))
                .andExpect(
                        MockMvcResultMatchers.status().isOk());
    }

}
