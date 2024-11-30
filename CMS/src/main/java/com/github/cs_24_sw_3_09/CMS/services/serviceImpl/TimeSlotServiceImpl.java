package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;


import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.TimeSlotRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

    private TimeSlotRepository timeSlotRepository;
    private PushTSService pushTSService;
    private DisplayDeviceRepository displayDeviceRepository;
    private SlideshowRepository slideshowRepository;
    private VisualMediaRepository visualMediaRepository;
    private VisualMediaService visualMediaService;
    private SlideshowService slideshowService;

    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository, PushTSService pushTSService, DisplayDeviceRepository displayDeviceRepository,
                               SlideshowRepository slideshowRepository, VisualMediaRepository visualMediaRepository, VisualMediaService visualMediaService, SlideshowService, slideshowService) {
        this.timeSlotRepository = timeSlotRepository;
        this.pushTSService = pushTSService;
        this.displayDeviceRepository = displayDeviceRepository;
        this.slideshowRepository = slideshowRepository;
        this.visualMediaRepository = visualMediaRepository;
        this.visualMediaService = visualMediaService;
        this.slideshowService = slideshowService;
    }

    @Override
    public TimeSlotEntity saveWithOnlyId(TimeSlotEntity timeSlotEntity) {
        Set<DisplayDeviceEntity> displayDevices = timeSlotEntity.getDisplayDevices();

        Set<DisplayDeviceEntity> newDisplayDevices = new HashSet<>();
        for (DisplayDeviceEntity displayDevice : displayDevices) {
            DisplayDeviceEntity newDisplayDevice = displayDeviceRepository.findById(displayDevice.getId()).get();
            newDisplayDevices.add(newDisplayDevice);
        }
        timeSlotEntity.getDisplayDevices().clear();
        timeSlotEntity.setDisplayDevices(newDisplayDevices);

        TimeSlotEntity newTs = timeSlotRepository.save(timeSlotEntity);

        pushTSService.updateDisplayDevicesToNewTimeSlots();

        return newTs;
    }

    @Override
    public Optional<TimeSlotEntity> save(TimeSlotEntity timeSlotEntity) {
        //Handle display devices
        Optional<TimeSlotEntity> displayDevice = addDisplayDevice(timeSlotEntity);
        if (displayDevice.isEmpty()) return Optional.empty();
        timeSlotEntity = displayDevice.get();

        //Handle display content
        if (timeSlotEntity.getDisplayContent() != null) {
            Optional<TimeSlotEntity> displayContent = addDisplayContent(timeSlotEntity);
            if (displayContent.isEmpty()) return Optional.empty();
            timeSlotEntity = displayContent.get();
        }

        TimeSlotEntity toReturn = timeSlotRepository.save(timeSlotEntity);
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        return Optional.of(toReturn);
    }

    private Optional<TimeSlotEntity> addDisplayDevice(TimeSlotEntity timeSlotEntity) {
        Set<DisplayDeviceEntity> displayDevices = timeSlotEntity.getDisplayDevices();

        Set<DisplayDeviceEntity> newDisplayDevices = new HashSet<>();
        for (DisplayDeviceEntity displayDevice : displayDevices) {
            DisplayDeviceEntity newDisplayDevice = displayDevice.getId() == null ? displayDevice : displayDeviceRepository.findById(displayDevice.getId()).orElse(null);

            if (newDisplayDevice == null) return Optional.empty();

            newDisplayDevices.add(newDisplayDevice);
        }
        timeSlotEntity.getDisplayDevices().clear();
        timeSlotEntity.setDisplayDevices(newDisplayDevices);
        return Optional.of(timeSlotEntity);
    }

    private Optional<TimeSlotEntity> addDisplayContent(TimeSlotEntity timeSlotEntity) {
        Integer currentContentId = timeSlotEntity.getDisplayContent().getId();
        Optional<ContentEntity> optionalContent = currentContentId == null ? Optional.of(timeSlotEntity.getDisplayContent()) : findContentById(currentContentId);
        if (optionalContent.isEmpty()) return Optional.empty();
        timeSlotEntity.setDisplayContent(optionalContent.get());
        return Optional.of(timeSlotEntity);
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
    public Optional<TimeSlotEntity> findOne(Long id) {
        return timeSlotRepository.findById(Math.toIntExact(id));
    }

    @Override
    public List<TimeSlotEntity> findAll() {
        // return itterable, so we convert it to list.
        return StreamSupport.stream(timeSlotRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Page<TimeSlotEntity> findAll(Pageable pageable) {
        return timeSlotRepository.findAll(pageable);
    }

    @Override
    public boolean isExists(Long id) {
        return timeSlotRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public TimeSlotEntity partialUpdate(Long id, TimeSlotEntity timeSlotEntity) {
        timeSlotEntity.setId(Math.toIntExact(id));
        return timeSlotRepository.findById(Math.toIntExact(id)).map(existingTimeSlot -> {
            // if time slot from request has name, we set it to the existing time slot.
            // (same with other atts)

            Optional.ofNullable(timeSlotEntity.getName()).ifPresent(existingTimeSlot::setName);
            Optional.ofNullable(timeSlotEntity.getStartDate()).ifPresent(existingTimeSlot::setStartDate);
            Optional.ofNullable(timeSlotEntity.getEndDate()).ifPresent(existingTimeSlot::setEndDate);
            Optional.ofNullable(timeSlotEntity.getStartTime()).ifPresent(existingTimeSlot::setStartTime);
            Optional.ofNullable(timeSlotEntity.getEndTime()).ifPresent(existingTimeSlot::setEndTime);
            Optional.ofNullable(timeSlotEntity.getWeekdaysChosen()).ifPresent(existingTimeSlot::setWeekdaysChosen);
            Optional.ofNullable(timeSlotEntity.getDisplayContent()).ifPresent(existingTimeSlot::setDisplayContent);

            TimeSlotEntity toReturn = timeSlotRepository.save(existingTimeSlot);
            pushTSService.updateDisplayDevicesToNewTimeSlots();
            return toReturn;
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }

    @Override
    public void delete(Long id) {
        timeSlotRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public void deleteRelation(Long tsId, Long ddId) {
        int associations = timeSlotRepository.countAssociations(tsId);

        if (associations == 1) {
            delete(tsId);
        } else {
            timeSlotRepository.deleteAssociation(tsId, ddId);
        }
    }

    @Override
    public TimeSlotEntity setDisplayContent(Long tsId, Long dcId, String dcType) {

        return timeSlotRepository.findById(Math.toIntExact(tsId)).map(existingTimeSlot -> {


            ContentEntity foundDisplayContent = null;
            if (dcType.equals("visualMedia")) {
                foundDisplayContent = visualMediaService.findOne(dcId)
                        .orElseThrow(() -> new RuntimeException("Visual Media does not exist"));
            } else if (dcType.equals("slideshow")) {
                foundDisplayContent = slideshowService.findOne(dcId)
                        .orElseThrow(() -> new RuntimeException("Slideshow does not exist"));
            }

            existingTimeSlot.setDisplayContent(foundDisplayContent);

            return timeSlotRepository.save(existingTimeSlot);
         }).orElseThrow(() -> new RuntimeException("Time Slot does not exist"));
    } 
    
    @Override
    public TimeSlotEntity addDisplayDevice(Long id, Long displayDeviceId) throws RuntimeException {
        return timeSlotRepository.findById(Math.toIntExact(id)).map(existingTimeSlot -> {
            DisplayDeviceEntity foundDisplayDevice = displayDeviceRepository.findById(Math.toIntExact(displayDeviceId)).orElseThrow();
            existingTimeSlot.addDisplayDevice(foundDisplayDevice);
            existingTimeSlot.setId(Math.toIntExact(id));
            TimeSlotEntity updatedTimeslot = timeSlotRepository.save(existingTimeSlot);
            return timeSlotRepository.save(updatedTimeslot);
        }).orElseThrow();
    }
}
