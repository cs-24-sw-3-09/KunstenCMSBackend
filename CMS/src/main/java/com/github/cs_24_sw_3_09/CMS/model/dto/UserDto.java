package com.github.cs_24_sw_3_09.CMS.model.dto;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    protected Integer id;
    @Size(min = 1, max = 50, message = "a first name must be between 1 and 50 characters")
    private String firstName;
    @Size(min = 1, max = 50, message = "a last name must be between 1 and 50 characters")
    private String lastName;
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z0-9.-]+$", message = "Invalid email format")
    @Column(unique = true)
    private String email;
    @Size(min = 8, message = "a password must be at least 8 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @DateTimeFormat(pattern = "yyyy-mm-dd") // If the numbers is not right, it is converted into something valid
    private Date pauseNotificationStart;
    @DateTimeFormat(pattern = "yyyy-mm-dd") // If the numbers is not right, it is converted into something valid
    private Date pauseNotificationEnd;
    private Boolean notificationState;
    private Boolean mediaPlanner;
    private Boolean admin;

}