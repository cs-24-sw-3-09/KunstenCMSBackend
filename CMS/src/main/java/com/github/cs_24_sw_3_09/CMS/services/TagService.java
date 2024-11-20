package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;

import java.util.Optional;

public interface TagService {

    TagEntity save(TagEntity tag);

    boolean isExists(Long id);

    Optional<TagEntity> findOne(Long tagId);
}
