package com.thumbstage.hydrogen.model;

public class FileBase {
    protected String id;
    protected String url;
    protected Boolean isInCloud;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getInCloud() {
        return isInCloud;
    }

    public void setInCloud(Boolean inCloud) {
        isInCloud = inCloud;
    }
}
