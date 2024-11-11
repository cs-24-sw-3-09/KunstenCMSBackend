package com.github.cs_24_sw_3_09.CMS.routing;

//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Router {

    @GetMapping("/")
    String home() {
        System.out.println("/");
        // return "Hello World!";
        return "helloWorld";
    }

    @GetMapping("/test")
    String test() {
        System.out.println("/test");
        // app.getModule().
        return "test123!";
    }
}
