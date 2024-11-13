package com.github.cs_24_sw_3_09.CMS.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteSingleObj {
    static public boolean deleteById(int id, String tableName) throws SQLException {
        Connection db = HikariCPDataSource.getConnection();
        String updateQuery = "DELETE FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement statement = db.prepareStatement(updateQuery)) {
            statement.setInt(1, id); // Set the id for the WHERE clause

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0; // Return true if update was successful, otherwise false
        } catch (Exception e) {
            System.err.println("Error deleteing " + tableName + ": " + e.getMessage());
            return false; // Return false if an error occurred
        }
    }
}
