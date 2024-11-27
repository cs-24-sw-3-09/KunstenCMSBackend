/*package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.socketConnection.SocketIOModule;

import org.springframework.stereotype.Service;

@Service
public class PushTSServiceImpl implements PushTSService {

    private Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper;
    private DisplayDeviceRepository displayDeviceRepository;
    private SocketIOModule socketIOModule;

    public PushTSServiceImpl(DisplayDeviceRepository displayDeviceRepository,
            Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper, SocketIOModule socketIOModule) {
        this.displayDeviceRepository = displayDeviceRepository;
        this.displayDeviceMapper = displayDeviceMapper;
        this.socketIOModule = socketIOModule;
    }

    @Override
    public TimeSlotEntity timeSlotPrioritisationForDisplayDevice(List<TimeSlotEntity> timeSlotList,
            DisplayDeviceDto displayDeviceDto) {
        List<TimeSlotEntity> currentTimeSlots = new ArrayList<>();

        // Get the current date and time
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        DayOfWeek currentDay = currentDate.getDayOfWeek();

        // Check if current date and time fall within the TimeSlotEntity's range for all
        // timeSlots
        for (TimeSlotEntity timeSlot : timeSlotList) {
            if (isTimeSlotActive(timeSlot, currentDate, currentTime, currentDay)) {
                currentTimeSlots.add(timeSlot);
            }
        }

        // Figure out what timeslot is the one to be prioistraied
        TimeSlotEntity prio = null;
        for (TimeSlotEntity timeSlot : currentTimeSlots) {
            if (prio == null || calculateDaysCovered(timeSlot) <= calculateDaysCovered(prio)) {
                prio = timeSlot;
            }
        }

        return prio;
    }

    public long calculateDaysCovered(TimeSlotEntity timeSlot) {
        LocalDate startDate = timeSlot.getStartDate().toLocalDate();
        LocalDate endDate = timeSlot.getEndDate().toLocalDate();

        // Calculate the difference in days between the start and end dates
        return ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 to include the start date
    }

    public boolean isTimeSlotActive(TimeSlotEntity timeSlot, LocalDate currentDate, LocalTime currentTime,
            DayOfWeek currentDay) {
        LocalDate startDate = timeSlot.getStartDate().toLocalDate();
        LocalDate endDate = timeSlot.getEndDate().toLocalDate();
        LocalTime startTime = timeSlot.getStartTime().toLocalTime();
        LocalTime endTime = timeSlot.getEndTime().toLocalTime();

        // Check if the current date is within the start and end date range
        boolean isDateInRange = !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);

        // Check if the current time is within the start and end time range
        boolean isTimeInRange = !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);

        // Check if the current day is in the weekdaysChosen bitmask
        boolean isDayInRange = (timeSlot.getWeekdaysChosen() & (1 << (currentDay.getValue() - 1))) != 0;

        return isDateInRange && isTimeInRange && isDayInRange;
    }

    @Override
    public void sendTimeSlotToDisplayDevice(TimeSlotEntity timeSlotEntity, DisplayDeviceEntity displayDeviceEntity) {
        socketIOModule.sendContent(displayDeviceEntity.getId(), timeSlotEntity.getDisplayContent());
    }

    @Override
    public void sendTimeSlotToDisplayDevice(ContentEntity contentEntity, DisplayDeviceEntity displayDeviceEntity) {
        socketIOModule.sendContent(displayDeviceEntity.getId(), contentEntity);
    }

    public Set<Integer> updateDisplayDevicesToNewTimeSlots() {
        return updateDisplayDevicesToNewTimeSlots(true);
    }

    /*@Override
    public Set<Integer> updateDisplayDevicesToNewTimeSlots(boolean sendToDisplayDevices) {
        // Fetch the list of connected display devices
        List<DisplayDeviceEntity> displayDevices = displayDeviceRepository.findConnectedDisplayDevices();
        // Holder for the TS id's that is shown
        Set<Integer> timeSlotsInUse = new HashSet<>();
        for (DisplayDeviceEntity dd : displayDevices) {
            List<TimeSlotEntity> timeSlots = dd.getTimeSlots();

            TimeSlotEntity timeSlotToBeDisplayed = timeSlotPrioritisationForDisplayDevice(timeSlots,
                    displayDeviceMapper.mapTo(dd));
            if (timeSlotToBeDisplayed == null) {
                System.out.println("PRIO for " + dd.getId() + ": null");
                if (sendToDisplayDevices)
                    sendTimeSlotToDisplayDevice(dd.getFallbackContent(), dd);
            } else {
                System.out.println("PRIO for ddId " + dd.getId() + ": " + timeSlotToBeDisplayed.getName());
                if (sendToDisplayDevices)
                    sendTimeSlotToDisplayDevice(timeSlotToBeDisplayed, dd);
                timeSlotsInUse.add(timeSlotToBeDisplayed.getId());
            }
        }
        return timeSlotsInUse;
    }
}
*/
