package com.github.cs_24_sw_3_09.CMS.socketConnection;

import java.util.List;

public class ScreenStatusMessage {
    private int deviceid;
    private String contentname;
    private List<MediaType> medias;
    private int current;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public ScreenStatusMessage() {}

    public ScreenStatusMessage(int deviceid, String contentname, List<MediaType> medias, int current) {
        super();
        this.deviceid = deviceid;
        this.contentname = contentname;
        this.medias = medias;
        this.current = current;
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

    public List<MediaType> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaType> medias) {
        this.medias = medias;
    }


}

class MediaType {
    private String url;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MediaType() {}

    public MediaType(String url, String type) {
        super();
        this.url = url;
        this.type = type;
    }

}