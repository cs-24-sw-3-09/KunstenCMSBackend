package com.github.cs_24_sw_3_09.CMS.routing;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.cs_24_sw_3_09.CMS.DB.GetSingleObj;
import com.github.cs_24_sw_3_09.CMS.DB.HikariCPDataSource;
import com.github.cs_24_sw_3_09.CMS.DB.UpdateSingleObj;
import com.github.cs_24_sw_3_09.CMS.modelClasses.*;

import jakarta.validation.Valid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// https://www.baeldung.com/spring-rest-json-patch
@RestController
public class RouterPATCH {

    private DisplayDevice applyPatchToDisplayDevice(DisplayDevice patch, DisplayDevice targetDisplayDevice)
            throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("part 0");
        JsonPatch patchJson = objectMapper.convertValue(patch, JsonPatch.class); // Converts from dd class into
                                                                                 // JsonPatch class
        System.out.println("part 1");
        JsonNode patched = patchJson.apply(objectMapper.convertValue(targetDisplayDevice, JsonNode.class));
        System.out.println("part 2");
        return objectMapper.treeToValue(patched, DisplayDevice.class);
    }

    @PatchMapping(path = "/api/display_devices/{id}", consumes = "application/json-patch+json")
    public String updateDisplayDevice(@PathVariable int id, @RequestBody DisplayDevice patch)
            throws SQLException {
        System.out.println("PATCH /api/display_devices/{id}");

        Connection db = HikariCPDataSource.getConnection();
        try {
            System.out.println("Før db læsnigs");
            DisplayDevice dd = GetSingleObj.getDisplayDeviceById(id);
            System.out.println("Efter db læsning");
            if (dd == null) {
                throw new Error();
            }
            DisplayDevice ddPatched = applyPatchToDisplayDevice(patch, dd);
            System.out.println("patched");
            if (UpdateSingleObj.updateDisplayDeviceById(id, ddPatched)) {
                System.out.println("Got updated");
            } else {
                System.out.println("Did not update");
            }
        } catch (JsonPatchException | JsonProcessingException e) {
            return ("something went wrong with JSON formatting: " + e.getMessage());
        }
        return "hej";
    }
}
