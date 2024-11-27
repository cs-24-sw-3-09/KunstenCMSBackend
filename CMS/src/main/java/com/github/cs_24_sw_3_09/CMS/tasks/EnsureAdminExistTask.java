package com.github.cs_24_sw_3_09.CMS.tasks;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.services.UserService;

@Component
public class EnsureAdminExistTask {

    private final UserService userService;

    public EnsureAdminExistTask(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ensureAdminExist() {
        if(userService.existsByAdmin()) {
            return;
        }
        // Create admin user
        UserEntity adminEntity = UserEntity.builder()
        .email("admin@kunsten.dk")
        .password(new BCryptPasswordEncoder().encode("admin123"))
        .admin(true)
        .mediaPlanner(false)
        .firstName("Admin")
        .lastName("Admin")
        .notificationState(false)
        .build();
        userService.save(adminEntity);
        System.out.println("Admin user created");
    }
    
}
