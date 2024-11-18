package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.TimeSlotRepository;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    
    private TimeSlotRepository timeSlotRepository;

    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }
    
    @Override
    public TimeSlotEntity save(TimeSlotEntity timeSlotEntity) {
        return timeSlotRepository.save(timeSlotEntity);
    }
}
