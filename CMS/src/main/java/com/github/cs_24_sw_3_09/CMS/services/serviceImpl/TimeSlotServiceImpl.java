package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;


import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.TimeSlotRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

    private TimeSlotRepository timeSlotRepository;
    private PushTSService pushTSService;
    private DisplayDeviceRepository displayDeviceRepository;
    private SlideshowRepository slideshowRepository;
    private VisualMediaRepository visualMediaRepository;

    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository, PushTSService pushTSService, DisplayDeviceRepository displayDeviceRepository,
                               SlideshowRepository slideshowRepository, VisualMediaRepository visualMediaRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.pushTSService = pushTSService;
        this.displayDeviceRepository = displayDeviceRepository;
        this.slideshowRepository = slideshowRepository;
        this.visualMediaRepository = visualMediaRepository;
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
        Optional<TimeSlotEntity> updatedTimeSlot = addDisplayDevice(timeSlotEntity);
        if (updatedTimeSlot.isEmpty()) return Optional.empty();
        timeSlotEntity = updatedTimeSlot.get();

        //Handle display content
        if (timeSlotEntity.getDisplayContent() != null) {
            updatedTimeSlot = addDisplayContent(timeSlotEntity);
            if (updatedTimeSlot.isEmpty()) return Optional.empty();
            timeSlotEntity = updatedTimeSlot.get();
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
        TimeSlotEntity timeSlotToDelete = timeSlotRepository.findById(Math.toIntExact(id)).get();

        Set<DisplayDeviceEntity> displayDevices = timeSlotToDelete.getDisplayDevices(); 
        for(DisplayDeviceEntity displayDevice : displayDevices) {
            displayDevice.getTimeSlots().remove(timeSlotToDelete);
        }

        timeSlotToDelete.setDisplayContent(null);
        timeSlotToDelete.getDisplayDevices().clear();
        timeSlotRepository.save(timeSlotToDelete);

        timeSlotRepository.delete(timeSlotToDelete);
    }

    /*private void deleteRelationWithTS(DisplayDeviceEntity displayDevice, Integer timeSlotId) {
        List<TimeSlotEntity> timeSlots = displayDevice.getTimeSlots();
        for(int i = 0; i < timeSlots.size(); i++) {
            if(timeSlots.get(i).getId() == timeSlotId) {
                timeSlots.remove(i);
                break; 
            }
        }
    }*/

    @Override
    public void deleteRelation(Long tsId, Long ddId) {
        //Checked that it already exists, therefore this will never throw an error 
        if (countDisplayDeviceAssociations(tsId) <= 1) 
            delete(tsId);
        else 
            deleteAssociation(tsId, ddId);
    }

    private void deleteAssociation(Long tsId, Long ddId) {
        TimeSlotEntity ts = timeSlotRepository.findById(Math.toIntExact(tsId)).get();
        DisplayDeviceEntity dd = displayDeviceRepository.findById(Math.toIntExact(ddId)).get();

        ts.getDisplayDevices().remove(dd);

        //TODO: Added this line
        dd.getTimeSlots().remove(ts);
        
        timeSlotRepository.save(ts);
    }

    @Override 
    public int countDisplayDeviceAssociations(Long timeSlotId) {
        return timeSlotRepository.findById(Math.toIntExact(timeSlotId)).get().getDisplayDevices().size();
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
