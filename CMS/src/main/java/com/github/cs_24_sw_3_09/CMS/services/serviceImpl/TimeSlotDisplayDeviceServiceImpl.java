package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotDisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.TimeSlotDisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotDisplayDeviceService;

public class TimeSlotDisplayDeviceServiceImpl implements TimeSlotDisplayDeviceService {

    private TimeSlotDisplayDeviceRepository timeSlotDisplayDeviceRepository;

    public TimeSlotDisplayDeviceServiceImpl(TimeSlotDisplayDeviceRepository timeSlotDisplayDeivceRepository) {
        this.timeSlotDisplayDeviceRepository = timeSlotDisplayDeivceRepository;
    }
    
    @Override
    public TimeSlotDisplayDeviceEntity save(TimeSlotDisplayDeviceEntity timeSlotDisplayDeviceEntity) {
        return timeSlotDisplayDeviceRepository.save(timeSlotDisplayDeviceEntity);
    }
}
