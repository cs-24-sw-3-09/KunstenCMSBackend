package com.github.cs_24_sw_3_09.CMS.model.dto;


import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class DisplayDeviceDto {

    protected Integer id;
    private String name;
    private String location;
    private String model;
    private String displayOrientation;
    private String resolution;
    private Boolean connectedState;

    // No-argument constructor
    public DisplayDeviceDto() {
    }

    // All-arguments constructor
    public DisplayDeviceDto(Integer id, String name, String location, String model,
                         String displayOrientation, String resolution, Boolean connectedState) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.model = model;
        this.displayOrientation = displayOrientation;
        this.resolution = resolution;
        this.connectedState = connectedState;
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

    // Builder Pattern (manual implementation)
    public static class Builder {
        private Integer id;
        private String name;
        private String location;
        private String model;
        private String displayOrientation;
        private String resolution;
        private Boolean connectedState;

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

        public DisplayDeviceEntity build() {
            return new DisplayDeviceEntity(id, name, location, model, displayOrientation, resolution, connectedState);
        }
    }

    @Override
    public String toString() {
        return "DisplayDevice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", model='" + model + '\'' +
                ", displayOrientation='" + displayOrientation + '\'' +
                ", resolution='" + resolution + '\'' +
                ", connectedState=" + connectedState +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisplayDeviceDto that = (DisplayDeviceDto) o;
        return id.equals(that.id) && name.equals(that.name) && location.equals(that.location) &&
                model.equals(that.model) && displayOrientation.equals(that.displayOrientation) &&
                resolution.equals(that.resolution) && connectedState.equals(that.connectedState);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, location, model, displayOrientation, resolution, connectedState);
    }
}