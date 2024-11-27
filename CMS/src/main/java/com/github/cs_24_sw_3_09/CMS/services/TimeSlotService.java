package com.github.cs_24_sw_3_09.CMS.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

public interface TimeSlotService {
    TimeSlotEntity save(TimeSlotEntity timeSlotEntity);    
    
    List<TimeSlotEntity> findAll();

    Page<TimeSlotEntity> findAll(Pageable pageable);

    Optional<TimeSlotEntity> findOne(Long id);

    boolean isExists(Long id);

    Set<TimeSlotDto> findSetOfTimeSlotsSlideshowIsAPartOf(Long id);

    TimeSlotEntity partialUpdate(Long id, TimeSlotEntity timeSlotEntity);

    void delete(Long id);

    void deleteRelation(Long tsId, Long ddId);
} 
