package com.github.cs_24_sw_3_09.CMS.model.dto;

import java.util.HashSet;
import java.util.Set;

import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.TimeSlotEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.VisualMediaEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DisplayDeviceDto {

    protected Integer id;
    @Size(min = 1, max = 50, message = "a name must be between 1 and 50 characters")
    private String name;
    @Size(min = 1, max = 100, message = "a location must be between 1 and 100 characters")
    private String location;
    @Size(min = 1, max = 50, message = "a model must be between 1 and 50 characters")
    private String model;
    @Pattern(regexp = "^(vertical|horizontal)$", message = "a display orientationing must be vertical or horizontal")
    private String displayOrientation;
    @Pattern(regexp = "^\\d+x\\d+$", message = "Resolution must be in the format 'widthxheight' (e.g., 1920x1080)")
    private String resolution;
    private Boolean connectedState;
    private VisualMediaEntity fallbackVisualMedia;
    private Set<TimeSlotEntity> timeSlots;

    // No-argument constructor
    public DisplayDeviceDto() {
    }

    // All-arguments constructor
    public DisplayDeviceDto(Integer id, String name, String location, String model,
            String displayOrientation, String resolution, Boolean connectedState,
            VisualMediaEntity fallbackVisualMedia, Set<TimeSlotEntity> timeSlots) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.model = model;
        this.displayOrientation = displayOrientation;
        this.resolution = resolution;
        this.connectedState = connectedState;
        this.fallbackVisualMedia = fallbackVisualMedia;
        this.timeSlots = timeSlots;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDisplayOrientation() {
        return displayOrientation;
    }

    public void setDisplayOrientation(String displayOrientation) {
        this.displayOrientation = displayOrientation;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Boolean getConnectedState() {
        return connectedState;
    }

    public void setConnectedState(Boolean connectedState) {
        this.connectedState = connectedState;
    }

    public VisualMediaEntity getFallbackVisualMedia() {
        return fallbackVisualMedia;
    }

    public void setFallbackVisualMedia(VisualMediaEntity fallbackVisualMedia) {
        this.fallbackVisualMedia = fallbackVisualMedia;
    }

    public Set<TimeSlotEntity> getTimeSlots(Set<TimeSlotEntity> timeSlots){
        return timeSlots;
    }

    public void setTimeSlots(Set<TimeSlotEntity> timeSlots){
        this.timeSlots = timeSlots;
    }


    // Builder Pattern (manual implementation)
    public static class Builder {
        private Integer id;
        private String name;
        private String location;
        private String model;
        private String displayOrientation;
        private String resolution;
        private Boolean connectedState;
        private VisualMediaEntity fallbackVisualMedia;
        private Set<TimeSlotEntity> timeSlots;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public Builder setDisplayOrientation(String displayOrientation) {
            this.displayOrientation = displayOrientation;
            return this;
        }

        public Builder setResolution(String resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder setConnectedState(Boolean connectedState) {
            this.connectedState = connectedState;
            return this;
        }

        public Builder setFallbackVisualMedia(VisualMediaEntity fallbackVisualMedia) {
            this.fallbackVisualMedia = fallbackVisualMedia;
            return this;
        }

        public Builder setTimeSlots(Set<TimeSlotEntity> timeslots){
            this.timeSlots = timeslots;
            return this;
        }

        public DisplayDeviceDto build() {
            return new DisplayDeviceDto(id, name, location, model, displayOrientation, resolution, connectedState,
                    fallbackVisualMedia, timeSlots);
        }
    }

    @Override
    public String toString() {
        return "DisplayDeviceDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", model='" + model + '\'' +
                ", displayOrientation='" + displayOrientation + '\'' +
                ", resolution='" + resolution + '\'' +
                ", connectedState=" + connectedState +
                ", fallbackVisualMedia=" + fallbackVisualMedia +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DisplayDeviceDto that = (DisplayDeviceDto) o;
        return id.equals(that.id) && name.equals(that.name) && location.equals(that.location) &&
                model.equals(that.model) && displayOrientation.equals(that.displayOrientation) &&
                resolution.equals(that.resolution) && connectedState.equals(that.connectedState) &&
                fallbackVisualMedia.equals(that.fallbackVisualMedia);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, location, model, displayOrientation, resolution, connectedState,
                fallbackVisualMedia);
    }
}