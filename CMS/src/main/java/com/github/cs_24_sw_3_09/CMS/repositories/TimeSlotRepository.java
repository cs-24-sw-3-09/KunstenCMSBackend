package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends CrudRepository<TimeSlotEntity, Integer>,
    PagingAndSortingRepository<TimeSlotEntity, Integer>{

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM time_slot_display_device WHERE time_slot_id = :ts_id and display_device_id = :dd_id", nativeQuery = true)
    void deleteAssociation(@Param("ts_id")Long ts_id, @Param("dd_id") Long dd_id);

    @Transactional
    @Query(value = "SELECT COUNT(*) FROM time_slot_display_device WHERE time_slot_id = :ts_id", nativeQuery = true)
    int countAssociations(@Param("ts_id") Long ts_id);

    @Query("SELECT ts FROM TimeSlotEntity ts WHERE ts.displayDevices IS EMPTY")
    List<TimeSlotEntity> findTimeSlotsWithNoDisplayDevices();

    @Query(value = "SELECT DISTINCT ts.* " +
               "FROM time_slots ts " +
               "INNER JOIN slideshow ss ON ts.id = ss.time_slots.id " +
               "WHERE ts.id = :timeSlotId",
                nativeQuery = true)
    Set<TimeSlotEntity> findSetOfTimeSlotsBySlideshowId(Long timeSlotId);

}