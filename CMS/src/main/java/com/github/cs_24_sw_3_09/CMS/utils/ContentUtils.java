package com.github.cs_24_sw_3_09.CMS.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

import jakarta.persistence.EntityManager;

import java.util.Map;

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

    public boolean isValidType(String type) {
        return "visualMedia".equals(type) || "slideshow".equals(type);
    }

    public boolean isDisplayContentValid(Long displayContentId, String type) {
        if ("visualMedia".equals(type)) {
            return visualMediaService.isExists(displayContentId);
        } else if ("slideshow".equals(type)) {
            return slideshowService.isExists(displayContentId);
        }
        return false;
    }

    public SetTSContentValidationResult validateRequestBody(Map<String, Object> requestBody) {
        if (!requestBody.containsKey("displayContentId") || !requestBody.containsKey("type")) {
            return SetTSContentValidationResult.invalid("Missing 'displayContentId' or 'type' in request body.");
        }

        String type = requestBody.get("type").toString();
        if (!isValidType(type)) {
            return SetTSContentValidationResult.invalid("Invalid type. Supported types are 'visualMedia' and 'slideshow'.");
        }

        try {
            Long displayContentId = Long.valueOf(requestBody.get("displayContentId").toString());
            return SetTSContentValidationResult.valid(displayContentId, type);
        } catch (NumberFormatException e) {
            return SetTSContentValidationResult.invalid("'displayContentId' must be a valid number.");
        }
    }
}
