package com.esgi.virtualclassroom.data.models;

import java.util.List;

public class User {
    private String uid;
    private String name;
    private String email;
    private Boolean isProf;
    private List<Classroom> classrooms;

    public User() { }

    public User(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
