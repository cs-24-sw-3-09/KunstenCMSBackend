package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.SlideshowEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SlideshowRepository extends CrudRepository<SlideshowEntity, Integer>,
        PagingAndSortingRepository<SlideshowEntity, Integer> {}
