package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;

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
        System.out.println("hej");
        // Fetch the list of connected display devices
        List<DisplayDeviceEntity> devices = displayDeviceRepository.findConnectedDisplayDevices();

        if (!devices.isEmpty()) {
            List<TimeSlotEntity> timeSlots = devices.get(0).getTimeSlots();
            System.out.println("++++++");
            // Iterate over the set
            for (TimeSlotEntity timeSlot : timeSlots) {
                System.out.println(timeSlot.getName());
                System.out.println("-----");
            }
        } else {
            System.out.println("No connected display devices found.");
        }
    }
}
