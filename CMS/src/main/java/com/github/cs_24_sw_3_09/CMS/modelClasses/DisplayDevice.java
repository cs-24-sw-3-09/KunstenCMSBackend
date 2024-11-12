package com.github.cs_24_sw_3_09.CMS.modelClasses;

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

    protected int id = -1;
    private String name;
    private String location;
    private String model;
    private String displayOrientation;
    private String resolution;
    private int fallbackId;
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
