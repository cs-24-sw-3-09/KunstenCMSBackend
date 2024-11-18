package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/display_devices")
public class DisplayDeviceController {

    private final DisplayDeviceService displayDeviceService;
    private Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper;

    @Autowired
    public DisplayDeviceController(DisplayDeviceService displayDeviceService,
            Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper) {
        this.displayDeviceService = displayDeviceService;
        this.displayDeviceMapper = displayDeviceMapper;
    }

    @PostMapping
    public ResponseEntity<DisplayDeviceDto> createDisplayDevice(@Valid @RequestBody DisplayDeviceDto displayDevice) {

        // Done to decouple the persistence layer from the presentation and service
        // layer.
        DisplayDeviceEntity displayDeviceEntity = displayDeviceMapper.mapFrom(displayDevice);
        DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity);
        return new ResponseEntity<>(displayDeviceMapper.mapTo(savedDisplayDeviceEntity), HttpStatus.CREATED);
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
    public ResponseEntity<DisplayDeviceDto> fullUpdateDisplayDevice(@PathVariable("id") Long id,
            @Valid @RequestBody DisplayDeviceDto displayDeviceDto) {
        if (!displayDeviceService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        displayDeviceDto.setId(Math.toIntExact(id));
        DisplayDeviceEntity displayDeviceEntity = displayDeviceMapper.mapFrom(displayDeviceDto);
        DisplayDeviceEntity savedDisplayDeviceEntity = displayDeviceService.save(displayDeviceEntity);
        return new ResponseEntity<>(displayDeviceMapper.mapTo(savedDisplayDeviceEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
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
    public ResponseEntity deleteDisplayDevice(@PathVariable("id") Long id) {
        if (!displayDeviceService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        displayDeviceService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}