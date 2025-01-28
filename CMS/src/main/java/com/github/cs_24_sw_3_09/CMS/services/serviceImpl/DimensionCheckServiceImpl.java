package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.io.File;
import java.io.IOException;
//import java.lang.classfile.ClassFile.Option;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayDeque;

import javax.imageio.ImageIO;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.SlideshowService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

//import jakarta.validation.constraints.Null;

@Service
public class DimensionCheckServiceImpl implements DimensionCheckService{

    private VisualMediaService visualMediaService;
    private VisualMediaInclusionService visualMediaInclusionService;

    @Lazy
    private SlideshowService slideshowService;

    public DimensionCheckServiceImpl(VisualMediaService visualMediaService,
                                        VisualMediaInclusionService visualMediaInclusionService,
                                        @org.springframework.context.annotation.Lazy SlideshowService slideshowService){
        this.visualMediaService = visualMediaService;
        this.visualMediaInclusionService = visualMediaInclusionService;
        this.slideshowService = slideshowService;
    }

    @Override
    public String checkDimensionForAssignedFallback(DisplayDeviceEntity displayDevice, ContentEntity fallbackContent){
        String displayDeviceOrientation = displayDevice.getDisplayOrientation();    
        if (fallbackContent instanceof VisualMediaEntity) {
            return checkDisplayDeviceBetweenVisualMedia(
                displayDeviceOrientation, 
                fallbackContent.getId().longValue()
            );
        } else if (fallbackContent instanceof SlideshowEntity){
            return checkDisplayDeviceBetweenSlideshow(
                displayDeviceOrientation, 
                fallbackContent.getId().longValue()
            );
        } else {
            return "Fallback content not set"; 
        }      
    }

    private String getVisualMediaOrientation(String filetype, String path) {
        return switch(filetype) {
            case "mp4" -> getVisualMediaVideoOrientation(path);
            default -> getVisualMediaImageOrientation(path);
        };
    }

    @Override
    public String checkDimensionForAssignedVisualMediaToSlideshow(Long visualMediaInclusionId, Long slideshowId){
        Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionService.findAllVisualMediaInclusionInSlideshow(slideshowId);
        //if there is only one Visual Media Inclusion in Slideshow there is nothing to check
        if (visualMediaInclusionsInSlideshow.size() < 0){
            return "No visual media present";
        } else if(visualMediaInclusionsInSlideshow.size() == 0){
            return "1";
        }

        Deque<String> slideshowOrientations = getSlideshowOrientation(visualMediaInclusionsInSlideshow);
        if (!slideshowOrientations.peekFirst().split(": ")[1].equals(
            slideshowOrientations.peekLast().split(": ")[1])) {
            //todo: print this first 10 elements
            return "The media in the slideshow has mixed orientation";
        }

        //find sent vmi dimension
        Optional<VisualMediaInclusionEntity> optionalVisualMediaInclusion = visualMediaInclusionService.findOne(visualMediaInclusionId);
        if(optionalVisualMediaInclusion.isEmpty()){
            throw new IllegalArgumentException("Visual Media Inclusion with ID " + visualMediaInclusionId + " does not exist.");   
        }
        VisualMediaInclusionEntity visualMediaInclusion = optionalVisualMediaInclusion.get();        
        VisualMediaEntity visualMedia = visualMediaInclusion.getVisualMedia();

        if (visualMedia.getLocation() == null || visualMedia.getFileType() == null) {
            return "File not correctly configured";
        }

        String VisualMediaOrientation = getVisualMediaOrientation(visualMedia.getFileType(), visualMedia.getLocation());
       
        if(!VisualMediaOrientation.equals(slideshowOrientation)) {
            return "The dimension do not match:\nSlideshow orientation: " + slideshowOrientation + 
            "\nVisual Media orientation: "+ VisualMediaOrientation;
        }

        return "1";
    }

    @Override
    public String checkDimensionBetweenDisplayDeviceAndContentInTimeSlot(ContentEntity displayContent, Set<DisplayDeviceEntity> displayDevices){
       
        List<String> displayDeviceOrientationList = new ArrayList<>();
        Set<String> displayDeviceOrientationSet = new HashSet<>();
        for (DisplayDeviceEntity device : displayDevices){
            String orientation = device.getDisplayOrientation();
            displayDeviceOrientationList.add(device.getName() + ": " + orientation);
            displayDeviceOrientationSet.add(orientation);
        }
        if(displayDeviceOrientationSet.size() > 1) {
            return "The dimensions of display devices are mixed:\n" 
            + createErrorMessageWithList(displayDeviceOrientationList);
        }

        if (displayContent instanceof VisualMediaEntity) {
            return checkDisplayDeviceBetweenVisualMedia(
                displayDeviceOrientationSet.iterator().next(), 
                displayContent.getId().longValue()
            );
        } else if(displayContent instanceof SlideshowEntity) {
            return checkDisplayDeviceBetweenSlideshow(
                displayDeviceOrientationSet.iterator().next(), displayContent.getId().longValue()
            );
        } else {
            return "Display content not set"; 
        }
    }

