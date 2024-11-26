package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideshowRepository extends CrudRepository<SlideshowEntity, Integer>,
        PagingAndSortingRepository<SlideshowEntity, Integer>{
                
                //Find all slideshows that have a visual media with the given id 
                @Query(value = "SELECT DISTINCT ss.* " +
               "FROM slideshows ss " +
               "INNER JOIN visual_media_inclusion vmi ON ss.id = vmi.slideshow_id " +
               "INNER JOIN visual_medias vm ON vmi.visual_media_id = vm.id " +
               "WHERE vm.id = :visualMediaId",
                nativeQuery = true)
                Set<SlideshowEntity> findSlideshowsByVisualMediaId(@Param("visualMediaId") Long visualMediaId);
        }
