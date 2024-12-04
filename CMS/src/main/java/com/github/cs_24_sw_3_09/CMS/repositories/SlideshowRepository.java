package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideshowRepository extends CrudRepository<SlideshowEntity, Integer>,
        PagingAndSortingRepository<SlideshowEntity, Integer>{
                
                @Query("SELECT DISTINCT ss.id FROM SlideshowEntity ss")
                List<Integer> getAllSlideshowIds();

                //Find all slideshows that have a visual media with the given id 
                @Query("SELECT DISTINCT ss FROM SlideshowEntity ss " +
                        "JOIN ss.visualMediaInclusionCollection vmi " +
                        "JOIN vmi.visualMedia vm " +
                        "WHERE vm.id = :visualMediaId")
                 Set<SlideshowEntity> findSlideshowsByVisualMediaId(@Param("visualMediaId") Long visualMediaId);

                
        }
