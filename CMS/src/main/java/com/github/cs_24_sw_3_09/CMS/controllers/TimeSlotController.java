package com.github.cs_24_sw_3_09.CMS.controllers;

import java.sql.Date;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotColor;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import com.github.cs_24_sw_3_09.CMS.utils.ContentUtils;
import com.github.cs_24_sw_3_09.CMS.utils.SetTSContentValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/time_slots")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;
    private final Mapper<TimeSlotEntity, TimeSlotDto> timeSlotMapper;
    private final DisplayDeviceService displayDeviceService;
    private ContentUtils contentUtils;
    private final DimensionCheckService dimensionCheckService;
    private final VisualMediaService visualMediaService;
    private final SlideshowService slideshowService;

    @Autowired
    public TimeSlotController(
            TimeSlotService timeSlotService,
            Mapper<TimeSlotEntity, TimeSlotDto> timeSlotMapper,
            DisplayDeviceService displayDeviceService,
            ContentUtils contentUtils,
            DimensionCheckService dimensionCheckService,
            VisualMediaService visualMediaService,
            SlideshowService slideshowService
    ) {
        this.timeSlotService = timeSlotService;
        this.timeSlotMapper = timeSlotMapper;
        this.displayDeviceService = displayDeviceService;
        this.contentUtils = contentUtils;
        this.dimensionCheckService = dimensionCheckService;
        this.visualMediaService = visualMediaService;
        this.slideshowService = slideshowService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<?> createTimeSlot(@Valid @RequestBody TimeSlotDto timeSlot,
            @RequestParam(value = "forceDimensions", required = false) Boolean forceDimensions ) {
        // Done to decouple the persistence layer from the presentation and service
        // layer.
        TimeSlotEntity timeSlotEntity = timeSlotMapper.mapFrom(timeSlot);

        Optional<TimeSlotEntity> savedTimeSlotEntity = timeSlotService.save(timeSlotEntity);
        
        if (savedTimeSlotEntity.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(timeSlotMapper.mapTo(savedTimeSlotEntity.get()), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<TimeSlotDto> getTimeSlots(Pageable pageable,
            @RequestParam(value = "start", required = false) Date startDate,
            @RequestParam(value = "end", required = false) Date endDate) {

        // If one want to find based on date
        if (startDate != null && endDate != null) {
            List<TimeSlotEntity> timeSlotEntities = timeSlotService.findAll(startDate, endDate);
            List<TimeSlotDto> timeSlotDtos = timeSlotEntities.stream()
                    .map(timeSlotMapper::mapTo)
                    .collect(Collectors.toList());

            return new PageImpl<>(timeSlotDtos, pageable, timeSlotDtos.size());
        }

        Page<TimeSlotEntity> timeSlotEntities = timeSlotService.findAll(pageable);
        return timeSlotEntities.map(timeSlotMapper::mapTo);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<TimeSlotDto>> getAllDisplayDevices() {
        List<TimeSlotEntity> timeSlotEntities = timeSlotService.findAll();
        return ResponseEntity.ok(timeSlotEntities.stream().map(timeSlotMapper::mapTo).toList());
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
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<?> fullUpdateTimeSlot(@PathVariable("id") Long id,
            @Valid @RequestBody TimeSlotDto timeSlotDto,
            @RequestParam(value = "forceDimensions", required = false) Boolean forceDimensions) {
        if (!timeSlotService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        timeSlotDto.setId(Math.toIntExact(id));
        TimeSlotEntity timeSlotEntity = timeSlotMapper.mapFrom(timeSlotDto);
        //check if dimensions of displaydevice and content fit
        if (timeSlotEntity.getDisplayContent() != null && timeSlotEntity.getDisplayContent() != null) {
            if(forceDimensions == false){
                String checkResult = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(timeSlotEntity.getDisplayContent(), timeSlotEntity.getDisplayDevices());
                if(!"1".equals(checkResult)){
                    return new ResponseEntity<>(checkResult, HttpStatus.CONFLICT);  
                }
            }
        }
        Optional<TimeSlotEntity> savedTimeSlotEntity = timeSlotService.save(timeSlotEntity);
        if (savedTimeSlotEntity.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(timeSlotMapper.mapTo(savedTimeSlotEntity.get()), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<?> partialUpdateTimeSlot(@PathVariable("id") Long id,
                @Valid @RequestBody TimeSlotDto timeSlotDto,
                @RequestParam(value = "forceDimensions", required = false) Boolean forceDimensions) {
        if (!timeSlotService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TimeSlotEntity timeSlotEntity = timeSlotMapper.mapFrom(timeSlotDto);

        //check if dimensions of displaydevice and content fit
        if (timeSlotEntity.getDisplayContent() != null && timeSlotEntity.getDisplayContent() != null) {
            if(forceDimensions == false){
                String checkResult = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(timeSlotEntity.getDisplayContent(), timeSlotEntity.getDisplayDevices());
                if(!"1".equals(checkResult)){
                    return new ResponseEntity<>(checkResult, HttpStatus.CONFLICT);  
                }
            }
        }

        TimeSlotEntity updatedTimeSlotEntity = timeSlotService.partialUpdate(id, timeSlotEntity);
        return new ResponseEntity<>(timeSlotMapper.mapTo(updatedTimeSlotEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<Object> deleteTimeSlot(@PathVariable("id") Long id) {
        if (!timeSlotService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        timeSlotService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}/display_devices")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<Object> deleteRelation(@PathVariable("id") Long tsId,
                                                 @RequestBody Map<String, Object> requestBody) {
        // Validate input and extract fallbackId
        if (!requestBody.containsKey("ddId")) {
            return ResponseEntity.badRequest().build();
        }

        //check if is a number
        Long ddId;
        try {
            ddId = Long.valueOf(requestBody.get("ddId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        if (!timeSlotService.isExists(tsId) || !displayDeviceService.isExists(ddId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        timeSlotService.deleteRelation(tsId, ddId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{id}/display_content")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<?> setContent(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> requestBody,
            @RequestParam(value = "forceDimensions", required = false) Boolean forceDimensions ) {

        //validate request body (check if contentType, id, and type of these are correct)
        SetTSContentValidationResult validationResult = contentUtils.validateRequestBody(requestBody);
        if (!validationResult.isValid()) {
            return ResponseEntity.badRequest().build();
        }

        //Getting the data from the previous result
        Long displayContentId = validationResult.getDisplayContentId();
        String displayContentType = validationResult.getDisplayContentType();

        // Validate existence of time slot
        if (!timeSlotService.isExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Validate existence of the referenced content
        if (!contentUtils.isDisplayContentValid(displayContentId, displayContentType)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //check if dimensions of displaydevice and content fit
        TimeSlotEntity timeSlotEntity = timeSlotService.findOne(id).get();
        if (timeSlotEntity.getDisplayDevices() != null) {
            if(forceDimensions == false){
                ContentEntity displayContent = null;
                if(displayContentType.equals("visualMedia")){
                    displayContent = visualMediaService.findOne(displayContentId).get(); //safe to use .get() since already confirmed existence
                } else if (displayContentType.equals("slideshow")){
                    displayContent = slideshowService.findOne(displayContentId).get();
                }
                String checkResult = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(displayContent, timeSlotEntity.getDisplayDevices());
                if(!"1".equals(checkResult)){
                    return new ResponseEntity<>(checkResult, HttpStatus.CONFLICT);  
                }
            }
        }

        // Update the display content
        TimeSlotEntity updatedTimeSlotEntity = timeSlotService.setDisplayContent(id, displayContentId, displayContentType);

        return ResponseEntity.ok(timeSlotMapper.mapTo(updatedTimeSlotEntity));
    }



    @PatchMapping(path = "/{id}/display_devices")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<?> addDisplayDevice(@PathVariable("id") Long id, 
            @RequestBody Map<String, Object> requestBody,
            @RequestParam(value = "forceDimensions", required = false) Boolean forceDimensions) {
        if (!requestBody.containsKey("displayDeviceId")) {
            return ResponseEntity.badRequest().build();
        }
        Long displayDeviceId = ((Integer) requestBody.get("displayDeviceId")).longValue();
        if (!timeSlotService.isExists(id) || !displayDeviceService.isExists(displayDeviceId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //check if dimensions of displaydevice and content fit
        TimeSlotEntity timeSlotEntity = timeSlotService.findOne(id).get();
        Optional<DisplayDeviceEntity> optionalDevice = displayDeviceService.findOne(displayDeviceId);
        Set<DisplayDeviceEntity> displayDevice = Set.of(optionalDevice.get());//safe to use .get() since already validated existence
        if (timeSlotEntity.getDisplayContent() != null) {
            if(forceDimensions == false){
                String checkResult = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(timeSlotEntity.getDisplayContent(), displayDevice);
                if(!"1".equals(checkResult)){
                    return new ResponseEntity<>(checkResult, HttpStatus.CONFLICT);  
                }
            }
        }

        TimeSlotEntity updatedTimeSlot = timeSlotService.addDisplayDevice(id, displayDeviceId);

        // If tag was not found, updatedVisualMedia will be null.
        if (updatedTimeSlot == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(timeSlotMapper.mapTo(updatedTimeSlot), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    @GetMapping(path = "/{id}/overlapping_time_slots")
    public ResponseEntity<List<TimeSlotDto>> getOverlappingTimeSlots(@PathVariable("id") Long id) {
        if (!timeSlotService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<TimeSlotEntity> overlappingTimeSlots = timeSlotService.findOverlappingTimeSlots(id);
        List<TimeSlotDto> overlappingTimeSlotDtos = overlappingTimeSlots.stream()
                .map(timeSlotMapper::mapTo).toList();

        return new ResponseEntity<>(overlappingTimeSlotDtos, HttpStatus.OK);
    }

    @GetMapping(path = "/overlapping_time_slots")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<List<TimeSlotColor>> getAllTimeSlotColors() {


        List<TimeSlotColor> timeslotColors  = timeSlotService.getTimeSlotColors();
        return new ResponseEntity<>(timeslotColors, HttpStatus.OK);
    }


}

