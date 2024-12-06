package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;

import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import com.github.cs_24_sw_3_09.CMS.utils.ContentUtils;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/display_devices")
public class DisplayDeviceController {

    private final DisplayDeviceService displayDeviceService;
    private final VisualMediaService visualMediaService;
    private final SlideshowService slideshowService;
    private final TimeSlotService timeSlotService;
    private Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper;
    private ContentUtils contentUtils;

    @Autowired
    public DisplayDeviceController(DisplayDeviceService displayDeviceService,
            Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper,
            VisualMediaService visualMediaService, SlideshowService slideshowService,
            ContentUtils contentUtils, TimeSlotService timeSlotService) {
        this.displayDeviceService = displayDeviceService;
        this.displayDeviceMapper = displayDeviceMapper;
        this.visualMediaService = visualMediaService;
        this.contentUtils = contentUtils;
        this.slideshowService = slideshowService;
        this.timeSlotService = timeSlotService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DisplayDeviceDto> createDisplayDevice(@Valid @RequestBody DisplayDeviceDto displayDevice) {
        DisplayDeviceEntity displayDeviceEntity = displayDeviceMapper.mapFrom(displayDevice);
        Optional<DisplayDeviceEntity> savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity);
        
        if (savedDisplayDeviceEntity.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
        return new ResponseEntity<>(displayDeviceMapper.mapTo(savedDisplayDeviceEntity.get()), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<DisplayDeviceDto> getDisplayDevices(Pageable pageable) {
        Page<DisplayDeviceEntity> displayDeviceEntities = displayDeviceService.findAll(pageable);
        return displayDeviceEntities.map(displayDeviceMapper::mapTo);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DisplayDeviceDto> getDisplayDevice(@PathVariable("id") Long id) {
        Optional<DisplayDeviceEntity> foundDisplayDevice = displayDeviceService.findOne(id);

        return foundDisplayDevice.map(displayDeviceEntity -> {
            DisplayDeviceDto displayDeviceDto = displayDeviceMapper.mapTo(displayDeviceEntity);
            return new ResponseEntity<>(displayDeviceDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DisplayDeviceDto> fullUpdateDisplayDevice(@PathVariable("id") Long id,
            @Valid @RequestBody DisplayDeviceDto displayDeviceDto) {
        if (!displayDeviceService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        displayDeviceDto.setId(Math.toIntExact(id));
        DisplayDeviceEntity displayDeviceEntity = displayDeviceMapper.mapFrom(displayDeviceDto);

        Optional<DisplayDeviceEntity> savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity);
        if (savedDisplayDeviceEntity.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        return new ResponseEntity<>(displayDeviceMapper.mapTo(savedDisplayDeviceEntity.get()), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DisplayDeviceDto> partialUpdateDisplayDevice(@PathVariable("id") Long id,
            @Valid @RequestBody DisplayDeviceDto displayDeviceDto) {
        if (!displayDeviceService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        DisplayDeviceEntity displayDeviceEntity = displayDeviceMapper.mapFrom(displayDeviceDto);
        DisplayDeviceEntity updatedDisplayDeviceEntity = displayDeviceService.partialUpdate(id, displayDeviceEntity);
        return new ResponseEntity<>(displayDeviceMapper.mapTo(updatedDisplayDeviceEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteDisplayDevice(@PathVariable("id") Long id) {
        if (!displayDeviceService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        displayDeviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{id}/fallbackContent")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<DisplayDeviceDto> setFallbackContent(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> requestBody) {

        // Validate input and extract fallbackId
        if (!requestBody.containsKey("fallbackId")) {
            return ResponseEntity.badRequest().build();
        }

        // check if is a number
        Long fallbackId;
        try {
            fallbackId = Long.valueOf(requestBody.get("fallbackId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        // Get content type and validate existence of dd and content
        String type = contentUtils.getContentTypeById(Math.toIntExact(fallbackId));
        if (type == null || !contentUtils.isFallbackContentValid(type, fallbackId)
                || !displayDeviceService.isExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Update the display device and return the response
        DisplayDeviceEntity updatedDisplayDeviceEntity = displayDeviceService.setFallbackContent(id, fallbackId, type);

        return ResponseEntity.ok(displayDeviceMapper.mapTo(updatedDisplayDeviceEntity));
    }

    @PatchMapping(path = "/{id}/time_slots")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<DisplayDeviceDto> addTimeSlot(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> requestBody) {

        // Validate input and extract fallbackId
        if (!requestBody.containsKey("timeSlotId")) {
            return ResponseEntity.badRequest().build();
        }

        // check if is a number
        Long timeslotId;
        try {
            timeslotId = Long.valueOf(requestBody.get("timeSlotId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        // Get content type and validate existence of dd and content
        if (!displayDeviceService.isExists(id) || !timeSlotService.isExists(timeslotId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Update the display device and return the response
        DisplayDeviceEntity updatedDisplayDeviceEntity = displayDeviceService.addTimeSlot(id, timeslotId);

        return ResponseEntity.ok(displayDeviceMapper.mapTo(updatedDisplayDeviceEntity));
    }

    @PatchMapping(path = "/{id}/fallback")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<DisplayDeviceDto> addFallback(@PathVariable("id") Long id,
                                                 @RequestBody Map<String, Object> requestBody) {
        Long fallbackId = ((Integer) requestBody.get("fallbackId")).longValue();

        if (!displayDeviceService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<DisplayDeviceEntity> updatedDisplayDevice = displayDeviceService.addFallback(id, fallbackId);

        // If fallback content was not found, updatedDisplayDevice will be empty.
        if (updatedDisplayDevice.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(displayDeviceMapper.mapTo(updatedDisplayDevice.get()), HttpStatus.OK);
    }    
}