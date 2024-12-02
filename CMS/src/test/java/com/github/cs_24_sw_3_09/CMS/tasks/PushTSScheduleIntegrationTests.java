/*package com.github.cs_24_sw_3_09.CMS.tasks;

import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.PushTSServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class PushTSScheduleIntegrationTests {

    @Test
    public void unitTestForIsTimeSlotActiveThatReturnsTrue() throws Exception {
        TimeSlotEntity timeSlot = TestDataUtil.createTimeSlotEntityWithCurrentTime();

        // Get the current date and time
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        DayOfWeek currentDay = currentDate.getDayOfWeek();
        PushTSServiceImpl pushTSServiceImpl = new PushTSServiceImpl(null, null, null);

        assertEquals(true, pushTSServiceImpl.isTimeSlotActive(timeSlot, currentDate, currentTime, currentDay),
                "Unittest for helper function did not work");
    }

    @Test
    public void unitTestForIsTimeSlotActiveThatReturnsFalse() throws Exception {
        TimeSlotEntity timeSlot = TestDataUtil.createTimeSlotEntity();

        // Get the current date and time
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        DayOfWeek currentDay = currentDate.getDayOfWeek();
        PushTSServiceImpl pushTSServiceImpl = new PushTSServiceImpl(null, null, null);

        assertEquals(false, pushTSServiceImpl.isTimeSlotActive(timeSlot, currentDate, currentTime, currentDay),
                "Unittest for helper function did not work");
    }
}*/
