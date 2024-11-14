package com.github.cs_24_sw_3_09.CMS.Controllers;

import com.github.cs_24_sw_3_09.CMS.services.IVisualMediaService;
import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.VisualMediaServiceImpl;
import com.github.cs_24_sw_3_09.CMS.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/visual_media")

public class VisualMediaController {

    private final IVisualMediaService visualMediaService;

    @Autowired
    public VisualMediaController(IVisualMediaService visualMediaService) {
        this.visualMediaService = visualMediaService;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<Object> uploadFileRoute(@RequestParam("file") MultipartFile file)
            throws IOException {
        visualMediaService.createVisualMedia(file);
        return new ResponseEntity<>("File uploaded successfully", HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Object> deleteFile(@RequestParam("path") String filePath) {
        return visualMediaService.deleteVisualMedia(filePath);
    }
}
