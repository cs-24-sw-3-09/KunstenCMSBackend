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

    @Query(value = "SELECT dd FROM DisplayDeviceEntity dd JOIN dd.fallbackContent vm WHERE vm.id = :id")
    List<DisplayDeviceEntity> getDisplayDevicesPartOfVisualMedia(@Param("id") Long id);

    @Query("SELECT ts FROM TimeSlotEntity ts JOIN ts.displayContent dc WHERE dc.id = :id UNION SELECT ts FROM TimeSlotEntity ts JOIN ts.displayContent dc JOIN dc.visualMediaInclusionCollection vmic JOIN vmic.visualMedia vm WHERE vm.id = :id")
    List<TimeSlotEntity> getTimeslotsPartOfVisualMedia(@Param("id") Long id);

    @Query("SELECT vm.id FROM VisualMediaEntity vm")
    List<Integer> getAllVisualMediaIds();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM visual_media_tag WHERE visual_media_id = :id", nativeQuery = true)
    void deleteVisualMediaTags(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM visual_medias WHERE id = :id", nativeQuery = true)
    void deleteVisualMedia(@Param("id") Integer id);
}
