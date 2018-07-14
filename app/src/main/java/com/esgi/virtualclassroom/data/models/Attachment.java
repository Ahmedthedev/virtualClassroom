package com.esgi.virtualclassroom.data.models;


public class Attachment {
    private String name;
    private String url;
    private int downloads;
    private boolean downloaded;

    public Attachment() { }

    public Attachment(String name, String url, boolean downloaded) {
        this.name = name;
        this.url = url;
        this.downloaded = downloaded;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }
}
