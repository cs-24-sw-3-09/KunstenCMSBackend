package com.github.cs_24_sw_3_09.CMS.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.utils.Result;

public interface DisplayDeviceService {
    Result<DisplayDeviceEntity, String> save(DisplayDeviceEntity displayDevice, Boolean forceDimensions);

    List<DisplayDeviceEntity> findAll();

    Page<DisplayDeviceEntity> findAll(Pageable pageable);

    Optional<DisplayDeviceEntity> findOne(Long id);

    boolean isExists(Long id);

    Set<DisplayDeviceDto> findDisplayDevicesWhoUsesSlideshowAsFallback(Long id);

    DisplayDeviceEntity partialUpdate(Long id, DisplayDeviceEntity displayDeviceEntity);

    void delete(Long id);

    DisplayDeviceEntity setFallbackContent(Long id, Long fallbackId, String type);

    DisplayDeviceEntity addTimeSlot(Long id, Long timeslotId);

    Result<DisplayDeviceEntity, String> addFallback(Long id, Long fallbackId, Boolean forceDimensions);

}
