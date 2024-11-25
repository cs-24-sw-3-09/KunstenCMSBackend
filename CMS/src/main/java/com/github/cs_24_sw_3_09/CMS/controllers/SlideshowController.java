package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.impl.SlideshowMapperImpl;
import com.github.cs_24_sw_3_09.CMS.model.dto.SlideshowDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/slideshows")
public class SlideshowController {

    private SlideshowMapperImpl slideshowMapper;
    private SlideshowService slideshowService;

    public SlideshowController(SlideshowMapperImpl slideshowMapper, SlideshowService slideshowService,
            SlideshowRepository slideshowRepository) {
        this.slideshowMapper = slideshowMapper;
        this.slideshowService = slideshowService;
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
    public Page<SlideshowDto> getSlideshows(Pageable pageable) {
        Page<SlideshowEntity> slideshowEntities = slideshowService.findAll(pageable);
        return slideshowEntities.map(slideshowMapper::mapTo);
    }

    @PostMapping
    public ResponseEntity<SlideshowDto> createSlideshow(@RequestBody SlideshowDto slideshowDto) {
        SlideshowEntity slideshowEntity = slideshowMapper.mapFrom(slideshowDto);
        SlideshowEntity savedSlideshowEntity = slideshowService.save(slideshowEntity);
        return new ResponseEntity<>(slideshowMapper.mapTo(savedSlideshowEntity), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteSlideshow(@PathVariable("id") long id) {
        if (!slideshowService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        slideshowService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{id}")
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
    public ResponseEntity<SlideshowDto> patchSlideshow(@PathVariable("id") long id,
            @RequestBody SlideshowDto slideshowDto) {
        if (!slideshowService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        SlideshowEntity slideshowEntity = slideshowMapper.mapFrom(slideshowDto);
        SlideshowEntity updatedSlideshow = slideshowService.partialUpdate(id, slideshowEntity);

        return new ResponseEntity<>(slideshowMapper.mapTo(updatedSlideshow), HttpStatus.OK);
    }
}
