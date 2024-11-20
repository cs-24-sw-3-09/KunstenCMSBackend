package com.github.cs_24_sw_3_09.CMS.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.cs_24_sw_3_09.CMS.model.entities.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}
