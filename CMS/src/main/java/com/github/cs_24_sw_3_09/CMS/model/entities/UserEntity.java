package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.sql.Date;

import org.hibernate.annotations.ColumnDefault;

import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
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
    private boolean notificationState;
    @ColumnDefault("false")
    private boolean mediaPlanner;
    @ColumnDefault("false")
    private boolean admin;

    // No-argument constructor
    public UserEntity() {
    }

    // All-arguments constructor
    public UserEntity(Integer id, String firstName, String lastName, String email, String password,
            Date pauseNotificationStart, Date pauseNotificationEnd, boolean notificationState,
            boolean mediaPlanner, boolean admin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.pauseNotificationStart = pauseNotificationStart;
        this.pauseNotificationEnd = pauseNotificationEnd;
        this.notificationState = notificationState;
        this.mediaPlanner = mediaPlanner;
        this.admin = admin;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getPauseNotificationStart() {
        return pauseNotificationStart;
    }

    public void setPauseNotificationStart(Date pauseNotificationStart) {
        this.pauseNotificationStart = pauseNotificationStart;
    }

    public Date getPauseNotificationEnd() {
        return pauseNotificationEnd;
    }

    public void setPauseNotificationEnd(Date pauseNotificationEnd) {
        this.pauseNotificationEnd = pauseNotificationEnd;
    }

    public boolean isNotificationState() {
        return notificationState;
    }

    public void setNotificationState(boolean notificationState) {
        this.notificationState = notificationState;
    }

    public boolean isMediaPlanner() {
        return mediaPlanner;
    }

    public void setMediaPlanner(boolean mediaPlanner) {
        this.mediaPlanner = mediaPlanner;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    // Builder Pattern (manual implementation)
    public static class Builder {
        private Integer id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private Date pauseNotificationStart;
        private Date pauseNotificationEnd;
        private boolean notificationState;
        private boolean mediaPlanner;
        private boolean admin;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setPauseNotificationStart(Date pauseNotificationStart) {
            this.pauseNotificationStart = pauseNotificationStart;
            return this;
        }

        public Builder setPauseNotificationEnd(Date pauseNotificationEnd) {
            this.pauseNotificationEnd = pauseNotificationEnd;
            return this;
        }

        public Builder setNotificationState(boolean notificationState) {
            this.notificationState = notificationState;
            return this;
        }

        public Builder setMediaPlanner(boolean mediaPlanner) {
            this.mediaPlanner = mediaPlanner;
            return this;
        }

        public Builder setAdmin(boolean admin) {
            this.admin = admin;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(id, firstName, lastName, email, password, pauseNotificationStart, pauseNotificationEnd,
                    notificationState, mediaPlanner, admin);
        }
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", pause_notification_start=" + pauseNotificationStart +
                ", pause_notification_end=" + pauseNotificationEnd +
                ", notification_state=" + notificationState +
                ", media_planner=" + mediaPlanner +
                ", admin=" + admin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserEntity that = (UserEntity) o; // Replace `User` with your actual class name if different.
        return id.equals(that.id) && firstName.equals(that.firstName) && lastName.equals(that.lastName)
                && email.equals(that.email) && password.equals(that.password)
                && pauseNotificationStart.equals(that.pauseNotificationStart)
                && pauseNotificationEnd.equals(that.pauseNotificationEnd) && notificationState == that.notificationState
                && mediaPlanner == that.mediaPlanner && admin == that.admin;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, firstName, lastName, password, email, pauseNotificationStart,
                pauseNotificationEnd,
                notificationState, mediaPlanner, admin);
    }
}
