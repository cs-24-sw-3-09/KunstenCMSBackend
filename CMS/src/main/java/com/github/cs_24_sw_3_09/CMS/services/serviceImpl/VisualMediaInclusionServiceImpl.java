package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaInclusionRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class VisualMediaInclusionServiceImpl implements VisualMediaInclusionService {

    private VisualMediaInclusionRepository visualMediaInclusionRepository;

    public VisualMediaInclusionServiceImpl(VisualMediaInclusionRepository visualMediaInclusionRepository) {
        this.visualMediaInclusionRepository = visualMediaInclusionRepository;
    }

    @Override
    public VisualMediaInclusionEntity save(VisualMediaInclusionEntity visualMediaInclusionEntity) {
        return visualMediaInclusionRepository.save(visualMediaInclusionEntity);
    }

    @Override
    public List<VisualMediaInclusionEntity> findAll() {
        return List.of();
    }

    @Override
    public Page<VisualMediaInclusionEntity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<VisualMediaInclusionEntity> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean isExists(Long id) {
        return false;
    }

    @Override
    public VisualMediaEntity partialUpdate(Long id, VisualMediaInclusionEntity visualMediaInclusionEntity) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
