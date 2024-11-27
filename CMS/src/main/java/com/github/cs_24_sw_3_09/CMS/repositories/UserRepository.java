package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer>,
        PagingAndSortingRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByAdmin(boolean admin);
}
