package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.SlideshowRepository;
import com.github.cs_24_sw_3_09.CMS.repositories.VisualMediaInclusionRepository;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaInclusionService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

@Service
public class DimensionCheckServiceImpl implements DimensionCheckService{

    private VisualMediaService visualMediaService;
    private DisplayDeviceService displayDeviceService;
    private VisualMediaInclusionRepository visualMediaInclusionRepository;
    private SlideshowRepository slideshowRepository;
    private VisualMediaInclusionService visualMediaInclusionService;

    public DimensionCheckServiceImpl(VisualMediaService visualMediaService, DisplayDeviceService displayDeviceService,
                                        VisualMediaInclusionRepository visualMediaInclusionRepository, 
                                        SlideshowRepository slideshowRepository, VisualMediaInclusionService visualMediaInclusionService){
        this.visualMediaService = visualMediaService;
        this.displayDeviceService = displayDeviceService;
        this.visualMediaInclusionRepository = visualMediaInclusionRepository;
        this.slideshowRepository = slideshowRepository;
        this.visualMediaInclusionService = visualMediaInclusionService;
    }

    @Override
    public Boolean checkDimensionForAssignedFallback(Long displayDeviceId){
        DisplayDeviceEntity displayDevice = displayDeviceService.findOne(displayDeviceId).get();
        String displayOrientation = displayDevice.getDisplayOrientation();

        ContentEntity fallbackContent = displayDevice.getFallbackContent();
        //TODO: handle video!
        if (fallbackContent instanceof VisualMediaEntity) {
            VisualMediaEntity visualMedia = visualMediaService.findOne(fallbackContent.getId().longValue()).get();

            String fallbackOrientation = getVisualMediaImageOrientation(visualMedia.getLocation());
                
            if(!fallbackOrientation.equals(displayOrientation)){
                    return false;
            }
            
        } //TODO: handle slideshow   
        return true;      
    }

    @Override
    public Boolean checkDimensionForAssignedVisualMediaToSlideshow(Long addedVisualMediaInclusionId, Long slideshowId){
        Set<VisualMediaInclusionEntity> visualMediaInclusionsInSlideshow = visualMediaInclusionRepository.findAllVisualMediaInclusionForSlideshow(slideshowId);
        //if there is only one Visual Media Inclusion in Slideshow there is nothing to check
        if (visualMediaInclusionsInSlideshow.size() <= 1){
            return true;
        }

        //add check to see if anything is a video!

        Set<String> visualMediasInSlideshowOrientation = new HashSet();
        for(VisualMediaInclusionEntity vmi : visualMediaInclusionsInSlideshow){
            VisualMediaEntity visualMediaPartOfSlideshow = vmi.getVisualMedia();
            visualMediasInSlideshowOrientation.add(getVisualMediaImageOrientation(visualMediaPartOfSlideshow.getLocation()));
        System.out.println("ran for loop "+ visualMediasInSlideshowOrientation);
        }
        //if Set.size > 2 = mixed ->?
        //find sent vmi dimension
        VisualMediaInclusionEntity addedVisualMediaInclusion = visualMediaInclusionService.findOne(addedVisualMediaInclusionId).get();        
        VisualMediaEntity addedvisualMedia = addedVisualMediaInclusion.getVisualMedia();
        System.out.println("before call");
        String addedVisualMediaOrientation = getVisualMediaImageOrientation(addedvisualMedia.getLocation());
       
        for (String orientation : visualMediasInSlideshowOrientation){
            if(!addedVisualMediaOrientation.equals(orientation)){
                return false;
            }
        }
        return true;
    }

    private String getVisualMediaImageOrientation(String visualMediaPath){
        
        String rootPath = System.getProperty("user.dir"); 
            String relativePath = visualMediaPath;
            String absolutePath = Paths.get(rootPath, relativePath).toString();
            System.out.println("abs: "+absolutePath);
            try{
                File file = new File(absolutePath);
                if (!file.exists()) {
                    System.err.println("File does not exist at path: " + absolutePath);
                }
            System.out.println("file: "+file);
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
