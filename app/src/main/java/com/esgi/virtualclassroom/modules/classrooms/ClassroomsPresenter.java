package com.esgi.virtualclassroom.modules.classrooms;

import android.support.annotation.NonNull;

import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassroomsPresenter implements ClassroomsRecyclerViewAdapter.Listener {
    private ClassroomsView view;
    private ArrayList<Classroom> classrooms;
    private String classroomsPeriod;
    private FirebaseProvider firebaseProvider;

    ClassroomsPresenter(ClassroomsView view, String classroomsPeriod) {
        this.view = view;
        this.classrooms = new ArrayList<>();
        this.classroomsPeriod = classroomsPeriod;
        this.firebaseProvider = FirebaseProvider.getInstance();
        this.init();
    }

    public ArrayList<Classroom> getClassroomsList() {
        return classrooms;
    }

    private void init() {
        this.getClassrooms(this.classroomsPeriod).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classrooms.clear();

                for (DataSnapshot classroomSnapshot: dataSnapshot.getChildren()) {
                    classrooms.add(classroomSnapshot.getValue(Classroom.class));
                }

                view.updateClassroomsList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Query getClassrooms(String classroomsPeriod) {
        switch (classroomsPeriod) {
            case "Live":
                return this.firebaseProvider.getClassroomsLive();
            case "Upcoming":
                return this.firebaseProvider.getClassroomsUpcoming();
            case "Past":
                return this.firebaseProvider.getClassroomsPast();
            default:
                return this.firebaseProvider.getClassroomsLive();
        }
    }

    @Override
    public void onClassroomClick(Classroom classroom) {
        view.goToClassroom(classroom);
    }
}
