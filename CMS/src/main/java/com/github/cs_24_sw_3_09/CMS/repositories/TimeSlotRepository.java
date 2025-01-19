package com.github.cs_24_sw_3_09.CMS.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

@Repository
public interface TimeSlotRepository extends CrudRepository<TimeSlotEntity, Integer>,
        PagingAndSortingRepository<TimeSlotEntity, Integer> {


    @Query("SELECT ts FROM TimeSlotEntity ts WHERE ts.displayDevices IS EMPTY")
    List<TimeSlotEntity> findTimeSlotsWithNoDisplayDevices();

    @Query("SELECT ts FROM TimeSlotEntity ts " +
            "WHERE ts.startDate <= :ts_endDate " +
            "AND ts.endDate >= :ts_startDate")
    List<TimeSlotEntity> findAllInTimeFrame(@Param("ts_startDate") Date start,
            @Param("ts_endDate") Date end);

    @Query("SELECT DISTINCT ts FROM TimeSlotEntity ts " +
       "JOIN ts.displayContent dc " +
       "JOIN SlideshowEntity ss ON dc.id = ss.id " +
       "LEFT JOIN FETCH ts.displayDevices " +
       "WHERE ss.id = :slideshowId")
    Set<TimeSlotEntity> findSetOfTimeSlotsBySlideshowId(@Param("slideshowId") Long slideshowId);

    @Query("SELECT DISTINCT ts FROM TimeSlotEntity ts " +
           "JOIN ts.displayContent c " +
            "JOIN SlideshowEntity ss ON c.id = ss.id ")
    List<TimeSlotEntity> getAllTimeSlotsWithSlideshowAsContent();

    @Query( "SELECT ts FROM TimeSlotEntity ts JOIN ts.displayDevices dd WHERE dd.id = :displayDeviceId")
    Set<TimeSlotEntity>findSetOfTimeSlotsByDisplayDeviceId(@Param("displayDeviceId") Long displayDeviceId);

}
