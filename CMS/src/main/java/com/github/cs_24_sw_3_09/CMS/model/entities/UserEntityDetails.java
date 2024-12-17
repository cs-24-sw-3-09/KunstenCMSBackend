package com.github.cs_24_sw_3_09.CMS.model.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * The code in this class is inspired, taken or modified from the following:
 * https://www.geeksforgeeks.org/spring-boot-3-0-jwt-authentication-with-spring-security-using-mysql-database/ 
 */
public class UserEntityDetails implements UserDetails {

    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public UserEntityDetails(UserEntity userEntity) {
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.authorities = new ArrayList<>();
        // If user has certain roles, grant them the authority roles.
        if (userEntity.getAdmin()) this.authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (userEntity.getMediaPlanner()) this.authorities.add(new SimpleGrantedAuthority("ROLE_PLANNER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { return true; }
}
