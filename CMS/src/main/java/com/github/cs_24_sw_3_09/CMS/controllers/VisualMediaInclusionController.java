package com.github.cs_24_sw_3_09.CMS.controllers;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaInclusionDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

@RestController
@RequestMapping("/api/visual_media_inclusions")
public class VisualMediaInclusionController {

    private Mapper<VisualMediaInclusionEntity, VisualMediaInclusionDto> visualMediaInclusionMapper;
    private VisualMediaInclusionService visualMediaInclusionService;

    public VisualMediaInclusionController(Mapper<VisualMediaInclusionEntity, VisualMediaInclusionDto> visualMediaInclusionMapper, VisualMediaInclusionService visualMediaInclusionService) {
        this.visualMediaInclusionMapper = visualMediaInclusionMapper;
        this.visualMediaInclusionService = visualMediaInclusionService;
    }

    @GetMapping
    public Page<VisualMediaInclusionDto> getVisualMediaInclusions(Pageable pageable) {
        Page<VisualMediaEntity> visualMediaEntities = visualMediaInclusionService.findAll(pageable);
        return visualMediaEntities.map(visualMediaInclusionMapper::mapTo);
    }

}
