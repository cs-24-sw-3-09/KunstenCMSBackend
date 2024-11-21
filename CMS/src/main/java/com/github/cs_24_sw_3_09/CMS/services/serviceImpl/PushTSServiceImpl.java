package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;

import jakarta.transaction.Transactional;

import org.hibernate.Hibernate;
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
        // Fetch the list of connected display devices
        // List<DisplayDeviceEntity> devices =
        // System.out.println(displayDeviceRepository.findConnectedDisplayDevices());

    }
}
