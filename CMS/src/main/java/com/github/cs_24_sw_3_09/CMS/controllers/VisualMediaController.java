package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/visual_medias")
public class VisualMediaController {

    private Mapper<VisualMediaEntity, VisualMediaDto> visualMediaMapper;
    private VisualMediaService visualMediaService;

    public VisualMediaController(Mapper<VisualMediaEntity, VisualMediaDto> visualMediaMapper, VisualMediaService visualMediaService) {
        this.visualMediaMapper = visualMediaMapper;
        this.visualMediaService = visualMediaService;
    }

    @PostMapping
    public ResponseEntity<VisualMediaDto> createVisualMedia(@RequestBody VisualMediaDto visualMediaDto) {
        VisualMediaEntity visualMediaEntity = visualMediaMapper.mapFrom(visualMediaDto);
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);
        VisualMediaDto savedVisualMediaDto = visualMediaMapper.mapTo(savedVisualMediaEntity);
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

    @PutMapping(path = "/{id}")
    public ResponseEntity<VisualMediaDto> fullUpdateVisualMedia(@PathVariable("id") Long id, @RequestBody VisualMediaDto visualMediaDto) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        visualMediaDto.setId(Math.toIntExact(id));
        VisualMediaEntity visualMediaEntity = visualMediaMapper.mapFrom(visualMediaDto);
        VisualMediaEntity savedVisualMediaEntity = visualMediaService.save(visualMediaEntity);
        return new ResponseEntity<>(visualMediaMapper.mapTo(savedVisualMediaEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<VisualMediaDto> partialUpdateVisualMedia(@PathVariable("id") Long id, @RequestBody VisualMediaDto visualMediaDto) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VisualMediaEntity visualMediaEntity = visualMediaMapper.mapFrom(visualMediaDto);
        VisualMediaEntity updatedDisplayDeviceEntity = visualMediaService.partialUpdate(id, visualMediaEntity);
        return new ResponseEntity<>(visualMediaMapper.mapTo(updatedDisplayDeviceEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<VisualMediaDto> deleteVisualMedia(@PathVariable("id") Long id) {
        if (!visualMediaService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        visualMediaService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
