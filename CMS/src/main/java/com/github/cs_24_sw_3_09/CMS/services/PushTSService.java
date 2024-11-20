package com.github.cs_24_sw_3_09.CMS.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;

public interface PushTSService {
    // This method is to figure out what TS have the highest prioritisation for a DD
    TimeSlotDto timeSlotPrioritisationForDisplayDevice(List<TimeSlotDto> timeSlotDtoList,
            DisplayDeviceDto displayDeviceDto);

    // This method sends a TS to a DD over the socket connection.
    void sendTimeSlotToDisplayDevice(TimeSlotDto timeSlotDto, DisplayDeviceDto displayDeviceDto);

    // This method is to get all TS and DD and figure out and send the new TS for
    // each DD
    void updateDisplayDevicesToNewTimeSlots();
}