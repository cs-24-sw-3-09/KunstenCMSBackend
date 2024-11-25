package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisualMediaRepository extends CrudRepository<VisualMediaEntity, Integer>,
        PagingAndSortingRepository<VisualMediaEntity, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM visual_media_tag WHERE tag_id = :tagId and visual_media_id = :vmId", nativeQuery = true)
    void deleteAssociation(@Param("vmId")Long vmId, @Param("tagId") Long tagId);
}
