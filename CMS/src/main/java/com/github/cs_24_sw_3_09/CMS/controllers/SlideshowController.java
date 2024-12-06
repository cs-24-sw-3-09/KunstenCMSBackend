package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.cs_24_sw_3_09.CMS.mappers.impl.SlideshowMapperImpl;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.SlideshowDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;

import org.json.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/slideshows")
public class SlideshowController {

    private final VisualMediaInclusionService visualMediaInclusionService;
    private final SlideshowMapperImpl slideshowMapper;
    private final SlideshowService slideshowService;
    private final TimeSlotService timeSlotService;
    private final DisplayDeviceService displayDeviceService;
    private final DimensionCheckService dimensionCheckService;

    public SlideshowController(SlideshowMapperImpl slideshowMapper, SlideshowService slideshowService,
            VisualMediaInclusionService visualMediaInclusionService, TimeSlotService timeSlotService, 
            DisplayDeviceService displayDeviceService, DimensionCheckService dimensionCheckService) {
        this.slideshowMapper = slideshowMapper;
        this.slideshowService = slideshowService;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.timeSlotService = timeSlotService;
        this.displayDeviceService = displayDeviceService;
        this.dimensionCheckService = dimensionCheckService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SlideshowDto> getSlideshow(@PathVariable("id") long id) {
        Optional<SlideshowEntity> foundSlideshow = slideshowService.findOne(id);

        return foundSlideshow.map(slideshowEntity -> {
            SlideshowDto slideshowDto = slideshowMapper.mapTo(slideshowEntity);
            return new ResponseEntity<>(slideshowDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Iterable<SlideshowDto> getSlideshows() {
        List<SlideshowEntity> slideshowEntities = slideshowService.findAll();
        return slideshowEntities.stream().map(slideshowMapper::mapTo).toList();
    }

    @GetMapping(path="/{id}/time_slots") 
    public ResponseEntity<Set<TimeSlotDto>> getSetOfTimeSlotsSlideshowIsAPartOf(@PathVariable("id") long id){
        if (!slideshowService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(timeSlotService.findSetOfTimeSlotsSlideshowIsAPartOf(id), HttpStatus.OK); 
    }

    @GetMapping(path="/{id}/fallbackContent")
    public ResponseEntity<Set<DisplayDeviceDto>> getSetOfDisplayDevicesWhoUsesSlideshowAsFallback(@PathVariable("id") long id){
        if (!slideshowService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(displayDeviceService.findDisplayDevicesWhoUsesSlideshowAsFallback(id), HttpStatus.OK);
    }

    @GetMapping(path="/states")
    public ResponseEntity<List<Map<String, Object>>> getStateOfAllSlideshows(){
        return new ResponseEntity<>(slideshowService.findStateOfEverySlideshow(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<SlideshowDto> createSlideshow(@RequestBody SlideshowDto slideshowDto) {
        SlideshowEntity slideshowEntity = slideshowMapper.mapFrom(slideshowDto);

        SlideshowEntity savedSlideshowEntity = slideshowService.save(slideshowEntity);
        return new ResponseEntity<>(slideshowMapper.mapTo(savedSlideshowEntity), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<Object> deleteSlideshow(@PathVariable("id") long id) {
        if (!slideshowService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        slideshowService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<SlideshowDto> updateSlideshow(@PathVariable("id") long id,
            @RequestBody SlideshowDto slideshowDto) {
        if (!slideshowService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        slideshowDto.setId(Math.toIntExact(id));
        SlideshowEntity slideshowEntity = slideshowMapper.mapFrom(slideshowDto);
        SlideshowEntity savedSlideshowEntity = slideshowService.save(slideshowEntity);

        return new ResponseEntity<>(slideshowMapper.mapTo(savedSlideshowEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<SlideshowDto> patchSlideshow(@PathVariable("id") long id,
            @RequestBody SlideshowDto slideshowDto) {
        if (!slideshowService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        SlideshowEntity slideshowEntity = slideshowMapper.mapFrom(slideshowDto);
        SlideshowEntity updatedSlideshow = slideshowService.partialUpdate(id, slideshowEntity);

        return new ResponseEntity<>(slideshowMapper.mapTo(updatedSlideshow), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}/visual_media_inclusions")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<?> addVisualMediaInclusion(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> requestBody,
            @RequestParam(value = "forceDimensions", required = false) Boolean forceDimensions) {

        if (!requestBody.containsKey("visualMediaInclusionId")) {
            return ResponseEntity.badRequest().build();
        }

        // check if is a number
        Long visualMediaInclusionId;
        try {
            visualMediaInclusionId = Long.valueOf(requestBody.get("visualMediaInclusionId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        // validate existence of slidehsow and visualMediaInclusion
        if (!slideshowService.isExists(id) || !visualMediaInclusionService.isExists(visualMediaInclusionId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if(forceDimensions == false){
            String checkResult = dimensionCheckService.checkDimensionForAssignedVisualMediaToSlideshow(
            visualMediaInclusionId, id);
            if (!"1".equals(checkResult)) {
                return new ResponseEntity<>(checkResult, HttpStatus.CONFLICT); 
            }
        }
        // Update the slideshow and return the response
        SlideshowEntity updatedSlideshowEntity = slideshowService.addVisualMediaInclusion(id, visualMediaInclusionId);

        return ResponseEntity.ok(slideshowMapper.mapTo(updatedSlideshowEntity));
    }

    @PostMapping(path = "/{id}/duplicate")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<SlideshowDto> createDuplicate(
        @PathVariable("id") Long id,
        @RequestBody Map<String, Object> requestBody
    ) {
        // Validate input and extract new name
        Object mapValue = requestBody.containsKey("name") ? requestBody.get("name") : null;
        String newName = mapValue != null ? mapValue.toString() : null;

        // Update the display device and return the response
        Optional<SlideshowEntity> updatedSlideshowEntity = slideshowService.duplicate(id, newName);
        if (updatedSlideshowEntity.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return new ResponseEntity<>(slideshowMapper.mapTo(updatedSlideshowEntity.get()), HttpStatus.CREATED);
    }

}
