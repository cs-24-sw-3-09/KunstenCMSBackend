package com.github.cs_24_sw_3_09.CMS.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaInclusionDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
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
import org.springframework.web.bind.annotation.RestController;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
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
    private final SlideshowService slideshowService;
    private final VisualMediaService visualMediaService;

    @Autowired
    public TimeSlotController(
            TimeSlotService timeSlotService,
            Mapper<TimeSlotEntity, TimeSlotDto> timeSlotMapper,
            DisplayDeviceService displayDeviceService,
            VisualMediaService visualMediaService,
            SlideshowService slideshowService
    ) {
        this.timeSlotService = timeSlotService;
        this.timeSlotMapper = timeSlotMapper;
        this.displayDeviceService = displayDeviceService;
        this.visualMediaService = visualMediaService;
        this.slideshowService = slideshowService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<TimeSlotDto> createTimeSlot(@Valid @RequestBody TimeSlotDto timeSlot) {
        // Done to decouple the persistence layer from the presentation and service
        // layer.
        TimeSlotEntity timeSlotEntity = timeSlotMapper.mapFrom(timeSlot);
        
        
        /*Optional<DisplayDeviceEntity> optionalDisplayDevice = timeSlotEntity.getDisplayDevices().stream().findFirst();
        boolean checkIds = timeSlotEntity.getDisplayDevices().stream().allMatch(device -> 
                    displayDeviceService.isExists(Long.valueOf(device.getId()))
                );

        TimeSlotEntity savedTimeSlotEntity;
        if (optionalDisplayDevice.isPresent() && optionalDisplayDevice.get().getId() == null) {
            savedTimeSlotEntity = timeSlotService.save(timeSlotEntity);
        } else if(checkIds) {
            savedTimeSlotEntity = timeSlotService.saveWithOnlyId(timeSlotEntity);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }*/


        Optional<TimeSlotEntity> savedTimeSlotEntity = timeSlotService.save(timeSlotEntity);
        if (savedTimeSlotEntity.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(timeSlotMapper.mapTo(savedTimeSlotEntity.get()), HttpStatus.CREATED);
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
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<TimeSlotDto> fullUpdateTimeSlot(@PathVariable("id") Long id,
                                                          @Valid @RequestBody TimeSlotDto timeSlotDto) {
        if (!timeSlotService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        timeSlotDto.setId(Math.toIntExact(id));
        TimeSlotEntity timeSlotEntity = timeSlotMapper.mapFrom(timeSlotDto);
        Optional<TimeSlotEntity> savedTimeSlotEntity = timeSlotService.save(timeSlotEntity);
        if (savedTimeSlotEntity.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(timeSlotMapper.mapTo(savedTimeSlotEntity.get()), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
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
    public ResponseEntity<TimeSlotDto> setContent(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> requestBody) {

        // Validate input and extract fallbackId
        if (!requestBody.containsKey("displayContentId") || !requestBody.containsKey("type")) {
            return ResponseEntity.badRequest().build();
        }

        // check if id is a number && type is valid
        Long displayContentId;
        String displayContentType = requestBody.get("type").toString();
        try {
            displayContentId = Long.valueOf(requestBody.get("displayContentId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        if(!displayContentType.equals("visualMedia") && !displayContentType.equals("slideshow")) {
            return ResponseEntity.badRequest().build();
        }


        // validate existence of TS
        if (!timeSlotService.isExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //Check if content exists
        if (displayContentType.equals("visualMedia") && !visualMediaService.isExists(displayContentId)
                || displayContentType.equals("slideshow") && !slideshowService.isExists(displayContentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


        // Update the display device and return the response
        TimeSlotEntity updatedTimeSlotEntity = timeSlotService.setDisplayContent(id, displayContentId, displayContentType);

        return ResponseEntity.ok(timeSlotMapper.mapTo(updatedTimeSlotEntity));
    }

}
