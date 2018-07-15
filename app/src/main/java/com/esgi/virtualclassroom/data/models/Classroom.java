package com.esgi.virtualclassroom.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Classroom implements Parcelable {
    private String id;
    private String title;
    private String description;
    private long start;
    private long end;
    private int attachmentsCount;
    private int viewersCount;
    private int subscriptionsCount;
    private String speechText;
    private User teacher;
    private ArrayList<String> subscriptions;

    public Classroom() {
        this.subscriptions = new ArrayList<>();
    }

    protected Classroom(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        start = in.readLong();
        end = in.readLong();
        attachmentsCount = in.readInt();
        viewersCount = in.readInt();
        subscriptionsCount = in.readInt();
        speechText = in.readString();
        teacher = in.readParcelable(getClass().getClassLoader());
        subscriptions = in.createStringArrayList();
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

    public Classroom(String title, String description, long start, long end, User teacher) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.teacher = teacher;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeLong(start);
        parcel.writeLong(end);
        parcel.writeInt(attachmentsCount);
        parcel.writeInt(viewersCount);
        parcel.writeInt(subscriptionsCount);
        parcel.writeString(speechText);
        parcel.writeParcelable(teacher, i);
        parcel.writeList(subscriptions);
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

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
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

    public void setId(String id) {
        this.id = id;
    }

    public int getAttachmentsCount() {
        return attachmentsCount;
    }

    public void setAttachmentsCount(int attachmentsCount) {
        this.attachmentsCount = attachmentsCount;
    }

    public int getViewersCount() {
        return viewersCount;
    }

    public void setViewersCount(int viewersCount) {
        this.viewersCount = viewersCount;
    }

    public int getSubscriptionsCount() {
        return subscriptionsCount;
    }

    public void setSubscriptionsCount(int subscriptionsCount) {
        this.subscriptionsCount = subscriptionsCount;
    }

    public void setSpeechText(String speechText) {
        this.speechText = speechText;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public ArrayList<String> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(ArrayList<String> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
