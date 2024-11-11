package com.github.cs_24_sw_3_09.CMS.routing;

//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class Router {

    @GetMapping("/")
    String home() {
        System.out.println("Hello");
        return "Hello World!";
    }

    @GetMapping("/test")
    String test() {
        System.out.println("Hello");
        return "test123!";
    }
}
