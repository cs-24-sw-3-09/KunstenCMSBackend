package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
import jakarta.persistence.EntityNotFoundException;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DisplayDeviceServiceImpl implements DisplayDeviceService {

    private final TimeSlotService timeSlotService;
    private DisplayDeviceRepository displayDeviceRepository;
    private VisualMediaRepository visualMediaRepository;
    private SlideshowRepository slideshowRepository;
    private PushTSService pushTSService;

    public DisplayDeviceServiceImpl(DisplayDeviceRepository displayDeviceRepository, VisualMediaRepository visualMediaRepository, SlideshowRepository slideshowRepository, TimeSlotService timeSlotService,
                                   PushTSService pushTSService) {
        this.displayDeviceRepository = displayDeviceRepository;
        this.visualMediaRepository = visualMediaRepository;
        this.slideshowRepository = slideshowRepository;
        this.pushTSService = pushTSService;
        this.timeSlotService = timeSlotService;
    }

    @Override
    public Optional<DisplayDeviceEntity> findOne(Long id) {
        return displayDeviceRepository.findById(Math.toIntExact(id));
    }

    @Override
    public List<DisplayDeviceEntity> findAll() {
        // return itterable, so we convert it to list.
        return StreamSupport.stream(displayDeviceRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DisplayDeviceEntity> findAll(Pageable pageable) {
        return displayDeviceRepository.findAll(pageable);
    }

    @Override
    public Optional<DisplayDeviceEntity> save(DisplayDeviceEntity displayDeviceEntity) {
        if (displayDeviceEntity.getFallbackContent() != null) {
            Optional<DisplayDeviceEntity> displayDevice = addFallbackContent(displayDeviceEntity);
            if (displayDevice.isEmpty()) return Optional.empty();
            displayDeviceEntity = displayDevice.get();
        }

        DisplayDeviceEntity toReturn = displayDeviceRepository.save(displayDeviceEntity);
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        return Optional.of(toReturn);
    }

    private Optional<DisplayDeviceEntity> addFallbackContent(DisplayDeviceEntity displayDeviceEntity) {
        Integer currentContentId = displayDeviceEntity.getFallbackContent().getId();
        Optional<ContentEntity> optionalContent = findContentById(currentContentId);
        if (optionalContent.isEmpty()) return Optional.empty(); 
        displayDeviceEntity.setFallbackContent(optionalContent.get());
        
        return Optional.of(displayDeviceEntity);
    }


    private Optional<ContentEntity> findContentById(Integer contentId) {
        if (slideshowRepository.existsById(contentId)) {
            return Optional.of(slideshowRepository.findById(contentId).orElse(null));
        } else if (visualMediaRepository.existsById(contentId)) {
            return Optional.of(visualMediaRepository.findById(contentId).orElse(null));
        }
        return Optional.empty();
    }

    @Override
    public boolean isExists(Long id) {
        return displayDeviceRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public DisplayDeviceEntity partialUpdate(Long id, DisplayDeviceEntity displayDeviceEntity) {
        displayDeviceEntity.setId(Math.toIntExact(id));
        return displayDeviceRepository.findById(Math.toIntExact(id)).map(existingDisplayDevice -> {
            // if display device from request has name, we set it to the existing display
            // device. (same with other atts)
            Optional.ofNullable(displayDeviceEntity.getName()).ifPresent(existingDisplayDevice::setName);
            Optional.ofNullable(displayDeviceEntity.getDisplayOrientation())
                    .ifPresent(existingDisplayDevice::setDisplayOrientation);
            Optional.ofNullable(displayDeviceEntity.getLocation()).ifPresent(existingDisplayDevice::setLocation);
            Optional.ofNullable(displayDeviceEntity.getResolution()).ifPresent(existingDisplayDevice::setResolution);
            Optional.ofNullable(displayDeviceEntity.getFallbackContent())
                    .ifPresent(existingDisplayDevice::setFallbackContent);
            Optional.ofNullable(displayDeviceEntity.getMonday_start()).ifPresent(existingDisplayDevice::setMonday_start);
            Optional.ofNullable(displayDeviceEntity.getMonday_end()).ifPresent(existingDisplayDevice::setMonday_end);
            Optional.ofNullable(displayDeviceEntity.getTuesday_start()).ifPresent(existingDisplayDevice::setTuesday_start);
            Optional.ofNullable(displayDeviceEntity.getTuesday_end()).ifPresent(existingDisplayDevice::setTuesday_end);
            Optional.ofNullable(displayDeviceEntity.getWednesday_start()).ifPresent(existingDisplayDevice::setWednesday_start);
            Optional.ofNullable(displayDeviceEntity.getWednesday_end()).ifPresent(existingDisplayDevice::setWednesday_end);
            Optional.ofNullable(displayDeviceEntity.getThursday_start()).ifPresent(existingDisplayDevice::setThursday_start);
            Optional.ofNullable(displayDeviceEntity.getThursday_end()).ifPresent(existingDisplayDevice::setThursday_end);
            Optional.ofNullable(displayDeviceEntity.getFriday_start()).ifPresent(existingDisplayDevice::setFriday_start);
            Optional.ofNullable(displayDeviceEntity.getFriday_end()).ifPresent(existingDisplayDevice::setFriday_end);
            Optional.ofNullable(displayDeviceEntity.getSaturday_start()).ifPresent(existingDisplayDevice::setSaturday_start);
            Optional.ofNullable(displayDeviceEntity.getSaturday_end()).ifPresent(existingDisplayDevice::setSaturday_end);
            Optional.ofNullable(displayDeviceEntity.getSunday_start()).ifPresent(existingDisplayDevice::setSunday_start);
            Optional.ofNullable(displayDeviceEntity.getSunday_end()).ifPresent(existingDisplayDevice::setSunday_end);

            DisplayDeviceEntity toReturn = displayDeviceRepository.save(existingDisplayDevice);
            pushTSService.updateDisplayDevicesToNewTimeSlots();
            return toReturn;
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }

    @Override
    public void delete(Long id) {

        DisplayDeviceEntity displayDevice = displayDeviceRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("DisplayDeviceEntity with id " + id + " not found"));

        displayDevice.getTimeSlots().clear();
        displayDevice.setFallbackContent(null);
        displayDeviceRepository.save(displayDevice);
        //TODO: Gør så den faktisk sletter samt sletter relations korrekt.
    }

    @Override
    public DisplayDeviceEntity setFallbackContent(Long id, Long fallbackId, String type) {
        return displayDeviceRepository.findById(Math.toIntExact(id)).map(existingDisplayDevice -> {

            if (type.equals("VisualMediaEntity")) {
                VisualMediaEntity foundFallback = visualMediaRepository.findById(Math.toIntExact(fallbackId))
                        .orElseThrow(() -> new RuntimeException("Visual Media does not exist"));
                existingDisplayDevice.setFallbackContent(foundFallback);
            } else if (type.equals("SlideshowEntity")) {
                SlideshowEntity foundFallback = slideshowRepository.findById(Math.toIntExact(fallbackId))
                        .orElseThrow(() -> new RuntimeException("Slideshow does not exist"));
                existingDisplayDevice.setFallbackContent(foundFallback);
            }

            return displayDeviceRepository.save(existingDisplayDevice);
        }).orElseThrow(() -> new RuntimeException("Display Device does not exist"));
    }

    @Override
    public DisplayDeviceEntity addTimeSlot(Long id, Long timeslotId) {
        return displayDeviceRepository.findById(Math.toIntExact(id)).map(existingDisplayDevice -> {
            TimeSlotEntity foundTimeslot = timeSlotService.findOne(timeslotId)
                    .orElseThrow(() -> new RuntimeException("Timeslot does not exist"));
            existingDisplayDevice.addTimeSlot(foundTimeslot);

            return displayDeviceRepository.save(existingDisplayDevice);
        }).orElseThrow(() -> new RuntimeException("Display Device does not exist"));
    }
}
