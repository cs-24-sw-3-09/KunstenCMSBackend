package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.SlideshowDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.TimeSlotRepository;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SlideshowServiceImpl implements SlideshowService {

    private final VisualMediaInclusionService visualMediaInclusionService;
    private SlideshowRepository slideshowRepository;
    private PushTSService pushTSService;
    private TimeSlotRepository timeSlotRepository;
    private Mapper<SlideshowEntity, SlideshowDto> slideshowMapper;

    public SlideshowServiceImpl(SlideshowRepository slideshowRepository,
            VisualMediaInclusionService visualMediaInclusionService, PushTSService pushTSService, TimeSlotRepository timeSlotRepository, Mapper<SlideshowEntity, SlideshowDto> slideshowMapper) {
        this.slideshowRepository = slideshowRepository;
        this.pushTSService = pushTSService;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.timeSlotRepository = timeSlotRepository;
        this.slideshowMapper = slideshowMapper;
    }

    @Override
    @Transactional
    public SlideshowEntity save(SlideshowEntity slideshowEntity) {
        SlideshowEntity toReturn = slideshowRepository.save(slideshowEntity);
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        return toReturn;
    }

    @Override
    public List<SlideshowEntity> findAll() {
        return StreamSupport.stream(slideshowRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Iterable<SlideshowEntity> findAll(Pageable pageable) {
        return slideshowRepository.findAll();
    }

    @Override
    public Optional<SlideshowEntity> findOne(Long id) {
        return slideshowRepository.findById(Math.toIntExact(id));
    }

    @Override
    public boolean isExists(Long id) {
        return slideshowRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public Set<SlideshowDto> findPartOfSlideshows(Long id){    
        Set<SlideshowEntity> setOfSlideshowEntities = slideshowRepository.findSlideshowsByVisualMediaId(id);

        if (setOfSlideshowEntities == null) {
            return Collections.emptySet();
        }

        Set<SlideshowDto> setOfSlideshowDtos = new HashSet<>();

        for (SlideshowEntity entity : setOfSlideshowEntities) {
            SlideshowDto slideshowDto = slideshowMapper.mapTo(entity);
            setOfSlideshowDtos.add(slideshowDto);
        }        
        return setOfSlideshowDtos;   
    }

    @Override
    public SlideshowEntity partialUpdate(Long id, SlideshowEntity slideshowEntity)
            throws RuntimeException {
        return slideshowRepository.findById(Math.toIntExact(id)).map(existingSlideshow -> {
            // if display device from request has name, we set it to the existing display
            // device. (same with other atts)
            Optional.ofNullable(slideshowEntity.getName()).ifPresent(existingSlideshow::setName);
            Optional.ofNullable(slideshowEntity.getIsArchived()).ifPresent(existingSlideshow::setIsArchived);
            Optional.ofNullable(slideshowEntity.getVisualMediaInclusionCollection())
                    .ifPresent(existingSlideshow::setVisualMediaInclusionCollection);

            SlideshowEntity toReturn = slideshowRepository.save(existingSlideshow);
            pushTSService.updateDisplayDevicesToNewTimeSlots();
            return toReturn;

        }).orElseThrow(() -> new RuntimeException("Slideshow does not exist"));
    }

    @Override
    public void delete(Long id) {
        SlideshowEntity slideshow = slideshowRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new EntityNotFoundException("Slideshow with id " + id + " not found"));

        //slideshow.getVisualMediaInclusionCollection().clear();
        //slideshowRepository.save(slideshow);
        slideshowRepository.deleteById(Math.toIntExact(id));
        pushTSService.updateDisplayDevicesToNewTimeSlots();
    }

    @Override
    public SlideshowEntity addVisualMediaInclusion(Long id, Long visualMediaInclusionId) {
        return slideshowRepository.findById(Math.toIntExact(id)).map(existingDisplayDevice -> {
            VisualMediaInclusionEntity foundVisualMediaInclusionEntity = visualMediaInclusionService
                    .findOne(visualMediaInclusionId)
                    .orElseThrow(() -> new RuntimeException("Visual media inclusion does not exist"));
            existingDisplayDevice.addVisualMediaInclusion(foundVisualMediaInclusionEntity);

            return slideshowRepository.save(existingDisplayDevice);
        }).orElseThrow(() -> new RuntimeException("Slideshow does not exist"));
    }
    
    @Override
    public List<Map<String, Object>> findStateOfEverySlideshow() {
        List<Integer> allSlideshowIds = slideshowRepository.getAllSlideshowIds();
        List<TimeSlotEntity> allTimeSlotsWithSlideshowAsContent = timeSlotRepository.getAllTimeSlotsWithSlideshowAsContent();
        
        List<Integer> displayContentIds = new ArrayList<>();
        for (TimeSlotEntity ts : allTimeSlotsWithSlideshowAsContent) {
            displayContentIds.add(ts.getDisplayContent().getId());
        }   
    
        Set<Integer> timeSlotsCurrentlyShown = pushTSService.updateDisplayDevicesToNewTimeSlots(false);
        List<TimeSlotEntity> activeTimeSlots = new ArrayList<>();
        List<TimeSlotEntity> futureTimeSlots = new ArrayList<>();
        
        //only get Time Slots that are currently shown or in the future
        for (TimeSlotEntity ts : allTimeSlotsWithSlideshowAsContent) {
            if (timeSlotsCurrentlyShown.contains(ts.getId())) {
                activeTimeSlots.add(ts);
            } else if (ts.getStartDate().after(new Date()) ||
                    (ts.getStartDate().equals(new Date())
                            && ts.getStartTime().after(new Time((new Date()).getTime())))) {
                // If the start date is in the future OR if the start date is today and the start time is in the future
                futureTimeSlots.add(ts);
            }
        }
    
        List<Map<String, Object>> slideshowStatusList = new ArrayList<>();
        for (Integer slideshowId : allSlideshowIds) {
            Map<String, Object> slideshowStatus = new HashMap<>();
            slideshowStatus.put("slideshowId", slideshowId);
            slideshowStatus.put("color", "red");
    
            for (TimeSlotEntity ts : activeTimeSlots) {
                for (Integer contentId : displayContentIds) {
                    if (ts.getDisplayContent().getId().equals(contentId) && contentId.equals(slideshowId)) {
                        slideshowStatus.put("color", "green"); 

                        // Convert display devices to a list of maps
                        List<Map<String, Object>> displayDevices = new ArrayList<>();
                        if (slideshowStatus.containsKey("displayDevices")){
                            Object devices = slideshowStatus.get("displayDevices");
                            if (devices instanceof List) {
                                displayDevices = (List<Map<String, Object>>) devices;
                            }
                        }

                        for (DisplayDeviceEntity obj : ts.getDisplayDevices()) {
                            Map<String, Object> displayDeviceMap = new HashMap<>();
                            displayDeviceMap.put("id", obj.getId());
                            displayDeviceMap.put("name", obj.getName());
                            displayDevices.add(displayDeviceMap);
                        }
                        slideshowStatus.put("displayDevices", displayDevices);
                        break;
                    }
                } 
            }
            if ("red".equals(slideshowStatus.get("color"))) {
                for (TimeSlotEntity ts : futureTimeSlots) {
                    for (Integer contentId : displayContentIds) {
                        if (ts.getDisplayContent().getId().equals(contentId) && contentId.equals(slideshowId)) {
                            slideshowStatus.put("color", "yellow"); 
                            break;
                        }
                    }
                }
            }
            if (!"green".equals(slideshowStatus.get("color"))) {
                slideshowStatus.remove("displayDevices");
            }
            slideshowStatusList.add(slideshowStatus);
        }
        return slideshowStatusList;
    }

    @Override
    public Optional<SlideshowEntity> duplicate(Long id, String name) {
        Optional<SlideshowEntity> checkExistence = findOne(id);
        if(checkExistence.isEmpty()) return Optional.empty();
        SlideshowEntity slideshowToDuplicate = checkExistence.get(); 

        Set<VisualMediaInclusionEntity> visualMediaInclusionEntities = new HashSet<>();
        if (slideshowToDuplicate.getVisualMediaInclusionCollection() != null) {
            for (VisualMediaInclusionEntity vmi : slideshowToDuplicate.getVisualMediaInclusionCollection()) {
                visualMediaInclusionEntities.add(detachVisualMediaInclusion(vmi)); 
            }
        }

        SlideshowEntity newSlideshow = SlideshowEntity
        .builder().name(name != null ? name : slideshowToDuplicate.getName() + " (Copy)")
        .isArchived(slideshowToDuplicate.getIsArchived())
        .visualMediaInclusionCollection(visualMediaInclusionEntities).build();
        
        newSlideshow = slideshowRepository.save(newSlideshow);

        return Optional.of(newSlideshow);
    }

    private VisualMediaInclusionEntity detachVisualMediaInclusion(VisualMediaInclusionEntity visualMediaInclusion) {
        VisualMediaInclusionEntity visualMediaInclusionToSave = VisualMediaInclusionEntity.builder()
        .slideDuration(visualMediaInclusion.getSlideDuration())
        .slideshowPosition(visualMediaInclusion.getSlideshowPosition())
        .visualMedia(visualMediaInclusion.getVisualMedia())
        .build();
        
        VisualMediaInclusionEntity visualMediaInclusionToReturn = visualMediaInclusionService.save(visualMediaInclusionToSave).get();

        return visualMediaInclusionToReturn;
    }
    
}
