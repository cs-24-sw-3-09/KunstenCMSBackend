package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface VisualMediaInclusionRepository extends CrudRepository<VisualMediaInclusionEntity, Integer>,
        PagingAndSortingRepository<VisualMediaInclusionEntity, Integer> {

                @Query("SELECT vmic FROM SlideshowEntity ss JOIN ss.visualMediaInclusionCollection vmic WHERE ss.id = :slideshowId")
                 Set<VisualMediaInclusionEntity> findAllVisualMediaInclusionForSlideshow(@Param("slideshowId") Long slideshowId);
        }
