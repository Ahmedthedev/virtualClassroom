package com.esgi.virtualclassroom.modules.classrooms;

import android.support.annotation.NonNull;

import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ClassroomsPresenter implements ClassroomsRecyclerViewAdapter.Listener, ClassroomsUpcomingDialogFragment.Listener {
    private static final String PERIOD_LIVE = "live";
    private static final String PERIOD_UPCOMING = "upcoming";
    private static final String PERIOD_PAST = "past";
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
                    Classroom classroom = classroomSnapshot.getValue(Classroom.class);

                    if (classroom == null) {
                        continue;
                    }

                    if (!classroomsPeriod.toLowerCase().equals(PERIOD_LIVE) || classroom.getStart().getTime() < new Date().getTime()) {
                        classrooms.add(classroom);
                    }
                }

                view.updateClassroomsList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Query getClassrooms(String classroomsPeriod) {
        switch (classroomsPeriod.toLowerCase()) {
            case PERIOD_LIVE:
                return this.firebaseProvider.getClassroomsLive();
            case PERIOD_UPCOMING:
                return this.firebaseProvider.getClassroomsUpcoming();
            case PERIOD_PAST:
                return this.firebaseProvider.getClassroomsPast();
            default:
                return this.firebaseProvider.getClassroomsLive();
        }
    }

    @Override
    public void onClassroomClick(Classroom classroom) {
        switch (classroomsPeriod.toLowerCase()) {
            case PERIOD_LIVE:
                view.goToClassroom(classroom);
                break;
            case PERIOD_UPCOMING:
                onUpcomingClassroomClick(classroom);
                break;
            case PERIOD_PAST:
                view.goToClassroom(classroom);
                break;
        }
    }

    private void onUpcomingClassroomClick(Classroom classroom) {
        view.showPopupUpcomingClassroom(classroom);
    }

    @Override
    public void onAcceptClick(Classroom classroom) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }

        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName());
        this.firebaseProvider.postSubscription(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    @Override
    public void onDeclineClick(Classroom classroom) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }

        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName());
        this.firebaseProvider.deleteSubscription(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }
}
