package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaInclusionDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaInclusionRepository;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/visual_media_inclusions")
public class VisualMediaInclusionController {

    private final VisualMediaInclusionRepository visualMediaInclusionRepository;
    private Mapper<VisualMediaInclusionEntity, VisualMediaInclusionDto> visualMediaInclusionMapper;
    private VisualMediaInclusionService visualMediaInclusionService;

    public VisualMediaInclusionController(Mapper<VisualMediaInclusionEntity, VisualMediaInclusionDto> visualMediaInclusionMapper, VisualMediaInclusionService visualMediaInclusionService, VisualMediaInclusionRepository visualMediaInclusionRepository) {
        this.visualMediaInclusionMapper = visualMediaInclusionMapper;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.visualMediaInclusionRepository = visualMediaInclusionRepository;
    }

    @GetMapping
    public Page<VisualMediaInclusionDto> getVisualMediaInclusions(Pageable pageable) {
        Page<VisualMediaInclusionEntity> visualMediaInclusionEntities = visualMediaInclusionService.findAll(pageable);
        return visualMediaInclusionEntities.map(visualMediaInclusionMapper::mapTo);
    }

    @PostMapping
    public ResponseEntity<VisualMediaInclusionDto> createVisualMediaInclusion(@RequestBody VisualMediaInclusionDto visualMediaInclusionDto) {
        VisualMediaInclusionEntity visualMediaInclusionEntity = visualMediaInclusionMapper.mapFrom(visualMediaInclusionDto);
        VisualMediaInclusionEntity savedVisualMediaInclusionEntity = visualMediaInclusionRepository.save(visualMediaInclusionEntity);
        return new ResponseEntity<>(visualMediaInclusionMapper.mapTo(savedVisualMediaInclusionEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<VisualMediaInclusionDto> getVisualMedia(@PathVariable("id") Long id) {
        Optional<VisualMediaInclusionEntity> foundVisualInclusionMedia = visualMediaInclusionService.findOne(id);

        return foundVisualInclusionMedia.map(visualMediaInclusionEntity -> {
            VisualMediaInclusionDto visualMediaInclusionDto = visualMediaInclusionMapper.mapTo(visualMediaInclusionEntity);
            return new ResponseEntity<>(visualMediaInclusionDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<VisualMediaInclusionDto> fullUpdateVisualMediaInclusion(@PathVariable("id") Long id, @RequestBody VisualMediaInclusionDto visualMediaInclusionDto) {
        if (!visualMediaInclusionService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        visualMediaInclusionDto.setId(Math.toIntExact(id));
        VisualMediaInclusionEntity visualMediaInclusionEntity = visualMediaInclusionMapper.mapFrom(visualMediaInclusionDto);
        VisualMediaInclusionEntity savedVisualMediaInclusionEntity = visualMediaInclusionService.save(visualMediaInclusionEntity);
        return new ResponseEntity<>(visualMediaInclusionMapper.mapTo(savedVisualMediaInclusionEntity), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<VisualMediaInclusionDto> partialUpdateDisplayDevice(@PathVariable("id") Long id, @RequestBody VisualMediaInclusionDto visualMediaInclusionDto) {
        if (!visualMediaInclusionService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VisualMediaInclusionEntity visualMediaInclusionEntity = visualMediaInclusionMapper.mapFrom(visualMediaInclusionDto);
        VisualMediaInclusionEntity updatedVisualMediaInclusionEntity = visualMediaInclusionService.partialUpdate(id, visualMediaInclusionEntity);
        return new ResponseEntity<>(visualMediaInclusionMapper.mapTo(updatedVisualMediaInclusionEntity), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<VisualMediaInclusionDto> deleteVisualMediaInclusion(@PathVariable("id") Long id) {
        if (!visualMediaInclusionService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        visualMediaInclusionService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
