package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.impl.SlideshowMapperImpl;
import com.github.cs_24_sw_3_09.CMS.model.dto.SlideshowDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/slideshows")
public class SlideshowController {

    private final VisualMediaInclusionService visualMediaInclusionService;
    private final SlideshowMapperImpl slideshowMapper;
    private final SlideshowService slideshowService;

    public SlideshowController(SlideshowMapperImpl slideshowMapper, SlideshowService slideshowService,
            VisualMediaInclusionService visualMediaInclusionService) {
        this.slideshowMapper = slideshowMapper;
        this.slideshowService = slideshowService;
        this.visualMediaInclusionService = visualMediaInclusionService;
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
    public Iterable<SlideshowEntity> getSlideshows() {
        Iterable<SlideshowEntity> slideshowEntities = slideshowService.findAll();
        return slideshowEntities;
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
    public ResponseEntity deleteSlideshow(@PathVariable("id") long id) {
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
    public ResponseEntity<SlideshowDto> addVisualMediaInclusion(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> requestBody) {

        // Validate input and extract fallbackId
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

        // Get content type and validate existence of dd and content
        if (!slideshowService.isExists(id) || !visualMediaInclusionService.isExists(visualMediaInclusionId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Update the display device and return the response
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
        String newName = requestBody.containsKey("name") 
        ? requestBody.get("name").toString() : null; 

        // Update the display device and return the response
        Optional<SlideshowEntity> updatedSlideshowEntity = slideshowService.duplicate(id, newName);
        if (updatedSlideshowEntity.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(slideshowMapper.mapTo(updatedSlideshowEntity.get()));
    }

}
