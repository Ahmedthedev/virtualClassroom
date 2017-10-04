package com.esgi.virtualclassroom;

import java.util.Date;

/**
 * Created by apple on 04/10/2017.
 */

public class ListItem {
    public String moduleName;
    public String teacherName;
    public String dateStart;
    public String dateEnd;

    public ListItem(String moduleName, String teacherName, String dateStart, String dateEnd) {
        this.moduleName = moduleName;
        this.teacherName = teacherName;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }
    public ListItem() {}

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}
