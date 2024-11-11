package com.github.cs_24_sw_3_09.CMS.routing;

//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Router {

    @GetMapping("/")
    String home() {
        System.out.println("/");
        // return "Hello World!";
        return "redirect:/index.jpg";
    }

    @GetMapping("/test")
    String test() {
        System.out.println("/test");
        // app.getModule().
        return "test123!";
    }

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
