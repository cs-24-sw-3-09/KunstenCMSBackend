package com.github.cs_24_sw_3_09.CMS.routing;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.cs_24_sw_3_09.CMS.DB.HikariCPDataSource;
import com.github.cs_24_sw_3_09.CMS.modelClasses.*;

import jakarta.validation.Valid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RouterPOST {

    @PostMapping("/api/display_devices")
    public String createDisplayDevice(@Valid @RequestBody DisplayDevice d) throws SQLException {
        System.out.println("POST /api/display_devices");

        Connection db = HikariCPDataSource.getConnection();

        try (PreparedStatement statement = db.prepareStatement(
                """
                          INSERT INTO display_devices(name, location, model, display_orientation, resolution, fallback_id, connected_state)
                          VALUES (?, ?, ?, ?, ?, ?, ?)
                        """)) {
            statement.setString(1, d.getName());
            statement.setString(2, d.getLocation());
            statement.setString(3, d.getModel());
            statement.setString(4, d.getDisplayOrientation());
            statement.setString(5, d.getResolution());
            // Check if fallbackId is null
            if (d.getFallbackId() != null) {
                statement.setInt(6, d.getFallbackId());
            } else {
                statement.setNull(6, java.sql.Types.INTEGER);
            }
            statement.setBoolean(7, d.getConnectedState());
            int rowsInserted = statement.executeUpdate();
        } catch (Exception e) {
            return "Display Device did not get added to the Database error: " + e.getMessage();
        }
        return "Display Device have been added to the Database";
    }
}
