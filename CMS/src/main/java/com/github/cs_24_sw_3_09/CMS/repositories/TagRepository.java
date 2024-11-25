package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.TagEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<TagEntity, Long>,
        PagingAndSortingRepository<TagEntity, Long> {


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM visual_media_tag WHERE tag_id = :tagId", nativeQuery = true)
    void deleteAssociations(@Param("tagId") Long tagId);
}
