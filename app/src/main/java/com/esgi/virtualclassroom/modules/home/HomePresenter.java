package com.esgi.virtualclassroom.modules.home;

import com.esgi.virtualclassroom.data.api.FirebaseProvider;

class HomePresenter {
    private HomeView view;
    private FirebaseProvider firebaseProvider;

    HomePresenter(HomeView view) {
        this.view = view;
        this.firebaseProvider = FirebaseProvider.getInstance();
    }

    public void signOut() {
        this.firebaseProvider.signOut();
        this.view.signOut();
    }

    public void onClassroomAddButtonClick() {
        this.view.showClassroomCreationActivity();
    }
}
