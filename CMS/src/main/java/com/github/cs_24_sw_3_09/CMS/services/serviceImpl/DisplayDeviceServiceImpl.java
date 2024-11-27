package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DisplayDeviceServiceImpl implements DisplayDeviceService {

    private final TimeSlotService timeSlotService;
    private DisplayDeviceRepository displayDeviceRepository;
    private VisualMediaRepository visualMediaRepository;
    private SlideshowRepository slideshowRepository;
    private PushTSService pushTSService;
    private final Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper;

    public DisplayDeviceServiceImpl(DisplayDeviceRepository displayDeviceRepository, VisualMediaRepository visualMediaRepository, SlideshowRepository slideshowRepository, TimeSlotService timeSlotService,
                                   PushTSService pushTSService, Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper) {
        this.displayDeviceRepository = displayDeviceRepository;
        this.visualMediaRepository = visualMediaRepository;
        this.slideshowRepository = slideshowRepository;
        this.pushTSService = pushTSService;
        this.timeSlotService = timeSlotService;
        this.displayDeviceMapper = displayDeviceMapper;
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
    public DisplayDeviceEntity save(DisplayDeviceEntity displayDevice) {
        DisplayDeviceEntity toReturn = displayDeviceRepository.save(displayDevice);
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        return toReturn;
    }

    @Override
    public boolean isExists(Long id) {
        return displayDeviceRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public Set<DisplayDeviceDto> findDisplayDevicesWhoUsesSlideshowAsFallback(Long id){
        Set<DisplayDeviceEntity> setOfDisplayDeviceEntities = displayDeviceRepository.findDisplayDevicesUsingSlideshowAsFallbackBySlideshowId(id);
        if (setOfDisplayDeviceEntities == null) {
            return Collections.emptySet();
        }
        Set <DisplayDeviceDto> setOfDisplayDeviceDtos = new HashSet<>();
        for (DisplayDeviceEntity entity : setOfDisplayDeviceEntities) {
            DisplayDeviceDto displayDeviceDto = displayDeviceMapper.mapTo(entity);
            setOfDisplayDeviceDtos.add(displayDeviceDto);
        }  
        return setOfDisplayDeviceDtos;      
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
            Optional.ofNullable(displayDeviceEntity.getConnectedState())
                    .ifPresent(existingDisplayDevice::setConnectedState);
            Optional.ofNullable(displayDeviceEntity.getLocation()).ifPresent(existingDisplayDevice::setLocation);
            Optional.ofNullable(displayDeviceEntity.getResolution()).ifPresent(existingDisplayDevice::setResolution);
            Optional.ofNullable(displayDeviceEntity.getFallbackContent())
                    .ifPresent(existingDisplayDevice::setFallbackContent);

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
