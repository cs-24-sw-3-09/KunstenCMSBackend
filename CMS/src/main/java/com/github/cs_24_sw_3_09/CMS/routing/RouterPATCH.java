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

    // Genreal method to take JsonPatch and change them into targetObj
    private <T> T applyPatchToObj(JsonPatch patch, T targetObj, Class<T> targetClass)
            throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // convert targetObj from object to JsonNode, and apply the patch
        JsonNode patched = patch.apply(objectMapper.convertValue(targetObj, JsonNode.class));
        // convert from JsonNode to object again
        return objectMapper.treeToValue(patched, targetClass);
    }

    @PatchMapping(path = "/api/display_devices/{id}")
    public String updateDisplayDevice(@PathVariable int id, @RequestBody JsonPatch patch)
            throws SQLException {
        System.out.println("PATCH /api/display_devices/" + id);
        try {
            DisplayDevice dd = GetSingleObj.getDisplayDeviceById(id);
            if (dd == null) {
                throw new Error();
            }
            DisplayDevice ddPatched = applyPatchToObj(patch, dd, DisplayDevice.class);
            UpdateSingleObj.updateDisplayDeviceById(id, ddPatched);
            return "Display Device with id " + id + " got updated";
        } catch (JsonPatchException | JsonProcessingException e) {
            return ("something went wrong with JSON formatting: " + e.getMessage());
        }
    }
}
