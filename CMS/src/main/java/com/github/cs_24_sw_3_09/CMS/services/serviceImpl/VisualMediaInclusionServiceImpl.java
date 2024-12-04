package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaInclusionRepository;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VisualMediaInclusionServiceImpl implements VisualMediaInclusionService {

    private final VisualMediaInclusionRepository visualMediaInclusionRepository;
    private final VisualMediaService visualMediaService;
    private PushTSService pushTSService;

    public VisualMediaInclusionServiceImpl(VisualMediaInclusionRepository visualMediaInclusionRepository,
            VisualMediaService visualMediaService, PushTSService pushTSService) {
        this.visualMediaInclusionRepository = visualMediaInclusionRepository;
        this.visualMediaService = visualMediaService;
        this.pushTSService = pushTSService;

    }

    @Override
    public Optional<VisualMediaInclusionEntity> save(VisualMediaInclusionEntity visualMediaInclusionEntity) {
        if (visualMediaInclusionEntity.getVisualMedia() != null && 
        visualMediaInclusionEntity.getVisualMedia().getId() != null) {
            Optional<VisualMediaInclusionEntity> toCheck = getVisualMedia(visualMediaInclusionEntity); 
            if (toCheck.isEmpty()) return Optional.empty();
            visualMediaInclusionEntity = toCheck.get();
        }

        VisualMediaInclusionEntity toReturn = visualMediaInclusionRepository.save(visualMediaInclusionEntity);
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        return Optional.of(toReturn);
    }

    private Optional<VisualMediaInclusionEntity> getVisualMedia(VisualMediaInclusionEntity visualMediaInclusionEntity) {
        Optional<VisualMediaEntity> visualMedia = visualMediaService.findOne((long) visualMediaInclusionEntity.getVisualMedia().getId());
        if (visualMedia.isEmpty()) return Optional.empty();
        visualMediaInclusionEntity.setVisualMedia(visualMedia.get());
        return Optional.of(visualMediaInclusionEntity);
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
            // if display device from request has name, we set it to the existing display
            // device. (same with other atts)
            Optional.ofNullable(visualMediaInclusionEntity.getId()).ifPresent(existingVisualMediaInclusion::setId);
            Optional.ofNullable(visualMediaInclusionEntity.getSlideDuration())
                    .ifPresent(existingVisualMediaInclusion::setSlideDuration);
            Optional.ofNullable(visualMediaInclusionEntity.getSlideshowPosition())
                    .ifPresent(existingVisualMediaInclusion::setSlideshowPosition);
            Optional.ofNullable(visualMediaInclusionEntity.getVisualMedia())
                    .ifPresent(existingVisualMediaInclusion::setVisualMedia);

            VisualMediaInclusionEntity toReturn = visualMediaInclusionRepository.save(existingVisualMediaInclusion);
            pushTSService.updateDisplayDevicesToNewTimeSlots();
            return toReturn;
        }).orElseThrow(() -> new RuntimeException("Visual Media Inclusion does not exist"));
    }

    @Override
    public void delete(Long id) {
        VisualMediaInclusionEntity visualMediaInclusion = visualMediaInclusionRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("Visual Media Inclusion with id " + id + " not found"));
        visualMediaInclusion.setVisualMedia(null);
        visualMediaInclusionRepository.save(visualMediaInclusion);
        visualMediaInclusionRepository.deleteById(Math.toIntExact(id));
        pushTSService.updateDisplayDevicesToNewTimeSlots();
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

    @Override
    public Optional<List<VisualMediaInclusionEntity>> updateSlideshowPosition(
            List<VisualMediaInclusionEntity> visualMediaInclusions) {

        List<VisualMediaInclusionEntity> visualMediaInclusionsToReturn = new ArrayList<>();
        for (VisualMediaInclusionEntity visualMediaInclusion : visualMediaInclusions) {
            VisualMediaInclusionEntity visualMediaInclusionEntityToUpdate = findOne((long) visualMediaInclusion.getId()).orElse(null);
            
            if(visualMediaInclusionEntityToUpdate == null) return Optional.empty();
            visualMediaInclusionEntityToUpdate.setSlideshowPosition(visualMediaInclusion.getSlideshowPosition());

            visualMediaInclusionsToReturn.add(visualMediaInclusionEntityToUpdate);
        }
        visualMediaInclusionRepository.saveAll(visualMediaInclusionsToReturn);
        
        return Optional.of(visualMediaInclusionsToReturn);
    }

    
}
