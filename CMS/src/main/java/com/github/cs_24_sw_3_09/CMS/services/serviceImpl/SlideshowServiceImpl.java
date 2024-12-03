package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SlideshowServiceImpl implements SlideshowService {

    private final VisualMediaInclusionService visualMediaInclusionService;
    private SlideshowRepository slideshowRepository;
    private PushTSService pushTSService;

    public SlideshowServiceImpl(SlideshowRepository slideshowRepository,
            VisualMediaInclusionService visualMediaInclusionService, PushTSService pushTSService) {
        this.slideshowRepository = slideshowRepository;
        this.pushTSService = pushTSService;
        this.visualMediaInclusionService = visualMediaInclusionService;
    }

    @Override
    @Transactional
    public SlideshowEntity save(SlideshowEntity slideshowEntity) {
        SlideshowEntity toReturn = slideshowRepository.save(slideshowEntity);
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        return toReturn;
    }

    @Override
    public List<SlideshowEntity> findAll() {
        return StreamSupport.stream(slideshowRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Iterable<SlideshowEntity> findAll(Pageable pageable) {
        return slideshowRepository.findAll();
    }

    @Override
    public Optional<SlideshowEntity> findOne(Long id) {
        return slideshowRepository.findById(Math.toIntExact(id));
    }

    @Override
    public boolean isExists(Long id) {
        return slideshowRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public SlideshowEntity partialUpdate(Long id, SlideshowEntity slideshowEntity)
            throws RuntimeException {
        return slideshowRepository.findById(Math.toIntExact(id)).map(existingSlideshow -> {
            // if display device from request has name, we set it to the existing display
            // device. (same with other atts)
            Optional.ofNullable(slideshowEntity.getName()).ifPresent(existingSlideshow::setName);
            Optional.ofNullable(slideshowEntity.getIsArchived()).ifPresent(existingSlideshow::setIsArchived);
            Optional.ofNullable(slideshowEntity.getVisualMediaInclusionCollection())
                    .ifPresent(existingSlideshow::setVisualMediaInclusionCollection);

            SlideshowEntity toReturn = slideshowRepository.save(existingSlideshow);
            pushTSService.updateDisplayDevicesToNewTimeSlots();
            return toReturn;

        }).orElseThrow(() -> new RuntimeException("Slideshow does not exist"));
    }

    @Override
    public void delete(Long id) {
        SlideshowEntity slideshow = slideshowRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("Slideshow with id " + id + " not found"));

        slideshow.getVisualMediaInclusionCollection().clear();
        slideshowRepository.save(slideshow);
        slideshowRepository.deleteById(Math.toIntExact(id));
        pushTSService.updateDisplayDevicesToNewTimeSlots();
    }

    @Override
    public SlideshowEntity addVisualMediaInclusion(Long id, Long visualMediaInclusionId) {
        return slideshowRepository.findById(Math.toIntExact(id)).map(existingDisplayDevice -> {
            VisualMediaInclusionEntity foundVisualMediaInclusionEntity = visualMediaInclusionService
                    .findOne(visualMediaInclusionId)
                    .orElseThrow(() -> new RuntimeException("Visual media inclusion does not exist"));
            existingDisplayDevice.addVisualMediaInclusion(foundVisualMediaInclusionEntity);

            return slideshowRepository.save(existingDisplayDevice);
        }).orElseThrow(() -> new RuntimeException("Slideshow does not exist"));
    }

    @Override
    public Optional<SlideshowEntity> duplicate(Long id, String name) {
        Optional<SlideshowEntity> slideshowToDuplicate = findOne(id);
        if(slideshowToDuplicate.isEmpty()) return Optional.empty();
        

        return Optional.empty();
    }
    
}
