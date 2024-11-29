package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DisplayDeviceService {
    Optional<DisplayDeviceEntity> save(DisplayDeviceEntity displayDevice);

    List<DisplayDeviceEntity> findAll();

    Page<DisplayDeviceEntity> findAll(Pageable pageable);

    Optional<DisplayDeviceEntity> findOne(Long id);

    boolean isExists(Long id);

    DisplayDeviceEntity partialUpdate(Long id, DisplayDeviceEntity displayDeviceEntity);

    void delete(Long id);

    DisplayDeviceEntity setFallbackContent(Long id, Long fallbackId, String type);

    DisplayDeviceEntity addTimeSlot(Long id, Long timeslotId);

}
