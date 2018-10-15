package com.aap.filmtime.model;

import java.io.Serializable;

/**
 * Pojo of dummy content
 */
public class DummyContentResp implements Serializable {
    private String url;
    private String title;
    private String name;
    private boolean isPoweredBy;
    private String sponsoredBy;
    private String description;
    private String videoUrl;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPoweredBy() {
        return isPoweredBy;
    }

    public void setPoweredBy(boolean poweredBy) {
        isPoweredBy = poweredBy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSponsoredBy() {
        return sponsoredBy;
    }

    public void setSponsoredBy(String sponsoredBy) {
        this.sponsoredBy = sponsoredBy;
    }
}
