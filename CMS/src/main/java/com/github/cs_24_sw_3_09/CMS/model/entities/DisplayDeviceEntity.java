package com.github.cs_24_sw_3_09.CMS.model.entities;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "display_devices")
public class DisplayDeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "display_device_id_seq")
    @SequenceGenerator(name = "display_device_id_seq", sequenceName = "display_device_id_seq", allocationSize = 1)
    protected Integer id;
    @NotNull
    private String name;
    @NotNull
    private String location;
    @NotNull
    private String model;
    @NotNull
    private String displayOrientation;
    @NotNull
    private String resolution;
    @ColumnDefault("false")
    private Boolean connectedState;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "falback_id")
    private VisualMediaEntity fallbackVisualMedia;

    // No-argument constructor
    public DisplayDeviceEntity() {
    }

    // All-arguments constructor
    public DisplayDeviceEntity(Integer id, String name, String location, String model,
            String displayOrientation, String resolution, Boolean connectedState,
            VisualMediaEntity fallbackVisualMedia) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.model = model;
        this.displayOrientation = displayOrientation;
        this.fallbackVisualMedia = fallbackVisualMedia;
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

    public VisualMediaEntity getFallbackVisualMedia() {
        return fallbackVisualMedia;
    }

    public void setFallbackVisualMedia(VisualMediaEntity fallbackVisualMedia) {
        this.fallbackVisualMedia = fallbackVisualMedia;
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

        public DisplayDeviceEntity build() {
            return new DisplayDeviceEntity(id, name, location, model, displayOrientation, resolution, connectedState,
                    fallbackVisualMedia);
        }
    }

    @Override
    public String toString() {
        return "DisplayDeviceEntity{" +
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
        DisplayDeviceEntity that = (DisplayDeviceEntity) o;
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
