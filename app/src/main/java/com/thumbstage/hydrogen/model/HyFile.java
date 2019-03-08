package com.thumbstage.hydrogen.model;

public class HyFile {
    protected String name;
    protected String id;
    protected String url;
    protected Boolean isInCloud;

    public HyFile() {
    }

    public HyFile(String name, String id, String url, Boolean isInCloud) {
        this.name = name;
        this.id = id;
        this.url = url;
        this.isInCloud = isInCloud;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
