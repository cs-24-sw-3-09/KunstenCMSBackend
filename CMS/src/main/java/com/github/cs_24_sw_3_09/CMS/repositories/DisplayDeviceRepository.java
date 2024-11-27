package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DisplayDeviceRepository extends CrudRepository<DisplayDeviceEntity, Integer>,
                PagingAndSortingRepository<DisplayDeviceEntity, Integer> {


       @Query("SELECT DISTINCT dd FROM DisplayDeviceEntity dd " +
       "JOIN dd.fallbackContent f " +
       "JOIN SlideshowEntity ss ON f.id = ss.id " +
       "WHERE ss.id = :slideshowId")
       Set<DisplayDeviceEntity> findDisplayDevicesUsingSlideshowAsFallbackBySlideshowId(@Param("slideshowId") Long slideshowId);
}
