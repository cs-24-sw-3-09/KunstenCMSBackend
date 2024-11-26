package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TagService {
    TagEntity save(TagEntity tag);

    boolean isExists(Long id);

    Optional<TagEntity> findOne(Long tagId);

    Page<TagEntity> findAll(Pageable pageable);

    void delete(Long id);

    TagEntity partialUpdate(Long id, TagEntity tagEntity);

}
