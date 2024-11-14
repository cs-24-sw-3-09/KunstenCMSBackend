package com.github.cs_24_sw_3_09.CMS.Controllers;


import com.github.cs_24_sw_3_09.CMS.services.IDisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.IVisualMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.sql.SQLException;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.github.cs_24_sw_3_09.CMS.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;


import jakarta.validation.Valid;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.cs_24_sw_3_09.CMS.model.*;

import jakarta.validation.Valid;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/display_devices")
public class DisplayDeviceController {

    private final IDisplayDeviceService displayDeviceService;

    @Autowired
    public DisplayDeviceController(IDisplayDeviceService displayDeviceService) {
        this.displayDeviceService = displayDeviceService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDisplayDevice(@PathVariable int id) throws SQLException {
        System.out.println("DELETE /api/display_devices/" + id);
        displayDeviceService.deleteDisplayDevice(id);
        return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
    }
/*

    @GetMapping("/")
    List<DisplayDevice> allDisplayDevices() throws SQLException {
        System.out.println("GET /api/display_devices");
        List<DisplayDevice> ddList = GetAllObj.buildDisplayDeviceAll();
        return ddList;
    }

    @GetMapping("/{id}")
    DisplayDevice getsingleDisplayDevice(@PathVariable int id) throws SQLException {
        System.out.println("GET /api/display_devices/" + id);
        DisplayDevice dd = GetSingleObj.buildDisplayDeviceById(id);
        return dd;
    }

    @PatchMapping("/{id}")
    public String updateDisplayDevice(@PathVariable int id, @Valid @RequestBody DisplayDevice patch) {
        System.out.println("PATCH /api/display_devices/" + id);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DisplayDevice dd = GetSingleObj.buildDisplayDeviceById(id);
            if (dd == null) {
                throw new Error();
            }
            JsonPatch ddtest = objectMapper.readValue(toJSONPatch(patch), JsonPatch.class);
            DisplayDevice ddPatched = applyPatchToObj(ddtest, dd, DisplayDevice.class);
            PatchSingleObj.patchDisplayDeviceById(id, ddPatched);
            return "Display Device with id " + id + " got updated";
        } catch (JsonPatchException | JsonProcessingException e) {
            return ("something went wrong with JSON formatting: " + e.getMessage());
        } catch (Error | SQLException e) {
            return ("could not post: " + e.getMessage());
        }
    }

    @PostMapping("/api/display_devices")
    public String createDisplayDevice(@Valid @RequestBody DisplayDevice dd) throws SQLException {
        System.out.println("POST /api/display_devices");
        try {
            return PostSingleObj.postDisplayDevice(dd);
        } catch (Exception e) {
            return "could not post: " + e.getMessage();
        }
    }

 */
}