package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.dto.SlideshowDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

import org.json.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SlideshowService {
    SlideshowEntity save(SlideshowEntity slideshowEntity);

    List<SlideshowEntity> findAll();

    Page<SlideshowEntity> findAll(Pageable pageable);

    Optional<SlideshowEntity> findOne(Long id);

    boolean isExists(Long id);

    Set<SlideshowDto>findPartOfSlideshows(Long id);

    SlideshowEntity partialUpdate(Long id, SlideshowEntity slideshowEntity);

    void delete(Long id);

    SlideshowEntity addVisualMediaInclusion(Long id, Long visualMediaInclusionId);

    JSONArray findStateOfEverySlideshow();
}
