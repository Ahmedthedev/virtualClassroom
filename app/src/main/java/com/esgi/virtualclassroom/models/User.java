package com.esgi.virtualclassroom.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User {

    public List<Module> modules;
    public Boolean isProf;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(List<Module> modules, Boolean isProf) {
        this.modules = modules;
        this.isProf = isProf;
    }
}
