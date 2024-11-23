package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
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

    public DisplayDeviceServiceImpl(DisplayDeviceRepository displayDeviceRepository, VisualMediaRepository visualMediaRepository, SlideshowRepository slideshowRepository, TimeSlotService timeSlotService) {
        this.displayDeviceRepository = displayDeviceRepository;
        this.visualMediaRepository = visualMediaRepository;
        this.slideshowRepository = slideshowRepository;
        this.timeSlotService = timeSlotService;
    }

    @Override
    public Optional<DisplayDeviceEntity> findOne(Long id) {
        return displayDeviceRepository.findById(Math.toIntExact(id));
    }

    @Override
    public List<DisplayDeviceEntity> findAll() {
        //return itterable, so we convert it to list.
        return StreamSupport.stream(displayDeviceRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Page<DisplayDeviceEntity> findAll(Pageable pageable) {
        return displayDeviceRepository.findAll(pageable);
    }


    @Override
    public DisplayDeviceEntity save(DisplayDeviceEntity displayDevice) {
        return displayDeviceRepository.save(displayDevice);
    }

    @Override
    public boolean isExists(Long id) {
        return displayDeviceRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public DisplayDeviceEntity partialUpdate(Long id, DisplayDeviceEntity displayDeviceEntity) {
        displayDeviceEntity.setId(Math.toIntExact(id));
        return displayDeviceRepository.findById(Math.toIntExact(id)).map(existingDisplayDevice -> {
            // if display device from request has name, we set it to the existing display device. (same with other atts)
            Optional.ofNullable(displayDeviceEntity.getName()).ifPresent(existingDisplayDevice::setName);
            Optional.ofNullable(displayDeviceEntity.getDisplayOrientation()).ifPresent(existingDisplayDevice::setDisplayOrientation);
            Optional.ofNullable(displayDeviceEntity.getConnectedState()).ifPresent(existingDisplayDevice::setConnectedState);
            Optional.ofNullable(displayDeviceEntity.getLocation()).ifPresent(existingDisplayDevice::setLocation);
            Optional.ofNullable(displayDeviceEntity.getModel()).ifPresent(existingDisplayDevice::setModel);
            Optional.ofNullable(displayDeviceEntity.getResolution()).ifPresent(existingDisplayDevice::setResolution);
            Optional.ofNullable(displayDeviceEntity.getFallbackContent()).ifPresent(existingDisplayDevice::setFallbackContent);
            return displayDeviceRepository.save(existingDisplayDevice);
        }).orElseThrow(() -> new RuntimeException("Author does not exist"));
    }

    @Override
    public void delete(Long id) {

        displayDeviceRepository.deleteById(Math.toIntExact(id));
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
