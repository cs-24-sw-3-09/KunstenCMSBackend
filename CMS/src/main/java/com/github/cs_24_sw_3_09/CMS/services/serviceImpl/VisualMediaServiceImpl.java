package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.github.cs_24_sw_3_09.CMS.model.dto.VisualMediaDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import com.github.cs_24_sw_3_09.CMS.utils.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.repositories.TagRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaInclusionRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

import java.util.Set;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VisualMediaServiceImpl implements VisualMediaService {

    private final VisualMediaRepository visualMediaRepository;
    private final VisualMediaInclusionRepository visualMediaInclusionRepository;
    private final TagRepository tagRepository;
    private PushTSService pushTSService;
    private SlideshowRepository slideshowRepository;

    public VisualMediaServiceImpl(VisualMediaRepository visualMediaRepository, TagServiceImpl tagService,
            TagRepository tagRepository, PushTSService pushTSService, SlideshowRepository slideshowRepository,
            VisualMediaInclusionRepository visualMediaInclusionRepository) {
        this.visualMediaRepository = visualMediaRepository;
        this.tagRepository = tagRepository;
        this.pushTSService = pushTSService;
        this.slideshowRepository = slideshowRepository;
        this.visualMediaInclusionRepository = visualMediaInclusionRepository;
    }

    @Override
    public VisualMediaEntity save(VisualMediaEntity visualMedia) {
        VisualMediaEntity toReturn = visualMediaRepository.save(visualMedia);
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        return toReturn;
    }

    @Override
    public List<VisualMediaEntity> findAll() {
        return StreamSupport.stream(visualMediaRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Page<VisualMediaEntity> findAll(Pageable pageable) {
        return visualMediaRepository.findAll(pageable);
    }

    @Override
    public Optional<VisualMediaEntity> findOne(Long id) {
        return visualMediaRepository.findById(Math.toIntExact(id));
    }

    @Override
    public List<TagEntity> getVisualMediaTags(Long id) {
        Optional<VisualMediaEntity> vm = visualMediaRepository.findById(Math.toIntExact(id));

        return vm.map(visualMediaEntity -> new ArrayList<>(visualMediaEntity.getTags()))
                .orElseThrow(() -> new RuntimeException("Visual media not found"));
    }

    @Override
    public Set<SlideshowEntity> findPartOfSlideshows(Long id) {
        return slideshowRepository.findSlideshowsByVisualMediaId(id);
    }

    @Override
    public boolean isExists(Long id) {
        return visualMediaRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public VisualMediaEntity partialUpdate(Long id, VisualMediaEntity visualMediaEntity) {
        visualMediaEntity.setId(Math.toIntExact(id));
        return visualMediaRepository.findById(Math.toIntExact(id)).map(existingVisualMedia -> {
            // if display device from request has name, we set it to the existing display
            // device. (same with other atts)
            Optional.ofNullable(visualMediaEntity.getName()).ifPresent(existingVisualMedia::setName);
            Optional.ofNullable(visualMediaEntity.getLocation()).ifPresent(existingVisualMedia::setLocation);
            Optional.ofNullable(visualMediaEntity.getDescription()).ifPresent(existingVisualMedia::setDescription);
            Optional.ofNullable(visualMediaEntity.getFileType()).ifPresent(existingVisualMedia::setFileType);
            Optional.ofNullable(visualMediaEntity.getLastDateModified())
                    .ifPresent(existingVisualMedia::setLastDateModified);
            Optional.ofNullable(visualMediaEntity.getTags()).ifPresent(existingVisualMedia::setTags);

            VisualMediaEntity toReturn = visualMediaRepository.save(existingVisualMedia);
            pushTSService.updateDisplayDevicesToNewTimeSlots();
            return toReturn;
        }).orElseThrow(() -> new RuntimeException("Visual Media Not Found"));
    }

    @Override
    public Optional<VisualMediaEntity> addTag(Long id, String text) {
        //Have already checked that it exists in the controller
        VisualMediaEntity foundVisualMedia = visualMediaRepository.findById(Math.toIntExact(id)).get();

        TagEntity foundTag = tagRepository.findByText(text);

        //If tag is not found, we create a new tag with the text.
        if (foundTag == null) {

            TagEntity newTag = TagEntity.builder().text(text).build();
            foundTag = tagRepository.save(newTag);
        }

        foundVisualMedia.addTag(foundTag);
        foundVisualMedia.setId(Math.toIntExact(id));
        visualMediaRepository.save(foundVisualMedia);
        return Optional.of(foundVisualMedia);
    }

    @Override
    public void delete(Long id) {
        VisualMediaEntity VM = visualMediaRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("Visual Media with id " + id + " not found"));
        VM.getTags().clear();
        List<VisualMediaInclusionEntity> inclusions = visualMediaInclusionRepository.findAllByVisualMedia(VM);
        inclusions.forEach(visualMediaInclusionRepository::delete);

        visualMediaRepository.save(VM);
        visualMediaRepository.deleteById(Math.toIntExact(id));
        pushTSService.updateDisplayDevicesToNewTimeSlots();
    }

    @Override
    public VisualMediaEntity deleteRelation(Long visualMediaId, Long tagId) {
        VisualMediaEntity visualMedia = visualMediaRepository.findById(Math.toIntExact(visualMediaId)).get();
        TagEntity tag = tagRepository.findById(tagId).get();

        visualMedia.getTags().remove(tag);
        return visualMediaRepository.save(visualMedia);
    }

    @Override
    public List<DisplayDeviceEntity> findDisplayDevicesVisualMediaIsPartOf(Long id) {
        return visualMediaRepository.getDisplayDevicesPartOfVisualMedia(id);
    }

    @Override
    public List<TimeSlotEntity> findTimeslotsVisualMediaIsPartOf(Long id) {
        return visualMediaRepository.getTimeslotsPartOfVisualMedia(id);
    }

    @Override
    public HttpStatus replaceFileById(Long id, MultipartFile file) throws IOException {

        VisualMediaEntity visualMediaEntity = findOne(id).orElseThrow();

        //Starts by deleting existing file from folder.
        FileUtils.removeVisualMediaFile(visualMediaEntity);

        //Updates the vm in database to be the new filetype
        visualMediaEntity.setFileType(file.getContentType());
        visualMediaRepository.save(visualMediaEntity);

        //Created the new file.
        FileUtils.createVisualMediaFile(file, String.valueOf(id));
        return HttpStatus.OK;

    }
}
