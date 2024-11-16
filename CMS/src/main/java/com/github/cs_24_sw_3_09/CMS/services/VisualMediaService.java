package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;

import java.util.List;
import java.util.Optional;

public interface VisualMediaService {

    VisualMediaEntity createVisualMedia(VisualMediaEntity visualMedia);
    List<VisualMediaEntity> findAll();

    Optional<VisualMediaEntity> findOne(Long id);
}
