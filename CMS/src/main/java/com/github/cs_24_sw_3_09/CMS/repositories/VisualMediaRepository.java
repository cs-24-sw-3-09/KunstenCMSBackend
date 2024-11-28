package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisualMediaRepository extends CrudRepository<VisualMediaEntity, Integer>,
        PagingAndSortingRepository<VisualMediaEntity, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM visual_media_tag WHERE tag_id = :tagId and visual_media_id = :vmId", nativeQuery = true)
    void deleteAssociation(@Param("vmId") Long vmId, @Param("tagId") Long tagId);


    @Query(value = "SELECT dd FROM DisplayDeviceEntity dd JOIN dd.fallbackContent vm WHERE vm.id = :id")
    List<DisplayDeviceEntity> getDisplayDevicesPartOfVisualMedia(@Param("id") Long id);

    @Query(value = "SELECT ts FROM TimeSlotEntity ts JOIN ts.displayContent dc WHERE dc.id = :id")
    List<TimeSlotEntity> getTimeslotsPartOfVisualMedia(@Param("id") Long id);
}
