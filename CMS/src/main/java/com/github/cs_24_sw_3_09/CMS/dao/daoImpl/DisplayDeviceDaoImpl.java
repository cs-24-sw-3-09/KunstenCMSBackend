package com.github.cs_24_sw_3_09.CMS.dao.daoImpl;

import com.github.cs_24_sw_3_09.CMS.dao.HikariCPDataSource;
import com.github.cs_24_sw_3_09.CMS.dao.IDisplayDeviceDao;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class DisplayDeviceDaoImpl implements IDisplayDeviceDao {

    private final Connection db;

    public DisplayDeviceDaoImpl() throws SQLException {
        this.db = HikariCPDataSource.getConnection();
    }

    @Override
    public void delete(int id) {

        String updateQuery = "DELETE FROM display_devices WHERE id = ?";

        try (PreparedStatement statement = db.prepareStatement(updateQuery)) {
            statement.setInt(1, id); // Set the id for the WHERE clause

            statement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error deleteing display_devices: " + e.getMessage());
        }

         
    }
}
