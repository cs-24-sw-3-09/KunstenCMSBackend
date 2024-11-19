package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.ContentRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.services.ContentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService {


    private ContentRepository contentRepository;

    public ContentServiceImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public Page<ContentEntity> findAll(Pageable pageable) {
        return contentRepository.findAll(pageable);
    }

    @Override
    public ContentEntity save(ContentEntity contentEntity) {
        return contentRepository.save(contentEntity);
    }

    @Override
    public Optional<ContentEntity> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean isExists(Long id) {
        return false;
    }

    @Override
    public ContentEntity partialUpdate(Long id, ContentEntity contentEntity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
