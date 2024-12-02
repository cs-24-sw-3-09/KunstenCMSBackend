package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaInclusionDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaInclusionRepository;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

import org.hibernate.validator.cfg.defs.EANDef;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/visual_media_inclusions")
public class VisualMediaInclusionController {

    private final VisualMediaInclusionRepository visualMediaInclusionRepository;
    private final VisualMediaService visualMediaService;
    private final Mapper<VisualMediaInclusionEntity, VisualMediaInclusionDto> visualMediaInclusionMapper;
    private final VisualMediaInclusionService visualMediaInclusionService;

    public VisualMediaInclusionController(
            Mapper<VisualMediaInclusionEntity, VisualMediaInclusionDto> visualMediaInclusionMapper,
            VisualMediaInclusionService visualMediaInclusionService,
            VisualMediaInclusionRepository visualMediaInclusionRepository, VisualMediaService visualMediaService) {
        this.visualMediaInclusionMapper = visualMediaInclusionMapper;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.visualMediaInclusionRepository = visualMediaInclusionRepository;
        this.visualMediaService = visualMediaService;
    }

    @GetMapping
    public Page<VisualMediaInclusionDto> getVisualMediaInclusions(Pageable pageable) {
        Page<VisualMediaInclusionEntity> visualMediaInclusionEntities = visualMediaInclusionService.findAll(pageable);
        return visualMediaInclusionEntities.map(visualMediaInclusionMapper::mapTo);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaInclusionDto> createVisualMediaInclusion(
            @RequestBody VisualMediaInclusionDto visualMediaInclusionDto) {
        VisualMediaInclusionEntity visualMediaInclusionEntity = visualMediaInclusionMapper
                .mapFrom(visualMediaInclusionDto);
        VisualMediaInclusionEntity savedVisualMediaInclusionEntity = visualMediaInclusionRepository
                .save(visualMediaInclusionEntity);
        return new ResponseEntity<>(visualMediaInclusionMapper.mapTo(savedVisualMediaInclusionEntity),
                HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<VisualMediaInclusionDto> getVisualMedia(@PathVariable("id") Long id) {
        Optional<VisualMediaInclusionEntity> foundVisualInclusionMedia = visualMediaInclusionService.findOne(id);

        return foundVisualInclusionMedia.map(visualMediaInclusionEntity -> {
            VisualMediaInclusionDto visualMediaInclusionDto = visualMediaInclusionMapper
                    .mapTo(visualMediaInclusionEntity);
            return new ResponseEntity<>(visualMediaInclusionDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaInclusionDto> fullUpdateVisualMediaInclusion(@PathVariable("id") Long id,
            @RequestBody VisualMediaInclusionDto visualMediaInclusionDto) {
        if (!visualMediaInclusionService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        visualMediaInclusionDto.setId(Math.toIntExact(id));
        VisualMediaInclusionEntity visualMediaInclusionEntity = visualMediaInclusionMapper
                .mapFrom(visualMediaInclusionDto);
        VisualMediaInclusionEntity savedVisualMediaInclusionEntity = visualMediaInclusionService
                .save(visualMediaInclusionEntity);
        return new ResponseEntity<>(visualMediaInclusionMapper.mapTo(savedVisualMediaInclusionEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaInclusionDto> partialUpdateDisplayDevice(@PathVariable("id") Long id,
            @RequestBody VisualMediaInclusionDto visualMediaInclusionDto) {
        if (!visualMediaInclusionService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VisualMediaInclusionEntity visualMediaInclusionEntity = visualMediaInclusionMapper
                .mapFrom(visualMediaInclusionDto);
        VisualMediaInclusionEntity updatedVisualMediaInclusionEntity = visualMediaInclusionService.partialUpdate(id,
                visualMediaInclusionEntity);
        return new ResponseEntity<>(visualMediaInclusionMapper.mapTo(updatedVisualMediaInclusionEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaInclusionDto> deleteVisualMediaInclusion(@PathVariable("id") Long id) {
        if (!visualMediaInclusionService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        visualMediaInclusionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{id}/visual_media")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaInclusionDto> setVisualMedia(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> requestBody) {

        // Validate input and extract fallbackId
        if (!requestBody.containsKey("visualMediaId")) {
            return ResponseEntity.badRequest().build();
        }

        // check if is a number
        Long visualMediaId;
        try {
            visualMediaId = Long.valueOf(requestBody.get("visualMediaId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        // Get content type and validate existence of dd and content
        if (!visualMediaInclusionService.isExists(id) || !visualMediaService.isExists(visualMediaId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Update the display device and return the response
        VisualMediaInclusionEntity updatedVisualMediaInclusionEntity = visualMediaInclusionService.setVisualMedia(id,
                visualMediaId);

        return ResponseEntity.ok(visualMediaInclusionMapper.mapTo(updatedVisualMediaInclusionEntity));
    }

    @PatchMapping(path = "/positions")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<List<VisualMediaInclusionDto>> updateSlideshowPositions(
        @RequestBody Map<String, Object> requestBody) {

        ArrayList<VisualMediaInclusionEntity> visualMediaInclusions = new ArrayList<>();
        for(Map<String, Object> data : (List<Map<String, Object>>) requestBody.get("visualMediaInclusion")) {        
            VisualMediaInclusionEntity vmi = new VisualMediaInclusionEntity();

            vmi.setId((Integer) data.get("id"));
            vmi.setSlideshowPosition((Integer) data.get("slideshowPosition"));

            visualMediaInclusions.add(vmi);
        }

        Optional<List<VisualMediaInclusionEntity>> updatedVisualMediaInclusions = visualMediaInclusionService.updateSlideshowPosition(visualMediaInclusions);
        if (updatedVisualMediaInclusions.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<VisualMediaInclusionDto> visualMediaInclusionDtos = 
            updatedVisualMediaInclusions.get()
            .stream().map(visualMediaInclusionMapper::mapTo)
            .toList();
        
        return new ResponseEntity<>(
            visualMediaInclusionDtos, 
            HttpStatus.OK
        );
    }


}
