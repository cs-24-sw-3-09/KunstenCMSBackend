package com.github.cs_24_sw_3_09.CMS.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.cs_24_sw_3_09.CMS.modelClasses.DisplayDevice;

public class GetSingleObj {
    static public DisplayDevice getDisplayDeviceById(int id) throws SQLException {
        Connection db = HikariCPDataSource.getConnection();
        DisplayDevice dd = null;
        DisplayDevice.DisplayDeviceBuilder ddBuilder = new DisplayDevice.DisplayDeviceBuilder();

        String query = """
                SELECT name, location, model, display_orientation, resolution, fallback_id, connected_state
                FROM display_devices
                WHERE id = ?
                """;

        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ddBuilder.setName(resultSet.getString("name"));
                    ddBuilder.setLocation(resultSet.getString("location"));
                    ddBuilder.setModel(resultSet.getString("model"));
                    ddBuilder.setDisplayOrientation(resultSet.getString("display_orientation"));
                    ddBuilder.setResolution(resultSet.getString("resolution"));
                    ddBuilder.setFallbackId(
                            resultSet.getObject("fallback_id") != null ? resultSet.getInt("fallback_id") : null);
                    ddBuilder.setConnectedState(resultSet.getBoolean("connected_state"));
                    ddBuilder.setId(id);
                    dd = ddBuilder.getDisplayDevice();
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving display device: " + e.getMessage());
            return null;
        }
        System.out.println(dd.toJSON());
        return dd;
    }
}
