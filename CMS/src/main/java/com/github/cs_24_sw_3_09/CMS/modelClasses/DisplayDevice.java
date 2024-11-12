package com.github.cs_24_sw_3_09.CMS.modelClasses;

import jakarta.validation.constraints.*;

public class DisplayDevice {
    // Using builderpattern from OOP
    public static class DisplayDeviceBuilder {
        private DisplayDevice d;

        public DisplayDeviceBuilder() {
            d = new DisplayDevice();
        }

        public void setName(String name) {
            d.setName(name);
        }

        public void setLocation(String location) {
            d.setLocation(location);
        }

        public void setModel(String model) {
            d.setModel(model);
        }

        public void setDisplayOrientation(String displayOrientation) {
            d.setDisplayOrientation(displayOrientation);
        }

        public void setResolution(String resolution) {
            d.setResolution(resolution);
        }

        public void setId(int id) {
            d.id = id;
        }

        public DisplayDevice getDisplayDevice() {
            // If id == -1 make a new DD in the DB or do nothing
            return d;
        }
    }

    // fields and their validation constraints
    protected int id = -1;
    @Size(min = 1, max = 50, message = "a name must be between 1 and 50 characters")
    private String name;
    @Size(min = 1, max = 100, message = "a location must be between 1 and 100 characters")
    private String location;
    @Size(min = 1, max = 50, message = "a model must be between 1 and 50 characters")
    private String model;
    @Pattern(regexp = "^(vertical|horizontal)$")
    private String displayOrientation;
    @Pattern(regexp = "^\\d+x\\d+$", message = "Resolution must be in the format 'widthxheight' (e.g., 1920x1080)")
    private String resolution;
    private int fallbackId; // fallback valididation is not necessary, springboot checks whether an int was
                            // received
    private boolean connectionState = false;

    public int getId() {
        return id;
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

    public int getFallbackId() {
        return fallbackId;
    }

    public void setFallbackId(int fallbackId) {
        this.fallbackId = fallbackId;
    }

    public boolean isConnectionState() {
        return connectionState;
    }

    public void setConnectionState(boolean connectionState) {
        this.connectionState = connectionState;
    }
}
