package com.github.cs_24_sw_3_09.CMS.routing;

//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.cs_24_sw_3_09.CMS.modelClasses.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RouterPOST {

    @PostMapping("/api/display_devices")
    public String createPerson(@RequestBody DisplayDevice d) {
        // TODO: make validation
        System.out.println("Got post");
        System.out.println(d.getName());
        return "Received device with name: " + d.getName();
    }
}
