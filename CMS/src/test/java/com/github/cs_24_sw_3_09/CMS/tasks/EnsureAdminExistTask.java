package com.github.cs_24_sw_3_09.CMS.tasks;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.github.cs_24_sw_3_09.CMS.services.UserService;

@Component
public class EnsureAdminExistTask {

    private final UserService userService;

    public EnsureAdminExistTask(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void ensureAdminExist() {
        /*if(userService.existsByAdmin()) {
            System.out.println("Admin already in db.s");
            return;
        }*/
        // Create admin user
        /*UserEntity adminEntity = UserEntity.builder()
        .email("admin@kunsten.dk")
        .password(new BCryptPasswordEncoder().encode("admin123"))
        .admin(true)
        .build();
        userService.save(adminEntity);*/
        System.out.println("Admin user created");
    }
    
}
