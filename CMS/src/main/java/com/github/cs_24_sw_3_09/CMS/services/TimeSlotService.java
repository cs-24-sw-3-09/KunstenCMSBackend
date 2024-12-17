package com.github.cs_24_sw_3_09.CMS.services;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotColor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.utils.Result;

public interface TimeSlotService {
    Optional<TimeSlotEntity> save(TimeSlotEntity timeSlotEntity);

    TimeSlotEntity saveWithOnlyId(TimeSlotEntity timeSlotEntity);

    List<TimeSlotEntity> findAll();

    Page<TimeSlotEntity> findAll(Pageable pageable);

    List<TimeSlotEntity> findAll(Date start, Date end);

    Optional<TimeSlotEntity> findOne(Long id);

    boolean isExists(Long id);

    Set<TimeSlotDto> findSetOfTimeSlotsSlideshowIsAPartOf(Long id);

    Result<TimeSlotEntity, String> partialUpdate(Long id, TimeSlotEntity timeSlotEntity, Boolean forceDimensions);

    int countDisplayDeviceAssociations(Long timeSlotId);

    void delete(Long id);

    void deleteRelation(Long tsId, Long ddId);

    Result<TimeSlotEntity, String> setDisplayContent(Long tsId, Long dcId, String type, Boolean forceDimensions);
  
    Result<TimeSlotEntity, String> addDisplayDevice(Long id, Long displayDeviceId, Boolean forceDimensions) throws RuntimeException;

    List<TimeSlotEntity> findOverlappingTimeSlots(Long id);

    List<TimeSlotColor> getTimeSlotColors();
}
