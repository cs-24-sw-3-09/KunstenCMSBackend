package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DisplayDeviceRepository extends CrudRepository<DisplayDeviceEntity, Integer>,
                PagingAndSortingRepository<DisplayDeviceEntity, Integer> {

        @Query("SELECT dd FROM DisplayDeviceEntity dd WHERE dd.connectedState = true")
        List<DisplayDeviceEntity> findConnectedDisplayDevices();

        @Modifying
        @Query("UPDATE DisplayDeviceEntity dd " +
                        "SET dd.connectedState = :connectedState " +
                        "WHERE dd.id = :id " +
                        "AND dd.connectedState <> :connectedState")
        int updateConnectedStateById(@Param("id") Integer id, @Param("connectedState") Boolean connectedState);

}
