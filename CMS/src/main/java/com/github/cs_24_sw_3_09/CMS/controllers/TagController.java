package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.TagDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import com.github.cs_24_sw_3_09.CMS.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
