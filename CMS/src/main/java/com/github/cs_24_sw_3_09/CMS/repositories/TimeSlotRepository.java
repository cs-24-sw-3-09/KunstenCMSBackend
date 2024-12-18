package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import jakarta.transaction.Transactional;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("SELECT ts FROM TimeSlotEntity ts WHERE ts.displayContent.type = 'slideshow'  AND ts.startDate = CURRENT_DATE  AND ts.startTime > :currentTime")
    Set<TimeSlotEntity> getAllTimeSlotsWithSSWithCurrentDateButFutureTime(@Param("currentTime") LocalTime currentTime);


}
