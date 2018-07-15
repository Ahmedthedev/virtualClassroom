package com.esgi.virtualclassroom.modules.classroomcreation;

interface ClassroomCreationView {
    void showCreationError(int stringId);
    void showTitleError(int stringId);
    void resetErrors();
    void showProgressDialog();
    void hideProgressDialog();
    void closeKeyboard();
    void postClassroomSuccess();
    void exit();
}
