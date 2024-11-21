package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DisplayDeviceRepository extends CrudRepository<DisplayDeviceEntity, Integer>,
                PagingAndSortingRepository<DisplayDeviceEntity, Integer> {

        // @EntityGraph(attributePaths = { "timeSlots" })
        // @Query("SELECT d FROM DisplayDeviceEntity d WHERE d.connectedState = true")
        // List<DisplayDeviceEntity> findConnectedDisplayDevices();
        // @Query("SELECT d FROM DisplayDeviceEntity d JOIN FETCH d.timeSlots WHERE
        // d.connectedState = true")
        // List<DisplayDeviceEntity> findConnectedDisplayDevices();
        // @Query("SELECT a from DisplayDeviceEntity a where a.connectedState = true")
        // Iterable<DisplayDeviceEntity> findConnectedDisplayDevices();

        // @EntityGraph(attributePaths = { "timeSlots" })
        @Query("SELECT d FROM DisplayDeviceEntity d WHERE d.connectedState = true")
        List<DisplayDeviceEntity> findConnectedDisplayDevices();
        //@Query("SELECT d FROM DisplayDeviceEntity d LEFT JOIN FETCH d.timeSlots WHERE d.connectedState = true")
        //List<DisplayDeviceEntity> findConnectedDisplayDevices();
}
