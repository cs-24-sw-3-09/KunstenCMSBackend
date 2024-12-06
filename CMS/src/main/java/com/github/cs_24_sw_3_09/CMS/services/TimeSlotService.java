package com.github.cs_24_sw_3_09.CMS.services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

public interface TimeSlotService {
    Optional<TimeSlotEntity> save(TimeSlotEntity timeSlotEntity);

    TimeSlotEntity saveWithOnlyId(TimeSlotEntity timeSlotEntity);

    List<TimeSlotEntity> findAll();

    Page<TimeSlotEntity> findAll(Pageable pageable);

    List<TimeSlotEntity> findAll(Date start, Date end);

    Optional<TimeSlotEntity> findOne(Long id);

    boolean isExists(Long id);

    Set<TimeSlotDto> findSetOfTimeSlotsSlideshowIsAPartOf(Long id);

    TimeSlotEntity partialUpdate(Long id, TimeSlotEntity timeSlotEntity);

    int countDisplayDeviceAssociations(Long timeSlotId);

    void delete(Long id);

    void deleteRelation(Long tsId, Long ddId);

    TimeSlotEntity setDisplayContent(Long tsId, Long dcId, String type);
  
    TimeSlotEntity addDisplayDevice(Long id, Long displayDeviceId) throws RuntimeException;

    List<TimeSlotEntity> findOverlappingTimeSlots(Long id);
}
