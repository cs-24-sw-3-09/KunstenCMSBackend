package com.github.cs_24_sw_3_09.CMS;

import com.github.cs_24_sw_3_09.CMS.dao.HikariCPDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class testHikari {
    public static void main(String[] args) {
        try (Connection connection = HikariCPDataSource.getConnection()) {
            System.out.println("Successfully connected to the database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
