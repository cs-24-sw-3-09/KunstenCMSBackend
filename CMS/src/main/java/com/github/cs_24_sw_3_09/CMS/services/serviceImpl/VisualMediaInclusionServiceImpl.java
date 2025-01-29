package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// JCodec imports
import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.io.NIOUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaInclusionRepository;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class VisualMediaInclusionServiceImpl implements VisualMediaInclusionService {

    private final VisualMediaInclusionRepository visualMediaInclusionRepository;
    private final VisualMediaService visualMediaService;
    private PushTSService pushTSService;
    private SlideshowRepository slideshowRepository;

    public VisualMediaInclusionServiceImpl(VisualMediaInclusionRepository visualMediaInclusionRepository,
            VisualMediaService visualMediaService, PushTSService pushTSService,
            SlideshowRepository slideshowRepository) {
        this.visualMediaInclusionRepository = visualMediaInclusionRepository;
        this.visualMediaService = visualMediaService;
        this.pushTSService = pushTSService;
        this.slideshowRepository = slideshowRepository;
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
        Optional<VisualMediaEntity> optionalVisualMedia = visualMediaService.findOne((long) visualMediaInclusionEntity.getVisualMedia().getId());
        if (optionalVisualMedia.isEmpty()) return Optional.empty();
        VisualMediaEntity visualMedia = optionalVisualMedia.get();
        visualMediaInclusionEntity.setVisualMedia(visualMedia);

        //if the visual media is a video, the slideDuration field should have the length of the video
        if (visualMedia != null && (visualMedia.getFileType().equals("video/mp4") 
            || visualMedia.getFileType().equals("mp4"))) {
            Integer videoDuration = findVideoDuration(visualMedia.getLocation());
            visualMediaInclusionEntity.setSlideDuration(videoDuration);
        }

        return Optional.of(visualMediaInclusionEntity);
    }

    private Integer findVideoDuration(String visualMediaPath){
        String rootPath = System.getProperty("user.dir"); 
        String relativePath = visualMediaPath;
        String absolutePath = Paths.get(rootPath, relativePath).toString();

        try {
            File videoFile = new File(absolutePath);
            //SeekableByteChannel -> used to read video file efficiently
            SeekableByteChannel channel = NIOUtils.readableChannel(videoFile);
            // Get frame-level access to video
            FrameGrab grab = FrameGrab.createFrameGrab(channel);
            // Get the video duration in seconds, must return a double
            double durationInSeconds = grab.getVideoTrack().getMeta().getTotalDuration();
            //convert to field type Integer
            Integer durationsInSecondsInteger = Integer.valueOf((int) Math.round(durationInSeconds));
            return durationsInSecondsInteger;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
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
            Optional.ofNullable(visualMediaInclusionEntity.getSlideshowPosition())
                    .ifPresent(existingVisualMediaInclusion::setSlideshowPosition);
            Optional.ofNullable(visualMediaInclusionEntity.getVisualMedia())
                    .ifPresent(existingVisualMediaInclusion::setVisualMedia);

            Optional.ofNullable(visualMediaInclusionEntity.getSlideDuration())
                    .ifPresent(slideDuration -> {

                        // Check if visual media exists and has file type "mp4"
                        VisualMediaEntity visualMedia = visualMediaInclusionEntity.getVisualMedia();
                        if (visualMedia != null && (visualMedia.getFileType().equals("video/mp4") 
                        || visualMedia.getFileType().equals("mp4"))) {
                            // Calculate the slide duration using the method
                            slideDuration = findVideoDuration(visualMedia.getLocation());
                        }
                        existingVisualMediaInclusion.setSlideDuration(slideDuration);
                    });
            VisualMediaInclusionEntity toReturn = visualMediaInclusionRepository.save(existingVisualMediaInclusion);
            pushTSService.updateDisplayDevicesToNewTimeSlots();
            return toReturn;
        }).orElseThrow(() -> new RuntimeException("Visual Media Inclusion does not exist"));
    }

    @Transactional
    @Override
    public void delete(VisualMediaInclusionEntity vmi) {
        Long id = (long) vmi.getId();
        this.delete(id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        VisualMediaInclusionEntity visualMediaInclusion = visualMediaInclusionRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("Visual Media Inclusion with id " + id + " not found"));

        // Store the slideshow and position information before deletion
        Long SSId = slideshowRepository.findSlideshowIdByVisualMediaInclusionId(id);
        Integer position = visualMediaInclusion.getSlideshowPosition();

        visualMediaInclusion.setVisualMedia(null);
        visualMediaInclusionRepository.save(visualMediaInclusion);
        visualMediaInclusionRepository.deleteById(Math.toIntExact(id));

        this.updatePositionsAfterDeletion(SSId, position);

        pushTSService.updateDisplayDevicesToNewTimeSlots();
    }

    @Transactional
    @Override
    public void updatePositionsAfterDeletion(Long slideshowId, Integer deletedPosition) {
        visualMediaInclusionRepository.updatePositionsAfterDeletion(slideshowId, deletedPosition);
    }

    @Override
    public VisualMediaInclusionEntity setVisualMedia(Long id, Long visualMediaId) {
        return visualMediaInclusionRepository.findById(Math.toIntExact(id)).map(existingVisualMediaInclusion -> {

            VisualMediaEntity foundVisualMediaEntity = visualMediaService.findOne(visualMediaId)
                    .orElseThrow(() -> new RuntimeException("Visual Media does not exist"));
            existingVisualMediaInclusion.setVisualMedia(foundVisualMediaEntity);

            //if the visual media is a video -> calculate duration
            if (foundVisualMediaEntity != null && (foundVisualMediaEntity.getFileType().equals("video/mp4") 
                || foundVisualMediaEntity.getFileType().equals("mp4"))) {
                // Calculate the slide duration
                Integer slideDuration = findVideoDuration(foundVisualMediaEntity.getLocation());
                existingVisualMediaInclusion.setSlideDuration(slideDuration);
            }

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
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        
        return Optional.of(visualMediaInclusionsToReturn);
    }

    @Override
    public Set<VisualMediaInclusionEntity> findAllVisualMediaInclusionInSlideshow(long slideshowId){
        return visualMediaInclusionRepository.findAllVisualMediaInclusionForSlideshow(slideshowId);
    }
    
}
