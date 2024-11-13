package com.github.cs_24_sw_3_09.CMS.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.github.cs_24_sw_3_09.CMS.modelClasses.DisplayDevice;

public class UpdateSingleObj {
    static public boolean updateDisplayDeviceById(int id, DisplayDevice updatedDevice) throws SQLException {
        Connection db = HikariCPDataSource.getConnection();
        String updateQuery = """
                UPDATE display_devices
                SET name = ?, location = ?, model = ?, display_orientation = ?, resolution = ?, fallback_id = ?, connected_state = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = db.prepareStatement(updateQuery)) {
            statement.setString(1, updatedDevice.getName());
            statement.setString(2, updatedDevice.getLocation());
            statement.setString(3, updatedDevice.getModel());
            statement.setString(4, updatedDevice.getDisplayOrientation());
            statement.setString(5, updatedDevice.getResolution());

            // Check if fallbackId is null
            if (updatedDevice.getFallbackId() != null) {
                statement.setInt(6, updatedDevice.getFallbackId());
            } else {
                statement.setNull(6, java.sql.Types.INTEGER);
            }

            statement.setBoolean(7, updatedDevice.getConnectedState());
            statement.setInt(8, id); // Set the id for the WHERE clause

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0; // Return true if update was successful, otherwise false
        } catch (Exception e) {
            System.err.println("Error updating display device: " + e.getMessage());
            return false; // Return false if an error occurred
        }
    }
}
