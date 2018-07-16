package com.esgi.virtualclassroom.modules.home;

import com.esgi.virtualclassroom.data.AuthenticationProvider;

class HomePresenter {
    private HomeView view;
    private AuthenticationProvider authenticationProvider;

    HomePresenter(HomeView view) {
        this.view = view;
        this.authenticationProvider = AuthenticationProvider.getInstance();
    }

    public void onActionLogOutButtonClick() {
        authenticationProvider.signOut();
        view.signOut();
    }

    public void onClassroomAddButtonClick() {
        view.showClassroomCreationActivity();
    }
}
