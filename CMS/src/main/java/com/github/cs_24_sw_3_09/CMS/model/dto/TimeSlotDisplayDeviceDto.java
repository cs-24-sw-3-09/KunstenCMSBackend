package com.github.cs_24_sw_3_09.CMS.model.dto;

import java.util.Set;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.google.common.base.Objects;

public class TimeSlotDisplayDeviceDto {

    protected Integer id;
    private TimeSlotEntity timeSlotId;
    private Set<DisplayDeviceEntity> displayDeviceId;

    // No-argument constructor
    public TimeSlotDisplayDeviceDto() {
    }

    // All-arguments constructor
    public TimeSlotDisplayDeviceDto(Integer id, TimeSlotEntity timeSlotId, Set<DisplayDeviceEntity> displayDeviceId) {
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

        public TimeSlotDisplayDeviceDto build() {
            return new TimeSlotDisplayDeviceDto(id, timeSlotId, displayDeviceId);
        }
    }

    @Override
    public String toString() {
        return "TimeSlotDisplayDeviceDto{" +
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
        TimeSlotDisplayDeviceDto that = (TimeSlotDisplayDeviceDto) o;
        return id.equals(that.id) && timeSlotId.equals(that.timeSlotId) && displayDeviceId.equals(that.displayDeviceId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, timeSlotId, displayDeviceId);
    }
}
