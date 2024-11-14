package com.github.cs_24_sw_3_09.CMS.dao.daoImpl;

import com.github.cs_24_sw_3_09.CMS.dao.HikariCPDataSource;
import com.github.cs_24_sw_3_09.CMS.dao.IVisualMediaDao;
import com.github.cs_24_sw_3_09.CMS.domain.VisualMedia;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class VisualMediaDaoImpl implements IVisualMediaDao {

    private final Connection db;

    public VisualMediaDaoImpl() throws SQLException {
        this.db = HikariCPDataSource.getConnection();
    }

    @Override
    public ResultSet create(VisualMedia VM) {
        String query = "SELECT * FROM display_devices";


        try (PreparedStatement statement = db.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (Exception e) {
            System.err.println("Error retrieving all objects: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<VisualMedia> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<VisualMedia> find() {
        return List.of();
    }
}
