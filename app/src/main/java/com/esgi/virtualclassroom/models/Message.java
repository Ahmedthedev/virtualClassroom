package com.esgi.virtualclassroom.models;

import java.util.Date;


public class Message {
    public String text;
    public Date dateCreation;
    public String username;
    public String userId;

    public Message() {

    }

    public Message(String text, Date dateCreation, String username, String userId) {
        this.text = text;
        this.dateCreation = dateCreation;
        this.username = username;
        this.userId = userId;
    }
}
