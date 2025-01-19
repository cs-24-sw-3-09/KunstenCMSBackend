package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.*;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
import com.github.cs_24_sw_3_09.CMS.utils.ContentUtils;
import com.github.cs_24_sw_3_09.CMS.utils.Result;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private ContentUtils contentUtils;
    private DimensionCheckService dimensionCheckService;

    public DisplayDeviceServiceImpl(DisplayDeviceRepository displayDeviceRepository, VisualMediaRepository visualMediaRepository, 
                                    SlideshowRepository slideshowRepository, TimeSlotService timeSlotService,
                                    PushTSService pushTSService, Mapper<DisplayDeviceEntity, DisplayDeviceDto> displayDeviceMapper,
                                    ContentUtils contentUtils, DimensionCheckService dimensionCheckService) {
        this.displayDeviceRepository = displayDeviceRepository;
        this.visualMediaRepository = visualMediaRepository;
        this.slideshowRepository = slideshowRepository;
        this.pushTSService = pushTSService;
        this.timeSlotService = timeSlotService;
        this.displayDeviceMapper = displayDeviceMapper;
        this.contentUtils = contentUtils;
        this.dimensionCheckService = dimensionCheckService;
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
    public Result<DisplayDeviceEntity, String> save(DisplayDeviceEntity displayDeviceEntity, Boolean forceDimensions) {
        
        //check whether the dimensions of the displayDevice and the fallbackContent fit
        if (displayDeviceEntity.getFallbackContent() != null) {

            //Assign fallback content to display device
            Optional<DisplayDeviceEntity> displayDevice = addFallbackContent(displayDeviceEntity);
            if (displayDevice.isEmpty()) return Result.err("Not found");
            displayDeviceEntity = displayDevice.get();

             //check whether the dimensions of the displayDevice and the fallbackContent fit
            if(!forceDimensions){
                String checkResult = dimensionCheckService.checkDimensionForAssignedFallback(displayDeviceEntity, displayDeviceEntity.getFallbackContent());
                if (!"1".equals(checkResult)) return Result.err(checkResult);
            }
        }

        DisplayDeviceEntity toReturn = displayDeviceRepository.save(displayDeviceEntity);
        pushTSService.updateDisplayDevicesToNewTimeSlots();

        return Result.ok(toReturn);
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
            Optional.ofNullable(displayDeviceEntity.getLocation()).ifPresent(existingDisplayDevice::setLocation);
            Optional.ofNullable(displayDeviceEntity.getResolution()).ifPresent(existingDisplayDevice::setResolution);

            //Fallback content
            Optional.ofNullable(displayDeviceEntity.getFallbackContent()).ifPresent(fallback -> {
                if (fallback.getId() == 0) {
                    existingDisplayDevice.setFallbackContent(null);
                    return;
                }
                Optional<ContentEntity> content = findContentById(fallback.getId());
                if (content.isEmpty()) return;
                existingDisplayDevice.setFallbackContent(content.get());
            });

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
        DisplayDeviceEntity displayDevice = displayDeviceRepository.findById(Math.toIntExact(id)).orElse(null);

        //All Time Slots that should be deleted (has zero associations) are saved in this array and cleans up after display device is deleted.
        //Happens because of the need to delete the time Slot which conflicts with the persistence context somehow. 
        List<TimeSlotEntity> TsToDelete = new ArrayList<>(); 
        for (TimeSlotEntity timeSlot : displayDevice.getTimeSlots()) {
            //Remove Relation between Display Device and Time Slot
            timeSlot.getDisplayDevices().remove(displayDevice);
            if (timeSlot.countDisplayDeviceAssociations() == 0) {
                TsToDelete.add(timeSlot);
            }
            
        }
        
        displayDevice.getTimeSlots().clear();
        displayDevice.setFallbackContent(null);
        displayDeviceRepository.save(displayDevice);

        displayDeviceRepository.delete(displayDevice);


        for(TimeSlotEntity ts : TsToDelete) {
            timeSlotService.delete((long) ts.getId());
        }
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

    @Override
    public Result<DisplayDeviceEntity, String> addFallback(Long id, Long fallbackId, Boolean forceDimensions) {
        Optional<ContentEntity> content = findContentById(Math.toIntExact(fallbackId));
        Optional<DisplayDeviceEntity> displayDeviceToCheck = displayDeviceRepository.findById(Math.toIntExact(id)); 
        
        if (displayDeviceToCheck.isEmpty() || content.isEmpty()) {
            return Result.err("Not found");
        }
        
        ContentEntity fallbackContent = content.get();
        DisplayDeviceEntity displayDevice = displayDeviceToCheck.get();

         //check whether the dimensions of the displayDevice and the fallbackContent fit 
        if (!forceDimensions){
            String checkResult = dimensionCheckService.checkDimensionForAssignedFallback(displayDevice, fallbackContent);
            if(!"1".equals(checkResult)) return Result.err(checkResult); 
        }
       
        displayDevice.setFallbackContent(fallbackContent);
        DisplayDeviceEntity displayToReturn = displayDeviceRepository.save(displayDevice);

        return Result.ok(displayToReturn);   
    }
}
