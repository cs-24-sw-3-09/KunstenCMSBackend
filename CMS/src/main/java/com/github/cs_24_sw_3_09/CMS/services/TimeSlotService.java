package com.github.cs_24_sw_3_09.CMS.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

public interface TimeSlotService {
    TimeSlotEntity save(TimeSlotEntity timeSlotEntity);          
} 
