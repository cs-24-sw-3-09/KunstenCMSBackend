package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface VisualMediaInclusionRepository extends CrudRepository<VisualMediaInclusionEntity, Integer>,
        PagingAndSortingRepository<VisualMediaInclusionEntity, Integer> {


        @Query("SELECT s FROM SlideshowEntity s JOIN s.visualMediaInclusionCollection v WHERE v.id = :visualMediaInclusionId")
        Optional<SlideshowEntity> findSlideshowByVisualMediaInclusionId(@Param("visualMediaInclusionId") Long visualMediaInclusionId);

        }
