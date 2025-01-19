package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;


import java.sql.Date;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotColor;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;
import com.github.cs_24_sw_3_09.CMS.utils.Result;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.mappers.Mapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.DisplayDeviceRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.TimeSlotRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaRepository;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
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
    private final Mapper<TimeSlotEntity, TimeSlotDto> timeSlotMapper;
    private DimensionCheckService dimensionCheckService;
    
    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository, PushTSService pushTSService, 
                            Mapper<TimeSlotEntity, TimeSlotDto>  timeSlotMapper, DisplayDeviceRepository displayDeviceRepository,
                            SlideshowRepository slideshowRepository, VisualMediaRepository visualMediaRepository, 
                            VisualMediaService visualMediaService, SlideshowService slideshowService,
                            DimensionCheckService dimensionCheckService) {
        this.timeSlotRepository = timeSlotRepository;
        this.pushTSService = pushTSService;
        this.displayDeviceRepository = displayDeviceRepository;
        this.slideshowRepository = slideshowRepository;
        this.visualMediaRepository = visualMediaRepository;
        this.visualMediaService = visualMediaService;
        this.slideshowService = slideshowService;
        this.timeSlotMapper = timeSlotMapper;
        this.dimensionCheckService = dimensionCheckService;
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
    public Result<TimeSlotEntity, String> save(TimeSlotEntity timeSlotEntity, Boolean forceDimensions){
        //Handle display devices
        Optional<TimeSlotEntity> updatedTimeSlot = addDisplayDevice(timeSlotEntity);
        if (updatedTimeSlot.isEmpty()) return Result.err("Not found");
        timeSlotEntity = updatedTimeSlot.get();

        //Handle display content
        if (timeSlotEntity.getDisplayContent() != null) {
            updatedTimeSlot = addDisplayContent(timeSlotEntity);
            if (updatedTimeSlot.isEmpty()) return Result.err("Not found");
            timeSlotEntity = updatedTimeSlot.get();
        }

        //check if dimensions of displaydevice and content fit
        if (!forceDimensions && timeSlotEntity.getDisplayDevices() != null && timeSlotEntity.getDisplayContent() != null) {
            String checkResult = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(
                    timeSlotEntity.getDisplayContent(), timeSlotEntity.getDisplayDevices()
            );
            if (!"1".equals(checkResult)) {
                return Result.err(checkResult);
            }
        }

        TimeSlotEntity toReturn = timeSlotRepository.save(timeSlotEntity);

        //If time slot is active then it notifies display devices
        pushTSService.updateDisplayDevicesToNewTimeSlots();
        return Result.ok(toReturn);
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


    private Optional<Set<DisplayDeviceEntity>> getDisplayDevices(TimeSlotEntity timeSlotEntity) {
        Set<DisplayDeviceEntity> displayDevices = timeSlotEntity.getDisplayDevices();
        if (displayDevices == null) return Optional.empty();

        Set<DisplayDeviceEntity> newDisplayDevices = new HashSet<>();
        for (DisplayDeviceEntity displayDevice : displayDevices) {
            DisplayDeviceEntity newDisplayDevice = displayDevice.getId() == null ? displayDevice : displayDeviceRepository.findById(displayDevice.getId()).orElse(null);
            if (newDisplayDevice == null) continue;
            newDisplayDevices.add(newDisplayDevice);
        }
        return Optional.of(newDisplayDevices);
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
    public List<TimeSlotEntity> findAll(Date start, Date end) {
        return timeSlotRepository.findAllInTimeFrame(start, end);
    }

    @Override
    public boolean isExists(Long id) {
        return timeSlotRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public Set<TimeSlotDto> findSetOfTimeSlotsSlideshowIsAPartOf(Long id) {
        Set<TimeSlotEntity> setOfTimeSlotEntities = timeSlotRepository.findSetOfTimeSlotsBySlideshowId(id);
        if (setOfTimeSlotEntities == null) {
            return Collections.emptySet();
        }
        Set<TimeSlotDto> setOfTimeSlotDtos = new HashSet<>();
        for (TimeSlotEntity entity : setOfTimeSlotEntities) {
            TimeSlotDto timeSlotDto = timeSlotMapper.mapTo(entity);
            setOfTimeSlotDtos.add(timeSlotDto);
        }
        return setOfTimeSlotDtos;
    }

    @Override
    public Result<TimeSlotEntity, String> partialUpdate(Long id, TimeSlotEntity timeSlotEntity, Boolean forceDimensions) {
        //Error handling
        if (!isExists(id)) {
            return Result.err("Not found");
        }

        
        //check if dimensions of displaydevice and content fit
        if (!forceDimensions && timeSlotEntity.getDisplayContent() != null && timeSlotEntity.getDisplayDevices() != null) {
            Optional<Set<DisplayDeviceEntity>> optionalDisplayDevices = getDisplayDevices(timeSlotEntity);
            String checkResult = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(
                timeSlotEntity.getDisplayContent(), optionalDisplayDevices.get()
            );
            if(!"1".equals(checkResult)){
                return Result.err(checkResult);  
            }
        }

        timeSlotEntity.setId(Math.toIntExact(id));
        return Result.ok(timeSlotRepository.findById(Math.toIntExact(id)).map(existingTimeSlot -> {
            // if time slot from request has name, we set it to the existing time slot.
            // (same with other atts)

            Optional.ofNullable(timeSlotEntity.getName()).ifPresent(existingTimeSlot::setName);
            Optional.ofNullable(timeSlotEntity.getStartDate()).ifPresent(existingTimeSlot::setStartDate);
            Optional.ofNullable(timeSlotEntity.getEndDate()).ifPresent(existingTimeSlot::setEndDate);
            Optional.ofNullable(timeSlotEntity.getStartTime()).ifPresent(existingTimeSlot::setStartTime);
            Optional.ofNullable(timeSlotEntity.getEndTime()).ifPresent(existingTimeSlot::setEndTime);
            Optional.ofNullable(timeSlotEntity.getWeekdaysChosen()).ifPresent(existingTimeSlot::setWeekdaysChosen);
            //Display Devices
            Optional.ofNullable(timeSlotEntity.getDisplayDevices()).ifPresent(displayDevices -> {
                Set<DisplayDeviceEntity> updatedDisplayDevices = new HashSet<>();
                displayDevices.stream().forEach(displayDevice -> {
                    if (displayDevice.getId() == null) {
                        updatedDisplayDevices.add(displayDevice);
                        return;
                    }
                    Optional<DisplayDeviceEntity> foundDisplayDevice = displayDeviceRepository.findById(displayDevice.getId());
                    if (foundDisplayDevice.isEmpty()) return;
                    updatedDisplayDevices.add(displayDevice);
                });
                existingTimeSlot.setDisplayDevices(displayDevices);
            });
            //Display Content
            Optional.ofNullable(timeSlotEntity.getDisplayContent()).ifPresent(displayContent -> {
                if (displayContent.getId() == null) {
                    existingTimeSlot.setDisplayContent(displayContent);
                    return;
                }
                Optional<ContentEntity> content = findContentById(displayContent.getId());
                if (content.isEmpty()) return;
                existingTimeSlot.setDisplayContent(content.get());
            });

            TimeSlotEntity toReturn = timeSlotRepository.save(existingTimeSlot);
            pushTSService.updateDisplayDevicesToNewTimeSlots();
            return toReturn;
        }).orElseThrow(() -> new RuntimeException("Time slot not found")));
    }

    @Override
    public void delete(Long id) {
        TimeSlotEntity timeSlotToDelete = timeSlotRepository.findById(Math.toIntExact(id)).get();

        Set<DisplayDeviceEntity> displayDevices = timeSlotToDelete.getDisplayDevices();
        for (DisplayDeviceEntity displayDevice : displayDevices) {
            displayDevice.getTimeSlots().remove(timeSlotToDelete);
        }

        timeSlotToDelete.setDisplayContent(null);
        timeSlotToDelete.getDisplayDevices().clear();
        timeSlotRepository.save(timeSlotToDelete);

        timeSlotRepository.delete(timeSlotToDelete);
    }

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
        dd.getTimeSlots().remove(ts);

        timeSlotRepository.save(ts);
    }

    @Override
    public int countDisplayDeviceAssociations(Long timeSlotId) {
        return timeSlotRepository.findById(Math.toIntExact(timeSlotId)).get().getDisplayDevices().size();
    }


    @Override
    public Result<TimeSlotEntity, String> setDisplayContent(Long tsId, Long dcId, String dcType, Boolean forceDimensions) {
        //check if dimensions of displaydevice and content fit
        Optional<TimeSlotEntity> timeSlotToCheck = findOne(tsId);
        Optional<ContentEntity> contentToCheck = findContentById(dcId.intValue());

         // Validate existence of time slot
         // Validate existence of the referenced content
         if (timeSlotToCheck.isEmpty() || contentToCheck.isEmpty()) {
            return Result.err("Not found");
        }

        //check if dimensions of displaydevice and content fit
        TimeSlotEntity timeSlotEntity = timeSlotToCheck.get();
        ContentEntity displayContent = contentToCheck.get();
        if (timeSlotEntity.getDisplayDevices() != null && !forceDimensions) {
            String checkResult = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(displayContent, timeSlotEntity.getDisplayDevices());
            if(!"1".equals(checkResult)){
                return Result.err(checkResult);  
            }
        }
        
        return Result.ok(timeSlotRepository.findById(Math.toIntExact(tsId)).map(existingTimeSlot -> {
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
        }).orElseThrow(() -> new RuntimeException("Time Slot does not exist")));
    }

    @Override
    public Result<TimeSlotEntity, String> addDisplayDevice(Long id, Long displayDeviceId, Boolean forceDimensions) throws RuntimeException {
        Optional<TimeSlotEntity> timeSlotToCheck = findOne(id);
        Optional<DisplayDeviceEntity> displayDeviceToCheck = displayDeviceRepository.findById(displayDeviceId.intValue());


        if (timeSlotToCheck.isEmpty() || displayDeviceToCheck.isEmpty()) {
            return Result.err("Not found");
        }

        //check if dimensions of displaydevice and content fit
        TimeSlotEntity timeSlotEntity = timeSlotToCheck.get();
        DisplayDeviceEntity displayDeviceEntity = displayDeviceToCheck.get(); //safe to use .get() since already validated existence
        Set<DisplayDeviceEntity> displayDevice = Set.of(displayDeviceEntity);
        if (timeSlotEntity.getDisplayContent() != null && !forceDimensions) {
            String checkResult = dimensionCheckService.checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(timeSlotEntity.getDisplayContent(), displayDevice);
            if(!"1".equals(checkResult)){
                return Result.err(checkResult);
            }
        }

        return Result.ok(timeSlotRepository.findById(Math.toIntExact(id)).map(existingTimeSlot -> {
            DisplayDeviceEntity foundDisplayDevice = displayDeviceRepository.findById(Math.toIntExact(displayDeviceId)).orElseThrow();
            existingTimeSlot.addDisplayDevice(foundDisplayDevice);
            existingTimeSlot.setId(Math.toIntExact(id));
            TimeSlotEntity updatedTimeslot = timeSlotRepository.save(existingTimeSlot);
            return timeSlotRepository.save(updatedTimeslot);
        }).orElseThrow());
    }

    @Override
    public List<TimeSlotEntity> findOverlappingTimeSlots(Long id) {
        // Fetch the requested time slot, throw exception if not found
        TimeSlotEntity timeSlotEntity = timeSlotRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("TimeSlotEntity not found for id: " + id));

        Set<TimeSlotEntity> overlappingTimeSlots = new HashSet<>();

        // Iterate through associated display devices and their time slots
        for (DisplayDeviceEntity displayDevice : timeSlotEntity.getDisplayDevices()) {
            for (TimeSlotEntity deviceTimeSlot : displayDevice.getTimeSlots()) {
                // Check for overlap and add to the result set
                if (deviceTimeSlot.overlaps(timeSlotEntity) && !deviceTimeSlot.equals(timeSlotEntity)) {
                    overlappingTimeSlots.add(deviceTimeSlot);
                }
            }
        }

        // Return the overlapping time slots as a list
        return new ArrayList<>(overlappingTimeSlots);
    }

    @Override
    public List<TimeSlotColor> getTimeSlotColors() {

        List<TimeSlotColor> timeSlotColors = new ArrayList<>();

        List<TimeSlotEntity> timeslots = findAll();

        timeslots.forEach(timeSlotEntity -> {
            if (findOverlappingTimeSlots(Long.valueOf(timeSlotEntity.getId())).isEmpty()) {
                TimeSlotColor timeslotColor = TimeSlotColor.builder()
                        .id(Long.valueOf(timeSlotEntity.getId()))
                        .color("neutral")
                        .build();

                timeSlotColors.add(timeslotColor);
            } else {
                TimeSlotColor timeslotColor = TimeSlotColor.builder()
                        .id(Long.valueOf(timeSlotEntity.getId()))
                        .color("red")
                        .build();

                timeSlotColors.add(timeslotColor);
            }
        });

        return timeSlotColors;
    }
}
