package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.List;

import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import org.springframework.stereotype.Service;

@Service
public class PushTSServiceImpl implements PushTSService {

    private DisplayDeviceRepository displayDeviceRepository;

    public PushTSServiceImpl(DisplayDeviceRepository displayDeviceRepository) {
        this.displayDeviceRepository = displayDeviceRepository;
    }
    /*
     * @Override
     * public TimeSlotDto timeSlotPrioritisationForDisplayDevice(List<TimeSlotDto>
     * timeSlotDtoList,
     * DisplayDeviceDto displayDeviceDto) {
     * return new TimeSlotDto();
     * }
     * 
     * @Override
     * public void sendTimeSlotToDisplayDevice(TimeSlotDto timeSlotDto,
     * DisplayDeviceDto displayDeviceDto) {
     * 
     * }
     */

    @Override
    public void updateDisplayDevicesToNewTimeSlots() {
        System.out.println("he");
        System.out.println(displayDeviceRepository.findConnectedDisplayDevices());
    }
}
