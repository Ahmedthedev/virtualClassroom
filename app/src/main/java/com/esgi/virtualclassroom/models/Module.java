package com.esgi.virtualclassroom.models;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class Module {
    public String id;
    public String title;
    public Date start;
    public Date end;
    public Map<String,Document> documents;
    public List<User> student;
    public List<Message> messages;
    public List<Evaluation> evaluations;
    public User teacher;
    public String description;
    public String speechText;

    public Module() {

    }

    public Module(String id, String title, Date start, Date end, User teacher) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        this.teacher = teacher;
    }

    public Module(String title, Date start, Date end, Map<String,Document> documents, List<User> student, List<Message> messages, List<Evaluation> evaluations, User teacher, String description, String speechText) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.documents = documents;
        this.student = student;
        this.messages = messages;
        this.evaluations = evaluations;
        this.teacher = teacher;
        this.description = description;
        this.speechText = speechText;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public List<User> getStudent() {
        return student;
    }

    public void setStudent(List<User> student) {
        this.student = student;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpeechText() {
        return speechText;
    }

    public void setSpeechText(String speechText) {
        this.speechText = speechText;
    }
}
