package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        timeSlotRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public void deleteRelation(Long tsId, Long ddId) {
        //todo: Overvej om der skal gøres brug af den periodiske sletning i stedet
        int associations = timeSlotRepository.countAssociations(tsId);

        if (associations == 1) {
            delete(tsId);
        } else {
            timeSlotRepository.deleteAssociation(tsId, ddId);
        }
    }
}
