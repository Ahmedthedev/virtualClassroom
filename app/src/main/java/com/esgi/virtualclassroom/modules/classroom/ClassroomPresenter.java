package com.esgi.virtualclassroom.modules.classroom;

import com.google.firebase.auth.FirebaseAuth;

public class ClassroomPresenter {
    private ClassroomView view;
    private FirebaseAuth firebaseAuth;

    ClassroomPresenter(ClassroomView view) {
        this.view = view;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void getClassrooms() {

    }
}
