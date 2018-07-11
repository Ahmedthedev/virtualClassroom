package com.esgi.virtualclassroom.data.models;

public class User {
    private String uid;
    private String name;
    private String email;
    private Boolean isProf;

    public User() { }

    public User(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getProf() {
        return isProf;
    }
}
