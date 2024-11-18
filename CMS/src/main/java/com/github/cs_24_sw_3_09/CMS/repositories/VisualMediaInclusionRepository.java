package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaInclusionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VisualMediaInclusionRepository extends CrudRepository<VisualMediaInclusionEntity, Integer>,
        PagingAndSortingRepository<VisualMediaInclusionEntity, Integer> {}
