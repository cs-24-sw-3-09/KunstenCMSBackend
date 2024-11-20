package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.List;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushTSServiceImpl implements PushTSService {

    private Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper;
    private DisplayDeviceRepository displayDeviceRepository;

    public PushTSServiceImpl(DisplayDeviceRepository displayDeviceRepository,
            Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper) {
        this.displayDeviceRepository = displayDeviceRepository;
        this.displayDeviceMapper = displayDeviceMapper;

    }

    @Override
    public TimeSlotDto timeSlotPrioritisationForDisplayDevice(List<TimeSlotDto> timeSlotDtoList,
            DisplayDeviceDto displayDeviceDto) {
        TimeSlotDto prioTimeSlot = null;

        return prioTimeSlot;
    }

    @Override
    public void sendTimeSlotToDisplayDevice(TimeSlotDto timeSlotDto,
            DisplayDeviceDto displayDeviceDto) {

    }

    @Override
    public void updateDisplayDevicesToNewTimeSlots() {
        System.out.println("he");
        // Gets all relevant DD and TS
        Iterable<DisplayDeviceEntity> displayDeviceEntities = displayDeviceRepository.findConnectedDisplayDevices();
        // TODO Get all of the relevant TS

        for (DisplayDeviceEntity displayDeviceEntity : displayDeviceEntities) {
            // Process each entity
            System.out.println("Processing: " + displayDeviceEntity);
        }

    }
}
