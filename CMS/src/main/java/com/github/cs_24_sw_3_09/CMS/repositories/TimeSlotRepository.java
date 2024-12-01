package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends CrudRepository<TimeSlotEntity, Integer>,
    PagingAndSortingRepository<TimeSlotEntity, Integer>{

    @Query("SELECT ts FROM TimeSlotEntity ts WHERE ts.displayDevices IS EMPTY")
    List<TimeSlotEntity> findTimeSlotsWithNoDisplayDevices();

    @Query("SELECT ts FROM TimeSlotEntity ts " +
            "WHERE ts.startDate <= :ts_endDate " +
            "AND ts.endDate >= :ts_startDate")
    List<TimeSlotEntity> findAllInTimeFrame(@Param("ts_startDate") Date start,
            @Param("ts_endDate") Date end);
}