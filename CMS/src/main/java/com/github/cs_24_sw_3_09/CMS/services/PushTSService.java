package com.github.cs_24_sw_3_09.CMS.services;

import java.util.List;
import java.util.Set;

import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

public interface PushTSService {
    // This method is to figure out what TS have the highest prioritisation for a DD
    TimeSlotEntity timeSlotPrioritisationForDisplayDevice(List<TimeSlotEntity> timeSlotList,
            DisplayDeviceDto displayDeviceDto);

    // This method sends a TS or fallback content to a DD over the socket connection.
    void sendTimeSlotToDisplayDevice(TimeSlotEntity timeSlotEntity, DisplayDeviceEntity displayDeviceEntity);

    void sendTimeSlotToDisplayDevice(ContentEntity contentEntity, DisplayDeviceEntity displayDeviceEntity);

    // This method is to get all TS and DD and figure out and send the new TS for each DD
    Set<Integer> updateDisplayDevicesToNewTimeSlots(boolean sendToDisplayDevices);

    Set<Integer> updateDisplayDevicesToNewTimeSlots();
}