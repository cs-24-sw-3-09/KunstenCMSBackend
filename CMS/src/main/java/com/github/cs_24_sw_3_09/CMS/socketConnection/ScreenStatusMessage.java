package com.github.cs_24_sw_3_09.CMS.socketConnection;

public class ScreenStatusMessage {
    private int screenId;
    private String current_url;
    private String type;

    public ScreenStatusMessage(int screenId, String current_url, String type) {
        this.screenId = screenId;
        this.current_url = current_url;
        this.type = type;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
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
