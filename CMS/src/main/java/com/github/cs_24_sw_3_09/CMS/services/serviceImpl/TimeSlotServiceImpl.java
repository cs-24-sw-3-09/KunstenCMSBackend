package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.TimeSlotRepository;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

    private TimeSlotRepository timeSlotRepository;
    private PushTSService pushTSService;

    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository, PushTSService pushTSService) {
        this.timeSlotRepository = timeSlotRepository;
        this.pushTSService = pushTSService;
    }

    @Override
    public TimeSlotEntity save(TimeSlotEntity timeSlotEntity) {
        TimeSlotEntity toReturn = timeSlotRepository.save(timeSlotEntity);
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        return toReturn;
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
        TimeSlotEntity timeslot = timeSlotRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("Timeslot with id " + id + " not found"));
        timeslot.setDisplayContent(null);
        timeslot.getDisplayDevices().clear();
        timeSlotRepository.save(timeslot);
        timeSlotRepository.deleteById(Math.toIntExact(id));
        pushTSService.updateDisplayDevicesToNewTimeSlots();
    }
}
