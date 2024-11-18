package com.github.cs_24_sw_3_09.CMS.controllers;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/time_slots")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;
    private Mapper<TimeSlotEntity, TimeSlotDto> timeSlotMapper;

    @Autowired
    public TimeSlotController(TimeSlotService timeSlotService,
            Mapper<TimeSlotEntity, TimeSlotDto> timeSlotMapper) {
        this.timeSlotService = timeSlotService;
        this.timeSlotMapper = timeSlotMapper;
    }

    @PostMapping
    public ResponseEntity<TimeSlotDto> createTimeSlot(@Valid @RequestBody TimeSlotDto timeSlot) {

        // Done to decouple the persistence layer from the presentation and service
        // layer.
        TimeSlotEntity timeSlotEntity = timeSlotMapper.mapFrom(timeSlot);
        TimeSlotEntity savedTimeSlotEntity = timeSlotService.save(timeSlotEntity);
        return new ResponseEntity<>(timeSlotMapper.mapTo(savedTimeSlotEntity), HttpStatus.CREATED);
    }

}
