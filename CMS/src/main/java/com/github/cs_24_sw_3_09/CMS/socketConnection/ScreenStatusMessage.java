package com.github.cs_24_sw_3_09.CMS.socketConnection;

public class ScreenStatusMessage {
    private int deviceId;
    private String current_url;
    private String type;

    public ScreenStatusMessage(int deviceId, String current_url, String type) {
        this.deviceId = deviceId;
        this.current_url = current_url;
        this.type = type;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getCurrentUrl() {
        return current_url;
    }

    public void setCurrentUrl(String current_url) {
        this.current_url = current_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
