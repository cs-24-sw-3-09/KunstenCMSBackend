package com.github.cs_24_sw_3_09.CMS.tasks;

import com.corundumstudio.socketio.SocketIOClient;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.PushTSServiceImpl;
import com.github.cs_24_sw_3_09.CMS.socketConnection.SocketIOModule;

import io.socket.client.IO;
import io.socket.client.Socket;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class PushTSScheduleIntegrationTests {
    // Mock is a powerful way of testing controllers.
    private DisplayDeviceService displayDeviceService;
    private DisplayDeviceRepository displayDeviceRepository;
    private PushTSService pushTSService;
    private SocketIOModule socketIoModule;
    
        @Autowired
        public PushTSScheduleIntegrationTests(SocketIOModule socketIoModule, DisplayDeviceRepository displayDeviceRepository,
                DisplayDeviceService displayDeviceService, PushTSService pushTSService) {
            this.displayDeviceService = displayDeviceService;
            this.displayDeviceRepository = displayDeviceRepository;
            this.pushTSService = pushTSService;
            this.socketIoModule = socketIoModule;
    }

    private CountDownLatch lock = new CountDownLatch(1);

    @Test
    public void testThatDisplayDeviceConnects() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        DisplayDeviceEntity dd = displayDeviceService.save(displayDeviceEntity);

    
        Socket client = IO.socket(URI.create("http://localhost:3051"), IO.Options.builder()
        .setQuery("id=1")
        .setForceNew(true)
        .build());
        client.connect();
        System.out.println("yespspdpasdsa");

        client.on("connect", (event) -> {
            System.out.println("Connected");
            lock.countDown();
        });

        lock.await(5000, TimeUnit.MILLISECONDS);

        assertEquals(true, socketIoModule.isConnected(dd.getId()), "Socket device is not connected");
    }

    @Test
    public void testForPushTSTaskWithValid() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        List<TimeSlotEntity> listTS = new ArrayList<>();
        listTS.add(TestDataUtil.createTimeSlotEntityWithCurrentTime());
        listTS.add(TestDataUtil.createTimeSlotEntityWithCurrentTime());
        listTS.add(TestDataUtil.createTimeSlotEntityWithCurrentTime());
        displayDeviceEntity.setTimeSlots(listTS);
        displayDeviceService.save(displayDeviceEntity);

        Set<Integer> result = pushTSService.updateDisplayDevicesToNewTimeSlots();
        assertEquals(1, result.size(), "There are not one connected screen in the DB");
    }

    @Test
    public void testForPushTSTaskWithUnvalid() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        List<TimeSlotEntity> listTS = new ArrayList<>();
        listTS.add(TestDataUtil.createTimeSlotEntity());
        displayDeviceEntity.setTimeSlots(listTS);
        displayDeviceService.save(displayDeviceEntity);

        Set<Integer> result = pushTSService.updateDisplayDevicesToNewTimeSlots();
        assertEquals(0, result.size(), "There are not one connected screen in the DB");
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
