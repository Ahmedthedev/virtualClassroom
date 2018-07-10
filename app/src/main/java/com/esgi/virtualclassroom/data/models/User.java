package com.esgi.virtualclassroom.data.models;

import java.util.List;


public class User {
    public String uid;
    public String name;
    public String email;
    public Boolean isProf;
    public List<Classroom> classrooms;

    public User() { }

    public User(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public User(String name, String email, List<Classroom> classrooms, boolean isProf) {
        this.name = name;
        this.email = email;
        this.isProf = isProf;
        this.classrooms = classrooms;
    }

    public User(String name, String email, boolean isProf) {
        this.name = name;
        this.email = email;
        this.isProf = isProf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
