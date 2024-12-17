package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.dto.SlideshowDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.utils.Result;

import org.json.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface SlideshowService {
    SlideshowEntity save(SlideshowEntity slideshowEntity);

    List<SlideshowEntity> findAll();

    Iterable<SlideshowEntity> findAll(Pageable pageable);

    Optional<SlideshowEntity> findOne(Long id);

    boolean isExists(Long id);

    Set<SlideshowDto>findPartOfSlideshows(Long id);

    SlideshowEntity partialUpdate(Long id, SlideshowEntity slideshowEntity);

    void delete(Long id);

    Result<SlideshowEntity, String> addVisualMediaInclusion(Long id, Long visualMediaInclusionId, Boolean forceDimensions);

    List<Map<String, Object>> findStateOfEverySlideshow();

    Optional<SlideshowEntity> duplicate(Long id, String name); 
}
