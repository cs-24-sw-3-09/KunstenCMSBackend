package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.Optional;

public interface DisplayDeviceService {
    DisplayDeviceEntity save(DisplayDeviceEntity displayDevice);

    List<DisplayDeviceEntity> findAll();

    Page<DisplayDeviceEntity> findAll(Pageable pageable);

    Optional<DisplayDeviceEntity> findOne(Long id);

    boolean isExists(Long id);

    DisplayDeviceEntity partialUpdate(Long id, DisplayDeviceEntity displayDeviceEntity);

    void delete(Long id);
}
