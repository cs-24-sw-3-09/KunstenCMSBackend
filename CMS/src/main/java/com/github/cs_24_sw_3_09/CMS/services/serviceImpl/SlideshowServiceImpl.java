package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaInclusionRepository;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.PushTSService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.utils.Result;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SlideshowServiceImpl implements SlideshowService {

    private VisualMediaInclusionRepository visualMediaInclusionRepository;

    private SlideshowRepository slideshowRepository;
    private PushTSService pushTSService;
    private TimeSlotRepository timeSlotRepository;
    private Mapper<SlideshowEntity, SlideshowDto> slideshowMapper;
    private DimensionCheckService dimensionCheckService;

    public SlideshowServiceImpl(SlideshowRepository slideshowRepository,
    VisualMediaInclusionRepository visualMediaInclusionRepository, PushTSService pushTSService, TimeSlotRepository timeSlotRepository, 
            Mapper<SlideshowEntity, SlideshowDto> slideshowMapper, DimensionCheckService dimensionCheckService) {
        this.slideshowRepository = slideshowRepository;
        this.pushTSService = pushTSService;
        this.visualMediaInclusionRepository = visualMediaInclusionRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.slideshowMapper = slideshowMapper;
        this.dimensionCheckService = dimensionCheckService;
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
        slideshowRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new EntityNotFoundException("Slideshow with id " + id + " not found"));

        Set<TimeSlotEntity> timeSlots = timeSlotRepository.findSetOfTimeSlotsBySlideshowId(id);
        if(timeSlots.size() > 0){
            for (TimeSlotEntity ts : timeSlots){
                ts.setDisplayContent(null);
            }
        }
        slideshowRepository.deleteById(Math.toIntExact(id));
        pushTSService.updateDisplayDevicesToNewTimeSlots();
    }

    @Override
    public Result<SlideshowEntity, String> addVisualMediaInclusion(Long id, Long visualMediaInclusionId, Boolean forceDimensions) {
         // validate existence of slidehsow and visualMediaInclusion
        Optional<SlideshowEntity> slideshowToCheck = findOne(id);
        Optional<VisualMediaInclusionEntity> visualMediaInclusionToCheck = visualMediaInclusionRepository.findById(visualMediaInclusionId.intValue());


        if (slideshowToCheck.isEmpty() || visualMediaInclusionToCheck.isEmpty()) {
            return Result.err("Not found");
        }

        //check whether the dimensions of the slideshow and new visualMediaInclusion fit
        if(!forceDimensions){
            String checkResult = dimensionCheckService.checkDimensionForAssignedVisualMediaToSlideshow(
            visualMediaInclusionId, id);
            if (!"1".equals(checkResult)) {
                return Result.err(checkResult);
            }
        }

        //todo: Seems kinda silly, because Slideshow and vmi have already been checked
        return Result.ok(slideshowRepository.findById(Math.toIntExact(id)).map(existingDisplayDevice -> {
            VisualMediaInclusionEntity foundVisualMediaInclusionEntity = visualMediaInclusionRepository
                    .findById(visualMediaInclusionId.intValue())
                    .orElseThrow(() -> new RuntimeException("Visual media inclusion does not exist"));
            existingDisplayDevice.addVisualMediaInclusion(foundVisualMediaInclusionEntity);

            return slideshowRepository.save(existingDisplayDevice);
        }).orElseThrow(() -> new RuntimeException("Slideshow does not exist")));
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
        
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        //only get Time Slots that are currently shown or in the future
        for (TimeSlotEntity ts : allTimeSlotsWithSlideshowAsContent) {

            LocalDate startDate = ts.getStartDate().toLocalDate();
            LocalTime startTime = ts.getStartTime().toLocalTime();
            LocalDate endDate = ts.getEndDate().toLocalDate();
                     
            boolean isTodayWithinRange = startDate.isBefore(today) && endDate.isAfter(today);
            boolean isValidToday = today.equals(startDate) && startTime.isAfter(now);

            if (timeSlotsCurrentlyShown.contains(ts.getId())) {
                activeTimeSlots.add(ts);
            } else if(isValidToday || today.isBefore(startDate) || isTodayWithinRange){
                futureTimeSlots.add(ts);
            }        
        }

        List<Map<String, Object>> slideshowStatusList = new ArrayList<>();
        for (Integer slideshowId : allSlideshowIds) {
            Map<String, Object> slideshowStatus = new HashMap<>();
            slideshowStatus.put("slideshowId", slideshowId);
            slideshowStatus.put("color", "red");

            for (TimeSlotEntity ts : activeTimeSlots) {
                if (ts.getDisplayContent().getId().equals(slideshowId)) {
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

            if (slideshowStatus.get("color").equals("red")) {
                for (TimeSlotEntity ts : futureTimeSlots) {
                    // Check for the time slot happen
                    LocalDate tsDate = ts.getEndDate().toLocalDate();
                    if (tsDate.isBefore(today)) {
                        continue;
                    }
                    if (!ts.getDisplayContent().getId().equals(slideshowId)) {
                        continue;
                    }
                    slideshowStatus.put("color", "yellow");
                    break;
                }
            }

            if (!slideshowStatus.get("color").equals("green")) {
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
        
        VisualMediaInclusionEntity visualMediaInclusionToReturn = visualMediaInclusionRepository.save(visualMediaInclusionToSave);

        return visualMediaInclusionToReturn;
    }
    
}
