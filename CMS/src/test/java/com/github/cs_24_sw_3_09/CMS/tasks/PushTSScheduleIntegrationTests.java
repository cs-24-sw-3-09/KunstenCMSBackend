package com.github.cs_24_sw_3_09.CMS.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.PushTSServiceImpl;

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
public class PushTSScheduleIntegrationTests {
    // Mock is a powerful way of testing controllers.
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private DisplayDeviceService displayDeviceService;
    private TimeSlotService timeSlotService;
    private DisplayDeviceRepository displayDeviceRepository;
    private PushTSService pushTSService;

    @Autowired
    public PushTSScheduleIntegrationTests(DisplayDeviceRepository displayDeviceRepository, MockMvc mockMvc,
            ObjectMapper objectMapper,
            DisplayDeviceService displayDeviceService, TimeSlotService timeSlotService, PushTSService pushTSService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.displayDeviceService = displayDeviceService;
        this.displayDeviceRepository = displayDeviceRepository;
        this.timeSlotService = timeSlotService;
        this.pushTSService = pushTSService;
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

    @Test
    public void testForPushTSTaskWithValid() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        List<TimeSlotEntity> listTS = new ArrayList<>();
        listTS.add(TestDataUtil.createTimeSlotEntityWithCurrentTime());
        listTS.add(TestDataUtil.createTimeSlotEntityWithCurrentTime());
        listTS.add(TestDataUtil.createTimeSlotEntityWithCurrentTime());
        displayDeviceEntity.setTimeSlots(listTS);
        displayDeviceEntity.setConnectedState(true);
        displayDeviceService.save(displayDeviceEntity);

        assertEquals(1, pushTSService.updateDisplayDevicesToNewTimeSlots(),
                "There are not one connected screen in the DB");
    }

    @Test
    public void testForPushTSTaskWithUnvalid() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        List<TimeSlotEntity> listTS = new ArrayList<>();
        listTS.add(TestDataUtil.createTimeSlotEntity());
        displayDeviceEntity.setTimeSlots(listTS);
        displayDeviceEntity.setConnectedState(true);
        displayDeviceService.save(displayDeviceEntity);

        assertEquals(0, pushTSService.updateDisplayDevicesToNewTimeSlots(),
                "There are not one connected screen in the DB");
    }

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
}
