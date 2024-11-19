package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContentService {
    ContentEntity save(ContentEntity contentEntity);

    Page<ContentEntity> findAll(Pageable pageable);

    Optional<ContentEntity> findOne(Long id);

    boolean isExists(Long id);

    ContentEntity partialUpdate(Long id, ContentEntity contentEntity);

    void delete(Long id);

}
