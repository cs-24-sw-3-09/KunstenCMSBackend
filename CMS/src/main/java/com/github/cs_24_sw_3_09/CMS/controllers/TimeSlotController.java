package com.github.cs_24_sw_3_09.CMS.controllers;
import java.util.Optional;

import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
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
    private final Mapper<TimeSlotEntity, TimeSlotDto> timeSlotMapper;
    private final DisplayDeviceService displayDeviceService;

    @Autowired
    public TimeSlotController(
            TimeSlotService timeSlotService,
            Mapper<TimeSlotEntity, TimeSlotDto> timeSlotMapper,
            DisplayDeviceService displayDeviceService
    ) {
        this.timeSlotService = timeSlotService;
        this.timeSlotMapper = timeSlotMapper;
        this.displayDeviceService = displayDeviceService;
    }

    @PostMapping
    public ResponseEntity<TimeSlotDto> createTimeSlot(@Valid @RequestBody TimeSlotDto timeSlot) {

        // Done to decouple the persistence layer from the presentation and service
        // layer.
        TimeSlotEntity timeSlotEntity = timeSlotMapper.mapFrom(timeSlot);
        TimeSlotEntity savedTimeSlotEntity = timeSlotService.save(timeSlotEntity);
        return new ResponseEntity<>(timeSlotMapper.mapTo(savedTimeSlotEntity), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<TimeSlotDto> getTimeSlots(Pageable pageable) {
        Page<TimeSlotEntity> timeSlotEntities = timeSlotService.findAll(pageable);
        return timeSlotEntities.map(timeSlotMapper::mapTo);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TimeSlotDto> getTimeSlot(@PathVariable("id") Long id) {
        Optional<TimeSlotEntity> foundTimeSlot = timeSlotService.findOne(id);

        return foundTimeSlot.map(timeSlotEntity -> {
            TimeSlotDto timeSlotDto = timeSlotMapper.mapTo(timeSlotEntity);
            return new ResponseEntity<>(timeSlotDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<TimeSlotDto> fullUpdateTimeSlot(@PathVariable("id") Long id,
            @Valid @RequestBody TimeSlotDto timeSlotDto) {
        if (!timeSlotService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        timeSlotDto.setId(Math.toIntExact(id));
        TimeSlotEntity timeSlotEntity = timeSlotMapper.mapFrom(timeSlotDto);
        TimeSlotEntity savedTimeSlotEntity = timeSlotService.save(timeSlotEntity);
        return new ResponseEntity<>(timeSlotMapper.mapTo(savedTimeSlotEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<TimeSlotDto> partialUpdateTimeSlot(@PathVariable("id") Long id,
            @Valid @RequestBody TimeSlotDto timeSlotDto) {
        if (!timeSlotService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TimeSlotEntity timeSlotEntity = timeSlotMapper.mapFrom(timeSlotDto);
        TimeSlotEntity updatedTimeSlotEntity = timeSlotService.partialUpdate(id, timeSlotEntity);
        return new ResponseEntity<>(timeSlotMapper.mapTo(updatedTimeSlotEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteTimeSlot(@PathVariable("id") Long id) {
        if (!timeSlotService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        timeSlotService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //todo: change such that it is part of the body instead
    @DeleteMapping(path = "/{id}/display_devices/{dd_id}")
    public ResponseEntity<Object> deleteRelation(@PathVariable("id") Long tsId, @PathVariable("dd_id") Long ddId) {
        if (!timeSlotService.isExists(tsId) || !displayDeviceService.isExists(ddId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        timeSlotService.deleteRelation(tsId, ddId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
