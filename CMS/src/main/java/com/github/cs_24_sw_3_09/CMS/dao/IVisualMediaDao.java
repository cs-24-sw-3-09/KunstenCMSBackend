package com.github.cs_24_sw_3_09.CMS.dao;

import com.github.cs_24_sw_3_09.CMS.domain.VisualMedia;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface IVisualMediaDao {
    ResultSet create(VisualMedia VM);
    Optional<VisualMedia> findById(long id);
    List<VisualMedia> find();
}