package com.esgi.virtualclassroom.modules.classrooms;

import com.esgi.virtualclassroom.data.models.Classroom;

interface ClassroomsView {
    void goToClassroom(Classroom classroom);
    void updateClassroomsList();
    void showPopupUpcomingClassroom(Classroom classroom);
    void closePopupUpcomingClassroom();
}
