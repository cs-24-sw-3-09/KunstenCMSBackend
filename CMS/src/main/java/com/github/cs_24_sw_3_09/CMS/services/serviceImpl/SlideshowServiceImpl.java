package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SlideshowServiceImpl implements SlideshowService {

    private final VisualMediaInclusionService visualMediaInclusionService;
    private SlideshowRepository slideshowRepository;

    public SlideshowServiceImpl(SlideshowRepository slideshowRepository, VisualMediaInclusionService visualMediaInclusionService) {
        this.slideshowRepository = slideshowRepository;
        this.visualMediaInclusionService = visualMediaInclusionService;
    }


    @Override
    public SlideshowEntity save(SlideshowEntity slideshowEntity) {
        return slideshowRepository.save(slideshowEntity);
    }

    @Override
    public List<SlideshowEntity> findAll() {
        return StreamSupport.stream(slideshowRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Page<SlideshowEntity> findAll(Pageable pageable) {
        return slideshowRepository.findAll(pageable);
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
            // if display device from request has name, we set it to the existing display device. (same with other atts)
            Optional.ofNullable(slideshowEntity.getName()).ifPresent(existingSlideshow::setName);
            Optional.ofNullable(slideshowEntity.getIsArchived()).ifPresent(existingSlideshow::setIsArchived);
            Optional.ofNullable(slideshowEntity.getVisualMediaInclusionCollection()).ifPresent(existingSlideshow::setVisualMediaInclusionCollection);

            return slideshowRepository.save(existingSlideshow);

        }).orElseThrow(() -> new RuntimeException("Slideshow does not exist"));
    }

    @Override
    public void delete(Long id) {
        slideshowRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public SlideshowEntity addVisualMediaInclusion(Long id, Long visualMediaInclusionId) {
        return slideshowRepository.findById(Math.toIntExact(id)).map(existingDisplayDevice -> {
            VisualMediaInclusionEntity foundVisualMediaInclusionEntity = visualMediaInclusionService.findOne(visualMediaInclusionId)
                    .orElseThrow(() -> new RuntimeException("Visual media inclusion does not exist"));
            existingDisplayDevice.addVisualMediaInclusion(foundVisualMediaInclusionEntity);

            return slideshowRepository.save(existingDisplayDevice);
        }).orElseThrow(() -> new RuntimeException("Slideshow does not exist"));
    }
}
