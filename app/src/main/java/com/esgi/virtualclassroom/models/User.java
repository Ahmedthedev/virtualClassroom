package com.esgi.virtualclassroom.models;

import java.util.List;

public class User {
    public String name;
    public String email;
    public Boolean isProf;
    public List<Module> modules;

    public User() {

    }

    public User(String name, String email, List<Module> modules, boolean isProf) {
        this.name = name;
        this.email = email;
        this.isProf = isProf;
        this.modules = modules;
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
