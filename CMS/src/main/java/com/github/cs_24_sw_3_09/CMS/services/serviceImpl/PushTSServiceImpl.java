package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.List;

import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;

public class PushTSServiceImpl implements PushTSService {
    @Override
    TimeSlotDto timeSlotPrioritisationForDisplayDevice(List<TimeSlotDto> timeSlotDtoList,
            DisplayDeviceDto displayDeviceDto) {
                return new TimeSlotDto();
    }

    @Override
    void sendTimeSlotToDisplayDevice(TimeSlotDto timeSlotDto, DisplayDeviceDto displayDeviceDto){
        
    }
}
