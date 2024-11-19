package com.github.cs_24_sw_3_09.CMS.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotDisplayDeviceEntity;

@Repository
public interface TimeSlotDisplayDeviceRepository extends CrudRepository<TimeSlotDisplayDeviceEntity, Integer>,
    PagingAndSortingRepository<TimeSlotDisplayDeviceEntity, Integer>{    
}