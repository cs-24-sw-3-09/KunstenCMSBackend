package com.github.cs_24_sw_3_09.CMS.model.entities;

import java.util.Set;

import com.github.cs_24_sw_3_09.CMS.model.dto.TimeSlotDisplayDeviceDto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "time_slot_display_devices")
public class TimeSlotDisplayDeviceEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "time_slot_display_devices_id_seq")
    @SequenceGenerator(name = "time_slot_display_devices_id_seq", sequenceName = "time_slot_display_devices_id_seq", allocationSize = 1)
    protected Integer id;
    @NotNull
    @OneToMany
    @JoinTable(
            name = "time_slot_display_device_time_slot",
            joinColumns = {@JoinColumn(name = "time_slot_display_device_id")},
            inverseJoinColumns = {@JoinColumn(name = "time_slot_id")}
    )
    private TimeSlotEntity timeSlotId;
    @NotNull
    @ManyToMany
    @JoinTable(
            name = "time_slot_display_device_display_device",
            joinColumns = {@JoinColumn(name = "time_slot_display_device_id")},
            inverseJoinColumns = {@JoinColumn(name = "display_device_id")}
    )
    private Set<DisplayDeviceEntity> displayDeviceId;

    // No-argument constructor
    public TimeSlotDisplayDeviceEntity() {
    }

    // All-arguments constructor
    public TimeSlotDisplayDeviceEntity(Integer id, TimeSlotEntity timeSlotId, Set<DisplayDeviceEntity> displayDeviceId) {
        this.id = id;
        this.timeSlotId = timeSlotId;
        this.displayDeviceId = displayDeviceId;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TimeSlotEntity getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(TimeSlotEntity timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public Set<DisplayDeviceEntity> getDisplayDeviceId() {
        return displayDeviceId;
    }

    public void setDisplayDeviceId(Set<DisplayDeviceEntity> displayDeviceId) {
        this.displayDeviceId = displayDeviceId;
    }

    // Builder class
    public static class Builder {
        private Integer id;
        private TimeSlotEntity timeSlotId;
        private Set<DisplayDeviceEntity>  displayDeviceId;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setTimeSlotId(TimeSlotEntity timeSlotId) {
            this.timeSlotId = timeSlotId;
            return this;
        }

        public Builder setDisplayDeviceId(Set<DisplayDeviceEntity> displayDeviceId) {
            this.displayDeviceId = displayDeviceId;
            return this;
        }

        public TimeSlotDisplayDeviceEntity build() {
            return new TimeSlotDisplayDeviceEntity(id, timeSlotId, displayDeviceId);
        }
    }

    @Override
    public String toString() {
        return "TimeSlotDisplayDeviceEntity{" +
                "displayDeviceId=" + displayDeviceId +
                ", id=" + id +
                ", timeSlotId=" + timeSlotId +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TimeSlotDisplayDeviceEntity that = (TimeSlotDisplayDeviceEntity) o;
        return id.equals(that.id) && timeSlotId.equals(that.timeSlotId) && displayDeviceId.equals(that.displayDeviceId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, timeSlotId, displayDeviceId);
    }
}