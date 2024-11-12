package com.github.cs_24_sw_3_09.CMS.routing;

//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
//import org.apache.tomcat.util.http.parser.MediaType;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Router {

    @Autowired
    private MyResourceHttpRequestHandler handler;

    @GetMapping("/")
    String home() {
        System.out.println("/");
        // return "Hello World!";
        return "index.jpg";
    }

    @GetMapping(value = "/visual_media/{mediaId}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] visual_media(@PathVariable String mediaId) throws IOException {
        System.out.println("ID: " + mediaId);

        InputStream in = getClass()
                .getResourceAsStream("/visual_media/" + mediaId + ".jpg");
        return IOUtils.toByteArray(in);
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
