package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import com.github.cs_24_sw_3_09.CMS.services.TagService;
import com.github.cs_24_sw_3_09.CMS.services.FileStorageService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/visual_medias")
public class VisualMediaController {

    private final Mapper<VisualMediaEntity, VisualMediaDto> visualMediaMapper;
    private final Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper;
    private final VisualMediaService visualMediaService;
    private final TagService tagService;
    private FileStorageService fileStorageService;
    private Mapper<TimeSlotEntity, TimeSlotDto> timeslotMapper;

    public VisualMediaController(
            Mapper<VisualMediaEntity, VisualMediaDto> visualMediaMapper,
            VisualMediaService visualMediaService,
            TagService tagService,
            FileStorageService fileStorageService,
            Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper,
            Mapper<TimeSlotEntity, TimeSlotDto> timeSlotMapper) {
        this.visualMediaMapper = visualMediaMapper;
        this.visualMediaService = visualMediaService;
        this.tagService = tagService;
        this.fileStorageService = fileStorageService;
        this.displayDeviceMapper = displayDeviceMapper;
        this.timeslotMapper = timeslotMapper;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaDto> createVisualMedia(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        VisualMediaDto visualMediaDto = VisualMediaDto.builder()
                .name(file.getOriginalFilename())
                .fileType(file.getContentType())
                .build();


        VisualMediaEntity visualMediaEntity = visualMediaMapper.mapFrom(visualMediaDto);
        VisualMediaEntity savedEntity = visualMediaService.save(visualMediaEntity);

        //Update the location field using the ID
        String location = "/visual_media/" + savedEntity.getId();
        savedEntity.setLocation(location);
        fileStorageService.saveVisualMediaFile(file, String.valueOf(savedEntity.getId()));


        //Save the updated entity
        savedEntity = visualMediaService.save(savedEntity);

        VisualMediaDto savedVisualMediaDto = visualMediaMapper.mapTo(savedEntity);
        return new ResponseEntity<>(savedVisualMediaDto, HttpStatus.CREATED);
    }

    @GetMapping
    public Page<VisualMediaDto> getVisualMedias(Pageable pageable) {
        Page<VisualMediaEntity> visualMediaEntities = visualMediaService.findAll(pageable);
        return visualMediaEntities.map(visualMediaMapper::mapTo);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<VisualMediaDto> getVisualMedia(@PathVariable("id") Long id) {
        Optional<VisualMediaEntity> foundVisualMedia = visualMediaService.findOne(id);

        return foundVisualMedia.map(visualMediaEntity -> {
            VisualMediaDto visualMediaDto = visualMediaMapper.mapTo(visualMediaEntity);
            return new ResponseEntity<>(visualMediaDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/{id}/tags")
    public ResponseEntity<List<TagEntity>> getVisualMediaTags(@PathVariable("id") Long id) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(visualMediaService.getVisualMediaTags(id), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/risk")
    public ResponseEntity<Set<SlideshowEntity>> getVisualMediaPartOfSlideshowsList(@PathVariable("id") Long id) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(visualMediaService.findPartOfSlideshows(id), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/display_devices")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<List<DisplayDeviceDto>> getDisplayDevicesVisualMediaIsPartOf(@PathVariable("id") Long id) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<DisplayDeviceEntity> foundDisplayDevices = visualMediaService.findDisplayDevicesVisualMediaIsPartOf(id);

        List<DisplayDeviceDto> foundDisplayDeviceDtos = foundDisplayDevices.stream()
                .map(displayDeviceMapper::mapTo) // Assuming `mapTo` converts an entity to a DTO
                .toList();

        return new ResponseEntity<>(foundDisplayDeviceDtos, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/timeslots")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<List<TimeSlotDto>> getTimeslotsVisualMediaIsPartOf(@PathVariable("id") Long id) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<TimeSlotEntity> foundTimeslots = visualMediaService.findTimeslotsVisualMediaIsPartOf(id);

        List<TimeSlotDto> foundTimeslotDtos = foundTimeslots.stream()
                .map(timeslotMapper::mapTo) // Assuming `mapTo` converts an entity to a DTO
                .toList();

        return new ResponseEntity<>(foundTimeslotDtos, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaDto> fullUpdateVisualMedia(@PathVariable("id") Long id,
                                                                @RequestBody VisualMediaDto visualMediaDto) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        visualMediaDto.setId(Math.toIntExact(id));
        VisualMediaEntity visualMediaEntity = visualMediaMapper.mapFrom(visualMediaDto);
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);
        return new ResponseEntity<>(visualMediaMapper.mapTo(savedVisualMediaEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaDto> partialUpdateVisualMedia(@PathVariable("id") Long id,
                                                                   @RequestBody VisualMediaDto visualMediaDto) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VisualMediaEntity visualMediaEntity = visualMediaMapper.mapFrom(visualMediaDto);
        VisualMediaEntity updatedVisualMediaEntity = visualMediaService.partialUpdate(id, visualMediaEntity);
        return new ResponseEntity<>(visualMediaMapper.mapTo(updatedVisualMediaEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaDto> deleteVisualMedia(@PathVariable("id") Long id) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        visualMediaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{id}/tags")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaDto> addTag(@PathVariable("id") Long id,
                                                 @RequestBody Map<String, Object> requestBody) {
        Long tagId = ((Integer) requestBody.get("tagId")).longValue();

        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VisualMediaEntity updatedVisualMedia = visualMediaService.addTag(id, tagId);

        // If tag was not found, updatedVisualMedia will be null.
        if (updatedVisualMedia == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(visualMediaMapper.mapTo(updatedVisualMedia), HttpStatus.OK);
    }

    @DeleteMapping(path = "{visual_media_Id}/tags")
    @PreAuthorize("hasAuthority('ROLE_PLANNER')")
    public ResponseEntity<VisualMediaDto> deleteTagRelation(@PathVariable("visual_media_Id") Long visualMediaId,
                                                            @RequestBody Map<String, Object> requestBody) {
        // Validate input and extract fallbackId
        if (!requestBody.containsKey("tagId")) {
            return ResponseEntity.badRequest().build();
        }

        //check if is a number
        Long tagId;
        try {
            tagId = Long.valueOf(requestBody.get("tagId").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        if (!visualMediaService.isExists(visualMediaId) || !tagService.isExists(tagId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        visualMediaService.deleteRelation(visualMediaId, tagId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
