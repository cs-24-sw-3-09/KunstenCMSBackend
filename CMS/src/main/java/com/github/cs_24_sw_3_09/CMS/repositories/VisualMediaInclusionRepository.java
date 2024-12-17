package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface VisualMediaInclusionRepository extends CrudRepository<VisualMediaInclusionEntity, Integer>,
                PagingAndSortingRepository<VisualMediaInclusionEntity, Integer> {

        List<VisualMediaInclusionEntity> findAllByVisualMedia(VisualMediaEntity visualMedia);

        @Query("SELECT vmic FROM SlideshowEntity ss JOIN ss.visualMediaInclusionCollection vmic WHERE ss.id = :slideshowId")
        Set<VisualMediaInclusionEntity> findAllVisualMediaInclusionForSlideshow(@Param("slideshowId") Long slideshowId);

        @Modifying
        @Query("UPDATE VisualMediaInclusionEntity vmi " +
                        "SET vmi.slideshowPosition = vmi.slideshowPosition - 1 " +
                        "WHERE vmi.slideshowPosition > :deletedPosition " +
                        "AND vmi IN (" +
                        "  SELECT v FROM SlideshowEntity s JOIN s.visualMediaInclusionCollection v WHERE s.id = :slideshowId"
                        +
                        ")")
        void updatePositionsAfterDeletion(@Param("slideshowId") Long slideshowId,
                        @Param("deletedPosition") Integer deletedPosition);

}
