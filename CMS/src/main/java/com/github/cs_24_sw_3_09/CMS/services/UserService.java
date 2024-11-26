package com.github.cs_24_sw_3_09.CMS.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserEntity save(UserEntity user);

    List<UserEntity> findAll();

    Page<UserEntity> findAll(Pageable pageable);

    Optional<UserEntity> findOne(Long id);

    boolean isExists(Long id);

    UserEntity partialUpdate(Long id, UserEntity userEntity);

    void delete(Long id);

    boolean existsByAdmin();

}