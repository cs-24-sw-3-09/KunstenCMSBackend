package com.github.cs_24_sw_3_09.CMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import com.github.cs_24_sw_3_09.CMS.modelClasses.DisplayDevice;

public class Main {
    @Autowired
    private static DataSource dataSource;

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(CmsApplication.class, args); // Start springboot server

        // **Example of how the DD builder works**
        // DisplayDevice.DisplayDeviceBuilder ddBuilder = new
        // DisplayDevice.DisplayDeviceBuilder();
        // ddBuilder.setName("hej");
        // DisplayDevice d = ddBuilder.getDisplayDevice();
        // System.out.println(d);

        // Connection db =
        // DriverManager.getConnection("jdbc:mariadb://5.9.123.164:3305/kunsten",
        // "kunsten",
        // "kunsten123");

        // try (PreparedStatement statement = db.prepareStatement("""
        // SELECT *
        // FROM display_devices
        // """)) {
        // ResultSet resultSet = statement.executeQuery();
        // while (resultSet.next()) {
        // String val1 = resultSet.getString("name"); // by column index
        // // int val2 = resultSet.getInt("column2"); // by column name
        // // ... use val1 and val2 ...
        // System.out.println(val1);
        // }
        // }
        // db.close();

        Connection db = HikariCPDataSource.getConnection();
        try (PreparedStatement statement = db.prepareStatement("""
                    SELECT *
                    FROM display_devices
                """)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String val1 = resultSet.getString("name"); // by column index
                // int val2 = resultSet.getInt("column2"); // by column name
                // ... use val1 and val2 ...
                System.out.println(val1);
            }
        }
        db.close();
    }
}
