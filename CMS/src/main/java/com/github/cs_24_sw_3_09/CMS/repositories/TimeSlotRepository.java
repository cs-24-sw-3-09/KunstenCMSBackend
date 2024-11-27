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

    /*@Query(value = "SELECT DISTINCT ts.* " +
               "FROM time_slots ts " +
               "INNER JOIN slideshows ss ON ss.id = ts.time_slot_content " +
               "WHERE ss.id = :slidehowId",
                nativeQuery = true)
    Set<TimeSlotEntity> findSetOfTimeSlotsBySlideshowId(@Param("slidehowId") Long slideshowId);*/

    @Query("SELECT DISTINCT ts FROM TimeSlotEntity ts " +
       "JOIN ts.displayContent dc " +
       "JOIN SlideshowEntity ss ON dc.id = ss.id " +
       "LEFT JOIN FETCH ts.displayDevices " +
       "WHERE ss.id = :slideshowId")
    Set<TimeSlotEntity> findSetOfTimeSlotsBySlideshowId(@Param("slideshowId") Long slideshowId);

    /*@Query(value = "SELECT DISTINCT ts.* " +
               "FROM time_slots ts " +
               "INNER JOIN contents c ON ts.time_slot_content = c.id " +
               "INNER JOIN slideshows ss ON c.id = ss.id " +
               "LEFT JOIN time_slot_display_device tsdd ON ts.id = tsdd.time_slot_id " +
               "LEFT JOIN display_devices dd ON dd.id = tsdd.display_device_id " +
               "WHERE ss.id = :slideshowId",
               nativeQuery = true)
            Set<TimeSlotEntity> findSetOfTimeSlotsBySlideshowId(@Param("slideshowId") Long slideshowId);*/
}