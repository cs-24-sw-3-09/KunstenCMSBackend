package com.github.cs_24_sw_3_09.CMS.services;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;

public interface DimensionCheckService {

    Boolean checkDimensionForAssignedFallback(Long displayDeviceId);

    Boolean checkDimensionForAssignedVisualMediaToSlideshow(Long visualMediaInclusionId, Long slideshowId);
    
    Boolean checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(long timeSlotId);
}
