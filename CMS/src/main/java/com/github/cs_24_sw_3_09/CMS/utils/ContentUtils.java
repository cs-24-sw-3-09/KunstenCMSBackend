package com.github.cs_24_sw_3_09.CMS.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

import jakarta.persistence.EntityManager;

@Component
public class ContentUtils {

    private final EntityManager entityManager;
    private final VisualMediaService visualMediaService;
    private final SlideshowService slideshowService;

    @Autowired
    public ContentUtils(EntityManager entityManager, VisualMediaService visualMediaService,
            SlideshowService slideshowService) {
        this.entityManager = entityManager;
        this.visualMediaService = visualMediaService;
        this.slideshowService = slideshowService;

    }

    public String getContentTypeById(Integer id) {
        ContentEntity content = entityManager.find(ContentEntity.class, id);
        if (content != null) {
            return content.getClass().getSimpleName();
        }
        return null;
    }

    public boolean isFallbackContentValid(String type, Long fallbackId) {
        return switch (type) {
            case "VisualMediaEntity" -> visualMediaService.isExists(fallbackId);
            case "SlideshowEntity" -> slideshowService.isExists(fallbackId);
            default -> false;
        };
    }
}
