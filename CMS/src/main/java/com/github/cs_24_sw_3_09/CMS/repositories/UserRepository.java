package com.github.cs_24_sw_3_09.CMS.repositories;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer>,
        PagingAndSortingRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByAdmin(boolean admin);

    @Query("SELECT DISTINCT user FROM UserEntity user " +
            "WHERE user.notificationState = true")
    Set<UserEntity> findUserWithNotificationsEnabled();
}
