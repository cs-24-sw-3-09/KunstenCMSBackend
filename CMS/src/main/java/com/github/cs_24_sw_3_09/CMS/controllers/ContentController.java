package com.github.cs_24_sw_3_09.CMS.controllers;


import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.ContentDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.ContentRepository;
import com.github.cs_24_sw_3_09.CMS.services.ContentService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/content")
public class ContentController {
    private Mapper<ContentEntity, ContentDto> contentMapper;
    private ContentService contentService;

    public ContentController(Mapper<ContentEntity, ContentDto> contentMapper, ContentService contentService, ContentRepository contentRepository) {
        this.contentMapper = contentMapper;
        this.contentService = contentService;
    }

    @GetMapping
    public Page<ContentDto> getContents(Pageable pageable) {
        Page<ContentEntity> contentEntities = contentService.findAll(pageable);
        return contentEntities.map(contentMapper::mapTo);
    }

    @PostMapping
    public ResponseEntity<ContentDto> createDisplayDevice(@RequestBody ContentDto contentdto) {
        ContentEntity contentEntity = contentMapper.mapFrom(contentdto);
        ContentEntity savedContentEntity = contentService.save(contentEntity);
        return new ResponseEntity<>(contentMapper.mapTo(savedContentEntity), HttpStatus.CREATED);
    }
}
