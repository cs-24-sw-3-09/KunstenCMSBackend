package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.lang.classfile.ClassFile.Option;
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
    private VisualMediaInclusionService visualMediaInclusionService;
    private SlideshowService slideshowService;
    private TimeSlotService timeSlotService;

    public DimensionCheckServiceImpl(VisualMediaService visualMediaService, DisplayDeviceService displayDeviceService,
                                        VisualMediaInclusionService visualMediaInclusionService,
                                        SlideshowService slideshowService, TimeSlotService timeSlotService){
        this.visualMediaService = visualMediaService;
        this.displayDeviceService = displayDeviceService;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.slideshowService = slideshowService;
        this.timeSlotService = timeSlotService;
    }

    @Override
    public Boolean checkDimensionForAssignedFallback(Long displayDeviceId){
        Optional<DisplayDeviceEntity> optionalDisplayDevice = displayDeviceService.findOne(displayDeviceId);
        if(optionalDisplayDevice.isEmpty()){
            throw new IllegalArgumentException("DisplayDevice with ID " + displayDeviceId + " does not exist.");
        }
        DisplayDeviceEntity displayDevice = optionalDisplayDevice.get();

        String displayDeviceOrientation = displayDevice.getDisplayOrientation();
        ContentEntity fallbackContent = displayDevice.getFallbackContent();
        if (fallbackContent == null) {
            throw new IllegalArgumentException("Fallback content is not set for DisplayDevice with ID " + displayDeviceId);
        }

        String fallbackOrientation;
        //TODO: handle video!
        if (fallbackContent instanceof VisualMediaEntity) {
            Optional<VisualMediaEntity> optionalVisualMedia = visualMediaService.findOne(fallbackContent.getId().longValue());
            if(optionalVisualMedia.isEmpty()){
                throw new IllegalArgumentException("Visual Media with ID " + fallbackContent.getId() + " does not exist.");   
            }
            VisualMediaEntity visualMedia = optionalVisualMedia.get();
            
            fallbackOrientation = getVisualMediaImageOrientation(visualMedia.getLocation());
            if(!fallbackOrientation.equals(displayDeviceOrientation)){
                return false;
            }

        } else if (fallbackContent instanceof SlideshowEntity){
            Optional<SlideshowEntity> optionalSlideshow = slideshowService.findOne(fallbackContent.getId().longValue());
            if(optionalSlideshow.isEmpty()){
                throw new IllegalArgumentException("Slideshow with ID " + fallbackContent.getId() + " does not exist.");   
            } 
            SlideshowEntity slideshow = optionalSlideshow.get();
            Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionService.findAllVisualMediaInclusionInSlideshow(slideshow.getId().longValue());
            fallbackOrientation = getSlideshowOrientation(visualMediaInclusionsInSlideshow);

            if(fallbackOrientation.equals("mixed")){
                return true; //If slideshow already has mixed orientation then pass check
            } else if (!fallbackOrientation.equals(displayDeviceOrientation)) {
                return false;
            }  
        } 
        return true;      
    }

    @Override
    public Boolean checkDimensionForAssignedVisualMediaToSlideshow(Long addedVisualMediaInclusionId, Long slideshowId){
        Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionService.findAllVisualMediaInclusionInSlideshow(slideshowId);
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
        Optional<VisualMediaInclusionEntity> optionalVisualMediaInclusion = visualMediaInclusionService.findOne(addedVisualMediaInclusionId);
        if(optionalVisualMediaInclusion.isEmpty()){
            throw new IllegalArgumentException("Visual Media Inclusion with ID " + addedVisualMediaInclusionId + " does not exist.");   
        }
        VisualMediaInclusionEntity addedVisualMediaInclusion = optionalVisualMediaInclusion.get();        
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
            displayDeviceOrientation.add(orientation);
        }

        ContentEntity displayContent = timeslot.getDisplayContent();
        String displayContentOrientation;
        if (displayContent instanceof VisualMediaEntity) {
            Optional<VisualMediaEntity> optionalVisualMedia = visualMediaService.findOne(displayContent.getId().longValue());
            if(optionalVisualMedia.isEmpty()){
                throw new IllegalArgumentException("Visual Media with ID " + displayContent.getId() + " does not exist.");
            }
            VisualMediaEntity visualMedia = optionalVisualMedia.get();
            displayContentOrientation = getVisualMediaImageOrientation(visualMedia.getLocation());

        } else if(displayContent instanceof SlideshowEntity) {
            Optional<SlideshowEntity> optionalSlideshow = slideshowService.findOne(displayContent.getId().longValue());
            if(optionalSlideshow.isEmpty()){
                throw new IllegalArgumentException("Slideshow with ID " + displayContent.getId() + " does not exist.");
            }
            SlideshowEntity slideshow = optionalSlideshow.get();
            Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionService.findAllVisualMediaInclusionInSlideshow(slideshow.getId().longValue());
            displayContentOrientation = getSlideshowOrientation(visualMediaInclusionsInSlideshow);
            
            //If the slideshow has mixed dimensions or the displaydevices are mixed pass check
            if(displayContentOrientation.equals("mixed") || displayDeviceOrientation.size() > 1){
                return true;
            }
            if (!displayDeviceOrientation.contains(displayContentOrientation)) {
                return false;
            }
        }

        return true;
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
}
