package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface VisualMediaInclusionService {
    VisualMediaInclusionEntity save(VisualMediaInclusionEntity visualMediaInclusionEntity);

    List<VisualMediaInclusionEntity> findAll();

    Page<VisualMediaInclusionEntity> findAll(Pageable pageable);

    Optional<VisualMediaInclusionEntity> findOne(Long id);

    boolean isExists(Long id);

    VisualMediaInclusionEntity partialUpdate(Long id, VisualMediaInclusionEntity visualMediaInclusionEntity);

    void delete(Long id);

    VisualMediaInclusionEntity setVisualMedia(Long id, Long visualMediaId);

    Optional<List<VisualMediaInclusionEntity>> updateSlideshowPosition(List<VisualMediaInclusionEntity> visualMediaInclusions);
}
