package com.esgi.virtualclassroom.modules.home;

public class HomePresenter {
    private HomeView view;

    HomePresenter(HomeView view) {
        this.view = view;
    }

    public void getClassrooms() {

    }

    public void onClassroomItemClick(String classroom) {
        view.goToClassroom(classroom);
    }
}
