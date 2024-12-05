package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.io.File;
import java.io.IOException;
//import java.lang.classfile.ClassFile.Option;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
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

//import jakarta.validation.constraints.Null;

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
    public String checkDimensionForAssignedFallback(long displayDeviceId, ContentEntity fallbackContent){
        Optional<DisplayDeviceEntity> optionalDisplayDevice = displayDeviceService.findOne(displayDeviceId);
        if(optionalDisplayDevice.isEmpty()){
            throw new IllegalArgumentException("DisplayDevice with ID " + displayDeviceId + " does not exist.");
        }
        DisplayDeviceEntity displayDevice = optionalDisplayDevice.get();
        String displayDeviceOrientation = displayDevice.getDisplayOrientation();

        String fallbackOrientation;
        if (fallbackContent instanceof VisualMediaEntity) {
            Optional<VisualMediaEntity> optionalVisualMedia = visualMediaService.findOne(fallbackContent.getId().longValue());
            if(optionalVisualMedia.isEmpty()){
                throw new IllegalArgumentException("Visual Media with ID " + fallbackContent.getId() + " does not exist.");   
            }
            VisualMediaEntity visualMedia = optionalVisualMedia.get();
            
            if (visualMedia.getLocation() == null || visualMedia.getFileType() == null) {
                return "File not correctly configured";
            }

            fallbackOrientation = getVisualMediaOrientation(visualMedia.getFileType(), visualMedia.getLocation());
            
            if(!fallbackOrientation.equals(displayDeviceOrientation)){
                return "The dimension do not match:\nDisplay Device orientation: " + displayDeviceOrientation + 
                "\nFallback Visual Media orientation: "+ fallbackOrientation;
            }

            //1 is returned if the orientations match, therefore the service logic can continue
            return "1";

        } else if (fallbackContent instanceof SlideshowEntity){
            Optional<SlideshowEntity> optionalSlideshow = slideshowService.findOne(fallbackContent.getId().longValue());
            if(optionalSlideshow.isEmpty()){
                throw new IllegalArgumentException("Slideshow with ID " + fallbackContent.getId() + " does not exist.");   
            } 
            SlideshowEntity slideshow = optionalSlideshow.get();
            Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionService.findAllVisualMediaInclusionInSlideshow(slideshow.getId().longValue());
            fallbackOrientation = getSlideshowOrientation(visualMediaInclusionsInSlideshow);

            if(fallbackOrientation.equals("mixed")){
                return "The fallback orientation is mixed"; 
            } else if (!fallbackOrientation.equals(displayDeviceOrientation)) {
                return "The dimension do not match:\nDisplay Device orientation: " + displayDeviceOrientation + 
                "\nFallback Slide show orientation: "+ fallbackOrientation;
            } 

            return "1";
        } 
        return "Fallback content not set";      
    }

    private String getVisualMediaOrientation(String filetype, String path) {
        return switch(filetype) {
            case "mp4" -> getVisualMediaVideoOrientation(path);
            default -> getVisualMediaImageOrientation(path);
        };
    }

    @Override
    public String checkDimensionForAssignedVisualMediaToSlideshow(Long addedVisualMediaInclusionId, Long slideshowId){
        Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionService.findAllVisualMediaInclusionInSlideshow(slideshowId);
        //if there is only one Visual Media Inclusion in Slideshow there is nothing to check
        if (visualMediaInclusionsInSlideshow.size() <= 1){
            return "Only one visual media present";
        }

        String slideshowOrientation = getSlideshowOrientation(visualMediaInclusionsInSlideshow);
        if (slideshowOrientation.equals("mixed")) {
            return "The dimensions in the slideshow are mixed";
        }

        //find sent vmi dimension
        Optional<VisualMediaInclusionEntity> optionalVisualMediaInclusion = visualMediaInclusionService.findOne(addedVisualMediaInclusionId);
        if(optionalVisualMediaInclusion.isEmpty()){
            throw new IllegalArgumentException("Visual Media Inclusion with ID " + addedVisualMediaInclusionId + " does not exist.");   
        }
        VisualMediaInclusionEntity addedVisualMediaInclusion = optionalVisualMediaInclusion.get();        
        VisualMediaEntity addedvisualMedia = addedVisualMediaInclusion.getVisualMedia();

        if (addedvisualMedia.getLocation() == null || addedvisualMedia.getFileType() == null) {
            return "File not correctly configured";
        }

        String addedVisualMediaOrientation = getVisualMediaOrientation(addedvisualMedia.getFileType(), addedvisualMedia.getLocation());
       
        if(!addedVisualMediaOrientation.equals(slideshowOrientation)) {
            return "The dimension do not match:\nSlideshow orientation: " + slideshowOrientation + 
            "\nVisual Media orientation: "+ addedVisualMediaOrientation;
        }

        return "1";
    }

    @Override
    public String checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(long timeSlotId){
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
        if(displayDeviceOrientation.size() > 1) {
            return "The dimensions of display devices are mixed";
        }

        ContentEntity displayContent = timeslot.getDisplayContent();
        String displayContentOrientation;
        if (displayContent instanceof VisualMediaEntity) {
            Optional<VisualMediaEntity> optionalVisualMedia = visualMediaService.findOne(displayContent.getId().longValue());
            if(optionalVisualMedia.isEmpty()) {
                throw new IllegalArgumentException("Visual Media with ID " + displayContent.getId() + " does not exist.");
            }
            VisualMediaEntity visualMedia = optionalVisualMedia.get();

            if (visualMedia.getLocation() == null || visualMedia.getFileType() == null) {
                return "File not correctly configured";
            }

            displayContentOrientation = getVisualMediaOrientation(visualMedia.getFileType(), visualMedia.getLocation());
                          
            if(!displayDeviceOrientation.contains(displayContentOrientation)){
                return "The dimensions do not match";
            }
            return "1";

        } else if(displayContent instanceof SlideshowEntity) {
            Optional<SlideshowEntity> optionalSlideshow = slideshowService.findOne(displayContent.getId().longValue());
            if(optionalSlideshow.isEmpty()){
                throw new IllegalArgumentException("Slideshow with ID " + displayContent.getId() + " does not exist.");
            }
            SlideshowEntity slideshow = optionalSlideshow.get();
            Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionService.findAllVisualMediaInclusionInSlideshow(slideshow.getId().longValue());
            displayContentOrientation = getSlideshowOrientation(visualMediaInclusionsInSlideshow);
            
            //If the slideshow has mixed dimensions or the displaydevices are mixed pass check
            if(displayContentOrientation.equals("mixed")) {
                return "The dimensions of slideshow are mixed";
            }
        
            if (!displayDeviceOrientation.contains(displayContentOrientation)) {
                return "The dimensions do not match";
            }

            return "1";
        }

        return "Display content not set";
    }

    private String getVisualMediaVideoOrientation(String visualMediaPath) {
        String rootPath = System.getProperty("user.dir"); 
        String relativePath = visualMediaPath;
        String absolutePath = Paths.get(rootPath, relativePath).toString();
        
        try {
            File videoFile = new File(absolutePath);

            // Extract a frame from the video
            FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(videoFile));

            // Get the first frame to determine dimensions
            Picture picture = grab.getNativeFrame();

            int width = picture.getWidth();
            int height = picture.getHeight();
            String visualMediaOrientation = (Math.max(width, height) == width) ? "horizontal" : "vertical";
            return visualMediaOrientation;                
            
        } catch (NullPointerException | IOException | JCodecException e) {
            e.printStackTrace();
            return e.toString();
        } 
    }

    
    private String getSlideshowOrientation(Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow){    
        Set<String> visualMediasInSlideshowOrientation = new HashSet<>();
        for(VisualMediaInclusionEntity vmi : visualMediaInclusionsInSlideshow){
            VisualMediaEntity visualMediaPartOfSlideshow = vmi.getVisualMedia();

            if (visualMediaPartOfSlideshow.getLocation() == null 
            || visualMediaPartOfSlideshow.getFileType() == null) {
                return "File not correctly configured";
            }

            visualMediasInSlideshowOrientation.add(
                getVisualMediaOrientation(
                    visualMediaPartOfSlideshow.getFileType(), visualMediaPartOfSlideshow.getLocation()
                )
            );
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
            } catch (NullPointerException | IOException e){
                e.printStackTrace();
                return e.toString();
            } 
    }
}
