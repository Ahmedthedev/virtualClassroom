package com.esgi.virtualclassroom.modules.classrooms;

import android.widget.ImageView;

import com.esgi.virtualclassroom.data.models.Classroom;
import com.google.firebase.storage.StorageReference;

interface ClassroomsView {
    void goToClassroom(Classroom classroom);
    void updateClassroomsList();
    void showPopupUpcomingClassroom(Classroom classroom);
    void loadImage(StorageReference ref, ImageView imageView);
}
