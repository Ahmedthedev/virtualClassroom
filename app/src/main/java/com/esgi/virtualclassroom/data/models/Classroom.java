package com.esgi.virtualclassroom.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Classroom implements Parcelable {
    private String id;
    private String title;
    private String description;
    private Date start;
    private Date end;
    private int attachmentsCount;
    private int studentsCount;
    private String speechText;
    private String teacherId;

    public Classroom() { }

    protected Classroom(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        attachmentsCount = in.readInt();
        studentsCount = in.readInt();
        speechText = in.readString();
        teacherId = in.readString();
    }

    public static final Creator<Classroom> CREATOR = new Creator<Classroom>() {
        @Override
        public Classroom createFromParcel(Parcel in) {
            return new Classroom(in);
        }

        @Override
        public Classroom[] newArray(int size) {
            return new Classroom[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(attachmentsCount);
        parcel.writeInt(studentsCount);
        parcel.writeString(speechText);
        parcel.writeString(teacherId);
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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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