    String checkDisplayDeviceBetweenVisualMedia(String displayDeviceOrientation, Long id) { 
        Optional<VisualMediaEntity> optionalVisualMedia = visualMediaService.findOne(id);
        if(optionalVisualMedia.isEmpty()) {
            throw new IllegalArgumentException("Visual Media with ID " + id + " does not exist.");
        }
        VisualMediaEntity visualMedia = optionalVisualMedia.get();

        if (visualMedia.getLocation() == null || visualMedia.getFileType() == null) {
            return "File not correctly configured";
        }

        String displayContentOrientation = getVisualMediaOrientation(visualMedia.getFileType(), visualMedia.getLocation());
                        
        if(!displayDeviceOrientation.contains(displayContentOrientation)){
            return "The dimension do not match:\n\tDisplay Devices orientation: " 
            + (displayContentOrientation.equals("vertical") ? "horizontal" : "vertical")
            + "\n\tThe visual media orientation: "+ displayContentOrientation;
        }
        return "1";
    }

    String checkDisplayDeviceBetweenSlideshow(String displayDeviceOrientation, Long id) { 
        Optional<SlideshowEntity> optionalSlideshow = slideshowService.findOne(id);
        if(optionalSlideshow.isEmpty()){
            throw new IllegalArgumentException("Slideshow with ID " + id + " does not exist.");
        }
        SlideshowEntity slideshow = optionalSlideshow.get();
        Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionService.findAllVisualMediaInclusionInSlideshow(slideshow.getId().longValue());
        Deque<String> displayContentOrientations = getSlideshowOrientation(visualMediaInclusionsInSlideshow);
        
        //todo: handle if empty

        //If the slideshow has mixed dimensions or the displaydevices are mixed pass check
        if(!displayContentOrientations.peekFirst().split(": ")[1].equals(
            displayContentOrientations.peekLast().split(": ")[1])
        ) {

            //TODO: Print the user defined amount of slideshows with different dimensions.
            return "The dimensions of slideshow are mixed\n";
        }
    
        if (!displayDeviceOrientation.contains(displayContentOrientation)) {
            return "The dimension do not match:\nDisplay Device orientation:\n" + 
            createErrorMessageWithList(displayDeviceOrientationList) + 
            "\nthe visual media orientation: "+ displayContentOrientation;
        }

        return "1";
    }


    private String createErrorMessageWithList(List<String> list) {
        return list.stream().map(el -> "\t" + el).collect(java.util.stream.Collectors.joining("\n"));
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

    
    private Deque<String> getSlideshowOrientation(Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow){  
        ArrayDeque<String> visualMediasInSlideshowOrientation = new ArrayDeque<>();
        
        for(VisualMediaInclusionEntity vmi : visualMediaInclusionsInSlideshow){
            VisualMediaEntity visualMediaPartOfSlideshow = vmi.getVisualMedia();

            if (visualMediaPartOfSlideshow.getLocation() == null 
            || visualMediaPartOfSlideshow.getFileType() == null) {
                //todo: Find lige ud af, om det her skal error handles
                continue;
            }

            String orientation = visualMediaPartOfSlideshow.getName() +
            ": " + getVisualMediaOrientation(
                visualMediaPartOfSlideshow.getFileType(), visualMediaPartOfSlideshow.getLocation()
            ); 


            if (orientation.contains("vertical")) {
                visualMediasInSlideshowOrientation.addLast(orientation);
            } else {
                visualMediasInSlideshowOrientation.addFirst(orientation);
            }
        }

        return visualMediasInSlideshowOrientation;
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

                int max_length = Math.max(width, height);
                
                String visualMediaOrientation;
                if (max_length == height) {
                    visualMediaOrientation = "vertical";
                }  else if (max_length == width) { 
                    visualMediaOrientation = "horizontal";
                } else {
                    visualMediaOrientation = "square";
                }
                return visualMediaOrientation;
                
            } catch (NullPointerException | IOException e){
                e.printStackTrace();
                return e.toString();
            } 
    }
}
