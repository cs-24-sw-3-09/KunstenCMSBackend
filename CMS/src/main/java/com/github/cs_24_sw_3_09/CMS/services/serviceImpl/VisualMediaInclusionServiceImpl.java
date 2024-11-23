package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaInclusionRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisualMediaInclusionServiceImpl implements VisualMediaInclusionService {

    private final VisualMediaInclusionRepository visualMediaInclusionRepository;
    private final VisualMediaService visualMediaService;

    public VisualMediaInclusionServiceImpl(VisualMediaInclusionRepository visualMediaInclusionRepository, VisualMediaService visualMediaService) {
        this.visualMediaInclusionRepository = visualMediaInclusionRepository;
        this.visualMediaService = visualMediaService;
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
        return visualMediaInclusionRepository.findAll(pageable);
    }

    @Override
    public Optional<VisualMediaInclusionEntity> findOne(Long id) {
        return visualMediaInclusionRepository.findById(Math.toIntExact(id));
    }

    @Override
    public boolean isExists(Long id) {
        return visualMediaInclusionRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public VisualMediaInclusionEntity partialUpdate(Long id, VisualMediaInclusionEntity visualMediaInclusionEntity) {
        visualMediaInclusionEntity.setId(Math.toIntExact(id));
        return visualMediaInclusionRepository.findById(Math.toIntExact(id)).map(existingVisualMediaInclusion -> {
            // if display device from request has name, we set it to the existing display device. (same with other atts)
            Optional.ofNullable(visualMediaInclusionEntity.getId()).ifPresent(existingVisualMediaInclusion::setId);
            Optional.ofNullable(visualMediaInclusionEntity.getSlideDuration()).ifPresent(existingVisualMediaInclusion::setSlideDuration);
            Optional.ofNullable(visualMediaInclusionEntity.getSlideshowPosition()).ifPresent(existingVisualMediaInclusion::setSlideshowPosition);
            Optional.ofNullable(visualMediaInclusionEntity.getVisualMedia()).ifPresent(existingVisualMediaInclusion::setVisualMedia);
            return visualMediaInclusionRepository.save(existingVisualMediaInclusion);
        }).orElseThrow(() -> new RuntimeException("Visual Media Inclusion does not exist"));
    }

    @Override
    public void delete(Long id) {
        visualMediaInclusionRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public VisualMediaInclusionEntity setVisualMedia(Long id, Long visualMediaId) {
        return visualMediaInclusionRepository.findById(Math.toIntExact(id)).map(existingVisualMediaInclusion -> {

            VisualMediaEntity foundVisualMediaEntity = visualMediaService.findOne(visualMediaId)
                    .orElseThrow(() -> new RuntimeException("Visual Media does not exist"));
            existingVisualMediaInclusion.setVisualMedia(foundVisualMediaEntity);

            return visualMediaInclusionRepository.save(existingVisualMediaInclusion);
        }).orElseThrow(() -> new RuntimeException("Visual Media inclusion does not exist"));

    }
}
