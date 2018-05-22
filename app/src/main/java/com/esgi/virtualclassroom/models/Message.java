package com.esgi.virtualclassroom.models;

import java.util.Date;


public class Message {
    public String id;
    public String text;
    public Date dateCreation;
    public User user;

    public Message() { }

    public Message(String text, Date dateCreation, User user) {
        this.text = text;
        this.dateCreation = dateCreation;
        this.user = user;
    }
}
