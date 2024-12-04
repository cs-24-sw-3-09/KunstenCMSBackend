package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

import com.github.cs_24_sw_3_09.CMS.model.entities.ContentEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.services.DimensionCheckService;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.VisualMediaService;

@Service
public class DimensionCheckServiceImpl implements DimensionCheckService{

    private VisualMediaService visualMediaService;
    private DisplayDeviceService displayDeviceService;

    public DimensionCheckServiceImpl(VisualMediaService visualMediaService, DisplayDeviceService displayDeviceService){
        this.visualMediaService = visualMediaService;
        this.displayDeviceService = displayDeviceService;
    }

    @Override
    public Boolean checkDimensionForAssignedFallback(Long displayDeviceId){
        DisplayDeviceEntity displayDevice = displayDeviceService.findOne(displayDeviceId).get();
        String displayOrientation = displayDevice.getDisplayOrientation();

        ContentEntity fallbackContent = displayDevice.getFallbackContent();
        //TODO: handle video!
        if (fallbackContent instanceof VisualMediaEntity) {
            VisualMediaEntity visualMedia = visualMediaService.findOne(fallbackContent.getId().longValue()).get();

            String rootPath = System.getProperty("user.dir"); 
            String relativePath = visualMedia.getLocation();
            String absolutePath = Paths.get(rootPath, relativePath).toString();

            try{
                File file = new File(absolutePath);
            if (!file.exists()) {
                System.err.println("File does not exist at path: " + absolutePath);
                return false; 
            }
                BufferedImage image = ImageIO.read(file);
                int width = image.getWidth();
                int height = image.getHeight();
                String fallbackOrientation = (Math.max(width, height) == width) ? "horizontal" : "vertical";
                
                if(!fallbackOrientation.equals(displayOrientation)){
                    return false;
                }
            } catch (IOException e){
                e.printStackTrace();
            } 
        } //TODO: handle slideshow   
        return true;      
    }
}
