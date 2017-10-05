package com.esgi.virtualclassroom.models;

/**
 * Created by apple on 05/10/2017.
 */

public class Document {
    private String url;
    private String name;

    public Document() {

    }

    public Document(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
