package com.github.cs_24_sw_3_09.CMS.services;

import java.util.Set;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

public interface DimensionCheckService {

    String checkDimensionForAssignedFallback(DisplayDeviceEntity displayDevice, ContentEntity fallbackContent);

    String checkDimensionForAssignedVisualMediaToSlideshow(Long visualMediaInclusionId, Long slideshowId);
    
    String checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(ContentEntity displayContent, Set<DisplayDeviceEntity> displayDevices);
}