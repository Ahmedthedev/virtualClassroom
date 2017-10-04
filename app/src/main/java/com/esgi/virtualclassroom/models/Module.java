package com.esgi.virtualclassroom.models;

import java.nio.file.Files;
import java.util.Date;
import java.util.List;


public class Module {

    public String title;
    public Date start;
    public Date end;
    public List<Files> files;
    public List<User> student;
    public List<Message> messages;
    public List<Evaluation> evaluations;
    public User teacher;
    public String description;
    //private SpeechText speechText;

    public Module() {

    }

    public Module(String title, Date start, Date end, List<Files> files, List<User> student, List<Message> messages, List<Evaluation> evaluations, User teacher, String description) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.files = files;
        this.student = student;
        this.messages = messages;
        this.evaluations = evaluations;
        this.teacher = teacher;
        this.description = description;
    }
}
