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
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotDisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

import com.github.cs_24_sw_3_09.CMS.services.TimeSlotDisplayDeviceService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/time_slot_display_devices")
public class TimeSlotDisplayDeviceController {
    private final TimeSlotDisplayDeviceService timeSlotDisplayDeviceService 
    private Mapper<TimeSlotDisplayDeviceEntity, TimeSlotDisplayDeviceDto> timeSlotDisplayDeviceMapper;

    @Autowired
    public TimeSlotDisplayDeviceController(TimeSlotDisplayDeviceService timeSlotService,
            Mapper<TimeSlotDisplayDeviceEntity, TimeSlotDisplayDeviceDto> timeSlotDisplayDeviceMapper) {
        this.timeSlotDisplayDeviceService = timeSlotDisplayDeviceService;
        this.timeSlotDisplayDeviceMapper = timeSlotDisplayDeviceMapper;
    }

    @PostMapping
    public ResponseEntity<TimeSlotDisplayDeviceDto> createTimeSlot(@Valid @RequestBody TimeSlotDisplayDeviceDto timeSlotDisplayDevice) {

        // Done to decouple the persistence layer from the presentation and service
        // layer.
        TimeSlotDisplayDeviceEntity timeSlotDisplayDeviceEntity = timeSlotDisplayDeviceMapper.mapFrom(timeSlotDisplayDevice);
        TimeSlotDisplayDeviceEntity savedTimeSlotDisplayDeviceEntity = timeSlotDisplayDeviceService.save(timeSlotDisplayDeviceEntity);
        return new ResponseEntity<>(timeSlotDisplayDeviceMapper.mapTo(savedTimeSlotDisplayDeviceEntity), HttpStatus.CREATED);
    }


    
}
