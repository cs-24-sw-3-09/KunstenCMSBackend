package com.github.cs_24_sw_3_09.CMS.socketConnection;

public class ScreenStatusMessage {
    private int deviceid;
    private String contentname;
    private String currenturl;
    private String type;

    public ScreenStatusMessage() {}

    public ScreenStatusMessage(int deviceid, String contentname, String currenturl, String type) {
        super();
        this.deviceid = deviceid;
        this.contentname = contentname;
        this.currenturl = currenturl;
        this.type = type;
    }

    public int getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }

    public String getContentname() {
        return contentname;
    }

    public void setContentname(String contentname) {
        this.contentname = contentname;
    }

    public String getCurrenturl() {
        return currenturl;
    }

    public void setCurrenturl(String currenturl) {
        this.currenturl = currenturl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
