package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VisualMediaRepository extends CrudRepository<VisualMediaEntity, Integer>,
        PagingAndSortingRepository<VisualMediaEntity, Integer> {
}
