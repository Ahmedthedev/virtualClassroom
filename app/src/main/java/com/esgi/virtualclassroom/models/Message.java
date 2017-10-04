package com.esgi.virtualclassroom.models;

import java.util.Date;


public class Message {

    public String text;
    public Date start;
    public User user;

    public Message() {

    }

    public Message(String text, Date start, User user) {
        this.text = text;
        this.start = start;
        this.user = user;
    }

}
