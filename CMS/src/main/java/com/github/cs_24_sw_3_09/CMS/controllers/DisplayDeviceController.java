package com.github.cs_24_sw_3_09.CMS.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import com.github.cs_24_sw_3_09.CMS.utils.ContentUtils;
import com.github.cs_24_sw_3_09.CMS.utils.Result;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/display_devices")
public class DisplayDeviceController {

    private final DisplayDeviceService displayDeviceService;
    private final TimeSlotService timeSlotService;
    private Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper;
    private ContentUtils contentUtils;
    private final DimensionCheckService dimensionCheckService;
    private final VisualMediaService visualMediaService;
    private final SlideshowService slideshowService;

    @Autowired
    public DisplayDeviceController(DisplayDeviceService displayDeviceService,
            Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper,
            ContentUtils contentUtils, TimeSlotService timeSlotService,
            DimensionCheckService dimensionCheckService,
            VisualMediaService visualMediaService,
            SlideshowService slideshowService) {
        this.displayDeviceService = displayDeviceService;
        this.displayDeviceMapper = displayDeviceMapper;
        this.contentUtils = contentUtils;
        this.timeSlotService = timeSlotService;
        this.dimensionCheckService = dimensionCheckService;
        this.visualMediaService = visualMediaService;
        this.slideshowService = slideshowService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createDisplayDevice(@Valid @RequestBody DisplayDeviceDto displayDevice,
            @RequestParam(value = "forceDimensions", required = false) Boolean forceDimensions ) {
        
        DisplayDeviceEntity displayDeviceEntity = displayDeviceMapper.mapFrom(displayDevice);
        Result<DisplayDeviceEntity, String> savedDisplayDeviceEntity = displayDeviceService.save(
            displayDeviceEntity, 
            forceDimensions != null ? forceDimensions : false
        );
        
        if (savedDisplayDeviceEntity.isErr()) {
            return switch(savedDisplayDeviceEntity.getErr().toLowerCase()) {
                case "not found" ->  new ResponseEntity<>(HttpStatus.NOT_FOUND); 
                default -> new ResponseEntity<>(savedDisplayDeviceEntity.getErr(), HttpStatus.CONFLICT);
            };
        }
        
        return new ResponseEntity<>(displayDeviceMapper.mapTo(savedDisplayDeviceEntity.getOk()), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<DisplayDeviceDto> getDisplayDevices(Pageable pageable) {
        Page<DisplayDeviceEntity> displayDeviceEntities = displayDeviceService.findAll(pageable);
        return displayDeviceEntities.map(displayDeviceMapper::mapTo);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<DisplayDeviceDto>> getAllDisplayDevices() {
        List<DisplayDeviceEntity> displayDeviceEntity = displayDeviceService.findAll();
        return ResponseEntity.ok(displayDeviceEntity.stream().map(displayDeviceMapper::mapTo).toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DisplayDeviceDto> getDisplayDevice(@PathVariable("id") Long id) {
        Optional<DisplayDeviceEntity> foundDisplayDevice = displayDeviceService.findOne(id);

        return foundDisplayDevice.map(displayDeviceEntity -> {
            DisplayDeviceDto displayDeviceDto = displayDeviceMapper.mapTo(displayDeviceEntity);
            return new ResponseEntity<>(displayDeviceDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path="/{id}/time_slots")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Set<TimeSlotDto>> getSetOfTimeSlotsDisplayDeviceIsAPartOf(@PathVariable("id") long id){
        if (!displayDeviceService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(timeSlotService.findSetOfTimeSlotsDisplayDeviceIsAPartOf(id), HttpStatus.OK); 
    }


    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> fullUpdateDisplayDevice(@PathVariable("id") Long id,
            @Valid @RequestBody DisplayDeviceDto displayDeviceDto,
            @RequestParam(value = "forceDimensions", required = false) Boolean forceDimensions) {
        if (!displayDeviceService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        displayDeviceDto.setId(Math.toIntExact(id));
        DisplayDeviceEntity displayDeviceEntity = displayDeviceMapper.mapFrom(displayDeviceDto);

        Result<DisplayDeviceEntity, String> savedDisplayDeviceEntity = displayDeviceService.save(
            displayDeviceEntity, 
            forceDimensions != null ? forceDimensions : false
        );

        if (savedDisplayDeviceEntity.isErr()) {
            return switch(savedDisplayDeviceEntity.getErr().toLowerCase()) {
                case "not found" ->  new ResponseEntity<>(HttpStatus.NOT_FOUND); 
                default -> new ResponseEntity<>(savedDisplayDeviceEntity.getErr(), HttpStatus.CONFLICT);
            };
        }

        return new ResponseEntity<>(displayDeviceMapper.mapTo(savedDisplayDeviceEntity.getOk()), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> partialUpdateDisplayDevice(@PathVariable("id") Long id,
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
    public ResponseEntity<?> deleteDisplayDevice(@PathVariable("id") Long id) {
        if (!displayDeviceService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        displayDeviceService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    @PatchMapping(path = "/{id}/fallback_content")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<?> setFallbackContent(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> requestBody,
            @RequestParam(value = "forceDimensions", required = false) Boolean forceDimensions) {

        // Validate input and extract fallbackId
        // check if is a number
        Long fallbackId;
        try {
            fallbackId = Long.valueOf(requestBody.get("fallbackId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
        
        // Update the display device and return the response
        Result<DisplayDeviceEntity, String> updatedDisplayDeviceEntity = displayDeviceService.addFallback(id, fallbackId, forceDimensions != null ? forceDimensions : false);
        if (updatedDisplayDeviceEntity.isErr()) {
            return switch(updatedDisplayDeviceEntity.getErr()) {
                case "Not found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                default -> new ResponseEntity<>(updatedDisplayDeviceEntity.getErr(), HttpStatus.CONFLICT);
            };
        }
        
        return ResponseEntity.ok(displayDeviceMapper.mapTo(updatedDisplayDeviceEntity.getOk()));
    }

}