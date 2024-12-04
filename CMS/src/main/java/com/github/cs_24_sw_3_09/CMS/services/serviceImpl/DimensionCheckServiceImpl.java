package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaInclusionRepository;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.TimeSlotService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

import jakarta.validation.constraints.Null;

@Service
public class DimensionCheckServiceImpl implements DimensionCheckService{

    private VisualMediaService visualMediaService;
    private DisplayDeviceService displayDeviceService;
    private VisualMediaInclusionRepository visualMediaInclusionRepository;
    private SlideshowRepository slideshowRepository;
    private VisualMediaInclusionService visualMediaInclusionService;
    private SlideshowService slideshowService;
    private TimeSlotService timeSlotService;

    public DimensionCheckServiceImpl(VisualMediaService visualMediaService, DisplayDeviceService displayDeviceService,
                                        VisualMediaInclusionRepository visualMediaInclusionRepository, 
                                        SlideshowRepository slideshowRepository, VisualMediaInclusionService visualMediaInclusionService,
                                        SlideshowService slideshowService, TimeSlotService timeSlotService){
        this.visualMediaService = visualMediaService;
        this.displayDeviceService = displayDeviceService;
        this.visualMediaInclusionRepository = visualMediaInclusionRepository;
        this.slideshowRepository = slideshowRepository;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.slideshowService = slideshowService;
        this.timeSlotService = timeSlotService;
    }

    @Override
    public Boolean checkDimensionForAssignedFallback(Long displayDeviceId){
        DisplayDeviceEntity displayDevice = displayDeviceService.findOne(displayDeviceId).get();
        String displayDeviceOrientation = displayDevice.getDisplayOrientation();
        ContentEntity fallbackContent = displayDevice.getFallbackContent();
        String fallbackOrientation;
        //TODO: handle video!
        if (fallbackContent instanceof VisualMediaEntity) {
            VisualMediaEntity visualMedia = visualMediaService.findOne(fallbackContent.getId().longValue()).get();
            fallbackOrientation = getVisualMediaImageOrientation(visualMedia.getLocation());
                
            if(!fallbackOrientation.equals(displayDeviceOrientation)){
                    return false;
            }
            
        } else{
            SlideshowEntity slideshow = slideshowService.findOne(fallbackContent.getId().longValue()).get();
            Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionRepository.findAllVisualMediaInclusionForSlideshow(slideshow.getId().longValue());
            fallbackOrientation = getSlideshowOrientation(visualMediaInclusionsInSlideshow);
        }
            if(fallbackOrientation.equals("mixed")){
                return true; //If slideshow already has mixed orientation then pass check
            } else if (!fallbackOrientation.equals(displayDeviceOrientation)) {
                return false;
            }      
        return true;      
    }

    @Override
    public Boolean checkDimensionForAssignedVisualMediaToSlideshow(Long addedVisualMediaInclusionId, Long slideshowId){
        Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionRepository.findAllVisualMediaInclusionForSlideshow(slideshowId);
        //if there is only one Visual Media Inclusion in Slideshow there is nothing to check
        if (visualMediaInclusionsInSlideshow.size() <= 1){
            return true;
        }

        String slideshowOrientation = getSlideshowOrientation(visualMediaInclusionsInSlideshow);
        //If there are both vertical and horizontal images in slideshow then pass check.
        if (slideshowOrientation.equals("mixed")) {
            return true;
        }

        //find sent vmi dimension
        VisualMediaInclusionEntity addedVisualMediaInclusion = visualMediaInclusionService.findOne(addedVisualMediaInclusionId).get();        
        VisualMediaEntity addedvisualMedia = addedVisualMediaInclusion.getVisualMedia();
        String addedVisualMediaOrientation = getVisualMediaImageOrientation(addedvisualMedia.getLocation());
       
        if(!addedVisualMediaOrientation.equals(slideshowOrientation)){
            return false;
        }
        
        return true;
    }

    public Boolean checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(long timeSlotId){
        Optional<TimeSlotEntity> optionalTimeSlot = timeSlotService.findOne(timeSlotId);
        if (optionalTimeSlot.isEmpty()) {
            throw new IllegalArgumentException("TimeSlot with ID " + timeSlotId + " does not exist.");
        }
        TimeSlotEntity timeslot = optionalTimeSlot.get();

        Set<DisplayDeviceEntity> displayDevices = timeslot.getDisplayDevices();
        Set<String> displayDeviceOrientation = new HashSet<>();
        for (DisplayDeviceEntity device : displayDevices){
            String orientation = device.getDisplayOrientation();
            if(orientation != null){
                displayDeviceOrientation.add(orientation);
            }
        }

        ContentEntity displayContent = timeslot.getDisplayContent();
        String displayContentOrientation = null;

        if (displayContent instanceof VisualMediaEntity) {
            Optional<VisualMediaEntity> optionalVisualMedia = visualMediaService.findOne(displayContent.getId().longValue());
            if(optionalVisualMedia.isPresent()){
                VisualMediaEntity visualMedia = optionalVisualMedia.get();
                displayContentOrientation = getVisualMediaImageOrientation(visualMedia.getLocation());
            }
        } else if(displayContent instanceof SlideshowEntity) {
            Optional<SlideshowEntity> optionalSlideshow = slideshowService.findOne(displayContent.getId().longValue());
            if(optionalSlideshow.isPresent()){
                SlideshowEntity slideshow = optionalSlideshow.get();
                Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionRepository.findAllVisualMediaInclusionForSlideshow(slideshow.getId().longValue());
                displayContentOrientation = getSlideshowOrientation(visualMediaInclusionsInSlideshow);
            }
        }

        if (displayContentOrientation == null) {
            throw new IllegalArgumentException("Could not determine orientation for display content.");
        }
        //If the slideshow has mixed dimensions or the displaydevices are mixed pass check
        if(displayContentOrientation.equals("mixed") || displayDeviceOrientation.size() > 1){
            return true;
        }
        
        if (!displayDeviceOrientation.contains(displayContentOrientation)) {
            return false;
        }
        return true;
    }

    private String getVisualMediaImageOrientation(String visualMediaPath){
            String rootPath = System.getProperty("user.dir"); 
            String relativePath = visualMediaPath;
            String absolutePath = Paths.get(rootPath, relativePath).toString();
           
            try{
                File file = new File(absolutePath);
                if (!file.exists()) {
                    System.err.println("File does not exist at path: " + absolutePath);
                }
            
                BufferedImage image = ImageIO.read(file);
                int width = image.getWidth();
                int height = image.getHeight();
                String visualMediaOrientation = (Math.max(width, height) == width) ? "horizontal" : "vertical";
                return visualMediaOrientation;
            } catch (IOException e){
                e.printStackTrace();
                return "error"; // not the best
            } 
    }

    private String getSlideshowOrientation(Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow){    
        //add check to see if anything is a video!
        Set<String> visualMediasInSlideshowOrientation = new HashSet<>();
        for(VisualMediaInclusionEntity vmi : visualMediaInclusionsInSlideshow){
            VisualMediaEntity visualMediaPartOfSlideshow = vmi.getVisualMedia();
            visualMediasInSlideshowOrientation.add(getVisualMediaImageOrientation(visualMediaPartOfSlideshow.getLocation()));
        }
        if (visualMediasInSlideshowOrientation.size() > 1) {
            return "mixed";
        } else if (visualMediasInSlideshowOrientation.contains("horizontal")){
            return "horizontal";
        } else {
            return "vertical";
        }
    }
}
