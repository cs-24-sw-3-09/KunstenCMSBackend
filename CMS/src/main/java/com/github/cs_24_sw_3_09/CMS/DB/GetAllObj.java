package com.github.cs_24_sw_3_09.CMS.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.github.cs_24_sw_3_09.CMS.modelClasses.DisplayDevice;

public class GetAllObj {
    static private ResultSet getObjAll(String tableName) throws SQLException {
        Connection db = HikariCPDataSource.getConnection();

        String query = "SELECT * FROM " + tableName;

        try (PreparedStatement statement = db.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet;
            }
        } catch (Exception e) {
            System.err.println("Error retrieving all objects: " + e.getMessage());
            return null;
        }
    }

    static public List<DisplayDevice> buildDisplayDeviceAll() throws SQLException {
        ArrayList<DisplayDevice> ddList = new ArrayList<DisplayDevice>();

        try (ResultSet resultSet = getObjAll("display_devices")) {
            while (resultSet.next()) {
                DisplayDevice.DisplayDeviceBuilder ddBuilder = new DisplayDevice.DisplayDeviceBuilder();
                ddBuilder.setName(resultSet.getString("name"));
                ddBuilder.setLocation(resultSet.getString("location"));
                ddBuilder.setModel(resultSet.getString("model"));
                ddBuilder.setDisplayOrientation(resultSet.getString("display_orientation"));
                ddBuilder.setResolution(resultSet.getString("resolution"));
                ddBuilder.setFallbackId(
                        resultSet.getObject("fallback_id") != null ? resultSet.getInt("fallback_id") : null);
                ddBuilder.setConnectedState(resultSet.getBoolean("connected_state"));
                ddBuilder.setId(resultSet.getInt("id"));
                ddList.add(ddBuilder.getDisplayDevice());
            }
        } catch (Exception e) {
            System.err.println("Error retrieving display device: " + e.getMessage());
            return null;
        }
        return ddList;
    }
}
