package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.TagDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import com.github.cs_24_sw_3_09.CMS.services.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private final Mapper<TagEntity, TagDto> tagMapper;

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

    @PatchMapping(path = "/{id}")
    public ResponseEntity<TagDto> patchTag(@PathVariable("id") Long id,
            @Valid @RequestBody TagDto tagDto) {
        if (!tagService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TagEntity tagEntity = tagMapper.mapFrom(tagDto);
        TagEntity updatedUserEntity = tagService.partialUpdate(id, tagEntity);
        return new ResponseEntity<>(tagMapper.mapTo(updatedUserEntity), HttpStatus.OK);
    }
}
