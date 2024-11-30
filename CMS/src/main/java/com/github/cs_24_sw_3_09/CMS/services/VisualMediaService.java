package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VisualMediaService {

    VisualMediaEntity save(VisualMediaEntity visualMedia);

    List<VisualMediaEntity> findAll();

    Page<VisualMediaEntity> findAll(Pageable pageable);

    Optional<VisualMediaEntity> findOne(Long id);

    List<TagEntity> getVisualMediaTags(Long id);

    Set<SlideshowEntity>findPartOfSlideshows(Long id);

    boolean isExists(Long id);

    VisualMediaEntity partialUpdate(Long id, VisualMediaEntity visualMediaEntity);

    void delete(Long id);

    Optional<VisualMediaEntity> addTag(Long id, Long tagId);

    void deleteRelation(Long visualMediaId, Long tagId);

    List<DisplayDeviceEntity> findDisplayDevicesVisualMediaIsPartOf(Long id);

    List<TimeSlotEntity> findTimeslotsVisualMediaIsPartOf(Long id);
}
