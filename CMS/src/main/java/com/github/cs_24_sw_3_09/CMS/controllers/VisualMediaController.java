package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.services.FileStorageService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/visual_medias")
public class VisualMediaController {

    private Mapper<VisualMediaEntity, VisualMediaDto> visualMediaMapper;
    private VisualMediaService visualMediaService;
    private FileStorageService fileStorageService;

    public VisualMediaController(Mapper<VisualMediaEntity, VisualMediaDto> visualMediaMapper, VisualMediaService visualMediaService, FileStorageService fileStorageService) {
        this.visualMediaMapper = visualMediaMapper;
        this.visualMediaService = visualMediaService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public ResponseEntity<VisualMediaDto> createVisualMedia(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        VisualMediaDto visualMediaDto = VisualMediaDto.builder()
                .name(file.getOriginalFilename())
                .fileType(file.getContentType())
                .lastDateModified(String.valueOf(LocalDateTime.now()))
                .build();

        fileStorageService.saveFile(file);

        VisualMediaEntity visualMediaEntity = visualMediaMapper.mapFrom(visualMediaDto);
        VisualMediaEntity savedEntity = visualMediaService.save(visualMediaEntity);

        //Update the location field using the ID
        String location = "/visual_media/" + savedEntity.getId();
        savedEntity.setLocation(location);

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

    @PutMapping(path = "/{id}")
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
    public ResponseEntity<VisualMediaDto> deleteVisualMedia(@PathVariable("id") Long id) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        visualMediaService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{id}/tags")
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

}
