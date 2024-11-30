package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.sql.Date;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    protected Integer id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private Date pauseNotificationStart;
    private Date pauseNotificationEnd;
    @ColumnDefault("false")
    private Boolean notificationState;
    @ColumnDefault("false")
    private Boolean mediaPlanner;
    @ColumnDefault("false")
    private Boolean admin;

}