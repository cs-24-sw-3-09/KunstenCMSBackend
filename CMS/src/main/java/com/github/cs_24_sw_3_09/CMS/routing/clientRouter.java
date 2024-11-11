package com.github.cs_24_sw_3_09.CMS.routing;

//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class clientRouter {

    @GetMapping("/")
    String home() {
        System.out.println("Hello");
        return "Hello World!";
    }
}
