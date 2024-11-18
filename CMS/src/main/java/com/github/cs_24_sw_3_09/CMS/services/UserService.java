package com.github.cs_24_sw_3_09.CMS.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;

public interface UserService {
    UserEntity save(UserEntity user);

    List<UserEntity> findAll();

    Page<UserEntity> findAll(Pageable pageable);
}