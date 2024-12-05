package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;

public interface DimensionCheckService {

    String checkDimensionForAssignedFallback(Long displayDeviceId);

    String checkDimensionForAssignedVisualMediaToSlideshow(Long visualMediaInclusionId, Long slideshowId);
    
    String checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(long timeSlotId);
}
