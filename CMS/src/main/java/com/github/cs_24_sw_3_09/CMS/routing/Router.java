package com.github.cs_24_sw_3_09.CMS.routing;

//import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
//import org.apache.tomcat.util.http.parser.MediaType;
//import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class Router {


    @GetMapping("/")
    String home() {
        System.out.println("/");
        // return "Hello World!";
        return "index.jpg";
    }

    @GetMapping(value = "/visual_media/{mediaId}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] visual_media(@PathVariable String mediaId) throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/visual_media/" + mediaId + ".jpg");
        return IOUtils.toByteArray(in);
    }

    @GetMapping(value = {"/visual_media2/{mediaId}"})
    public ResponseEntity<StreamingResponseBody> getVideo(@PathVariable String mediaId,
                                                          @RequestParam("jpg") boolean jpg
    ) throws IOException {

        String contentType = jpg ? "image/jpg" : "video/mp4";
        String type = contentType.split("/")[1];

        InputStream is = getClass()
                .getResourceAsStream("/visual_media/" + mediaId + "." + type);


        if (is == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] resourceBytes = IOUtils.toByteArray(is);

        //InputStreamResource resource = new InputStreamResource(is);

        StreamingResponseBody resource = outputStream -> {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                System.out.println(bytesRead);
            }
            outputStream.flush();
        };

        System.out.println(resource);

        ResponseEntity<StreamingResponseBody> result = ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("inline; filename=%s.%s", mediaId, type))
                .body(resource);

        System.out.println(result);

        return result;
    }

    /*@GetMapping(value = "/visual_media/vid/{mediaId}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] test(@PathVariable String mediaId) throws IOException {
        System.out.println("ID: " + mediaId);

        InputStream in = getClass()
                .getResourceAsStream("/visual_media/" + mediaId + ".jpg");
        return IOUtils.toByteArray(in);
    }*/

    /*@GetMapping(value = "/visual_media2/{mediaId}", produces = MediaType.IMAGE_GIF_VALUE)
    @ResponseBody
    public byte[] test2(@PathVariable String mediaId) throws IOException {
        System.out.println("ID: " + mediaId);

        InputStream in = getClass()
                .getResourceAsStream("/visual_media/" + mediaId + ".jpg");
        return IOUtils.toByteArray(in);
    }*/

    // Example of how to make rest with varible path
    @GetMapping("/test/{testid}")
    public String testID(@PathVariable String testid) {
        System.out.println("/test with ID " + testid);
        return "test your id:" + testid;
    }

    /*
     * @Override
     * public void addResourceHandlers(ResourceHandlerRegistry registry) {
     * registry
     * .addResourceHandler("/files/**")
     * .addResourceLocations("file://");
     * }
     */

}
