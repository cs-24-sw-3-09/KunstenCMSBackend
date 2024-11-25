package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SlideshowRepository extends CrudRepository<SlideshowEntity, Integer>,
        PagingAndSortingRepository<SlideshowEntity, Integer>{
                @Query("SELECT DISTINCT ss FROM SlideshowEntity ss INNER JOIN ss.visualMediaInclusionCollection vmi INNER JOIN vmi.visualMedia vm")
                List<SlideshowEntity> findSlideshowsViaVisualMedia();
        }
