package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DisplayDeviceRepository extends CrudRepository<DisplayDeviceEntity, Integer>,
                PagingAndSortingRepository<DisplayDeviceEntity, Integer> {

        @Query("SELECT a from DisplayDeviceEntity a where a.connectedState = true")
        Iterable<DisplayDeviceEntity> findConnectedDisplayDevices();
}
