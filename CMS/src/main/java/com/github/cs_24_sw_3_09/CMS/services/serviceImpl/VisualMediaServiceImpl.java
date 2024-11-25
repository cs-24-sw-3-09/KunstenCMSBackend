package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.TagRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import com.github.cs_24_sw_3_09.CMS.utils.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import jakarta.persistence.EntityNotFoundException;

@Service
public class VisualMediaServiceImpl implements VisualMediaService {

    private final VisualMediaRepository visualMediaRepository;
    private final TagRepository tagRepository;
    private PushTSService pushTSService;

    public VisualMediaServiceImpl(VisualMediaRepository visualMediaRepository,
            TagRepository tagRepository, PushTSService pushTSService) {
        this.visualMediaRepository = visualMediaRepository;
        this.tagRepository = tagRepository;
        this.pushTSService = pushTSService;

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
    public VisualMediaEntity addTag(Long id, Long tagId) {

        VisualMediaEntity foundVisualMedia = visualMediaRepository.findById(Math.toIntExact(id)).get();

        TagEntity foundTag = tagRepository.findById(tagId).get();

        foundVisualMedia.addTag(foundTag);
        foundVisualMedia.setId(Math.toIntExact(id));
        visualMediaRepository.save(foundVisualMedia);

        return foundVisualMedia;
    }

    @Override
    public void delete(Long id) {
        VisualMediaEntity timeslot = visualMediaRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("Visual Media with id " + id + " not found"));
        timeslot.getTags().clear();
        visualMediaRepository.save(timeslot);
        visualMediaRepository.deleteById(Math.toIntExact(id));
        pushTSService.updateDisplayDevicesToNewTimeSlots();
    }
}
