package com.github.cs_24_sw_3_09.CMS.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;

public interface TagService {
    TagEntity save(TagEntity tag);

    boolean isExists(Long id);

    Optional<TagEntity> findOne(Long tagId);

    Page<TagEntity> findAll(Pageable pageable);

    void delete(Long id);

    TagEntity partialUpdate(Long id, TagEntity tagEntity);

}
