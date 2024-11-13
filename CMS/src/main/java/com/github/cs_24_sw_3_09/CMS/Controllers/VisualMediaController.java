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

    @Autowired
    IVisualMediaService visualMediaService;

    @GetMapping(value = "/{mediaId}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] visual_media(@PathVariable String mediaId) throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/visual_media/" + mediaId + ".jpg");
        return IOUtils.toByteArray(in);
    }


    @PostMapping(value = "/upload")
    public ResponseEntity<Object> uploadFileRoute(@RequestParam("file") MultipartFile file)
            throws IOException {
         
        System.out.println("here");
        visualMediaService.createVisualMedia(file);
        return new ResponseEntity<>("File uploaded successfully", HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("path") String filePath) {
        File fileToDelete = FileUtils.createFileFromRoot(filePath);

        if (!fileToDelete.exists()) {
            return ResponseEntity.notFound().build();
        }

        if (!fileToDelete.delete()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok("File deleted successfully");
    }
}
