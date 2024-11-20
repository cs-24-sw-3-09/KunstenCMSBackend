package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface VisualMediaService {

    VisualMediaEntity save(VisualMediaEntity visualMedia);

    List<VisualMediaEntity> findAll();

    Page<VisualMediaEntity> findAll(Pageable pageable);

    Optional<VisualMediaEntity> findOne(Long id);

    List<TagEntity> getVisualMediaTags(Long id);

    boolean isExists(Long id);

    VisualMediaEntity partialUpdate(Long id, VisualMediaEntity visualMediaEntity);

    void delete(Long id);
}
