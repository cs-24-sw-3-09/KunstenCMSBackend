package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TagDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private Mapper<TagEntity, TagDto> tagMapper;

    @Autowired
    public TagController(TagService tagService, Mapper<TagEntity, TagDto> tagMapper) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {

        TagEntity tagEntity = tagMapper.mapFrom(tagDto);
        TagEntity savedTagEntity = tagService.save(tagEntity);
        return new ResponseEntity<>(tagMapper.mapTo(savedTagEntity), HttpStatus.CREATED);
    }

    @GetMapping
    public Page<TagDto> getTags(Pageable pageable) {
        Page<TagEntity> tagEntities = tagService.findAll(pageable);
        return tagEntities.map(tagMapper::mapTo);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<TagDto> deleteTag(@PathVariable("id") Long id) {
        if (!tagService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TagDto> getTagById(@PathVariable("id") Long id) {
        Optional<TagEntity> foundUser = tagService.findOne(id);

        return foundUser.map(tagEntity -> {
            TagDto tagDto = tagMapper.mapTo(tagEntity);
            return new ResponseEntity<>(tagDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
