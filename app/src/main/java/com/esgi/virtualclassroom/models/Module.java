package com.esgi.virtualclassroom.models;

import java.nio.file.Files;
import java.util.Date;
import java.util.List;


public class Module {

    public String title;
    public String start;
    public String end;
    public List<Files> documents;
    public List<User> student;
    public List<Message> messages;
    public List<Evaluation> evaluations;
    public User teacher;
    public String description;
    public String speechText;

    public Module() {

    }

    public Module(String title, String start, String end, User teacher) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.teacher = teacher;
    }

    public Module(String title, String start, String end, List<Files> documents, List<User> student, List<Message> messages, List<Evaluation> evaluations, User teacher, String description, String speechText) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<Files> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Files> documents) {
        this.documents = documents;
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
