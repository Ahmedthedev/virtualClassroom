package com.esgi.virtualclassroom.data.models;

import java.util.Date;


public class Message {
    private String id;
    private String text;
    private Date dateCreation;
    private User user;

    public Message() { }

    public Message(String text, Date dateCreation, User user) {
        this.text = text;
        this.dateCreation = dateCreation;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public User getUser() {
        return user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
