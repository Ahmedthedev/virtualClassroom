package com.esgi.virtualclassroom.modules.home;

import com.google.firebase.auth.FirebaseAuth;

public class HomePresenter {
    private HomeView view;
    private FirebaseAuth firebaseAuth;

    HomePresenter(HomeView view) {
        this.view = view;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void getClassrooms() {

    }

    public void goToClassroom(String classroom) {
        view.goToClassroom(classroom);
    }
}
