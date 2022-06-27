package com.example.springs3demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ContentDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String language;
    private String videoType;
    private String url;

    public ContentDetails() {

    }

    public ContentDetails(String language, String videoType, String url) {
        super();
        this.language = language;
        this.videoType = videoType;
        this.url = url;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getVideoType() {
        return videoType;
    }
    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }


}