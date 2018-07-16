package com.esgi.virtualclassroom.modules.classroomcreation;

interface ClassroomCreationView {
    void showCreationError(int stringId);
    void showTitleError(int stringId);
    void resetErrors();
    void showProgressDialog();
    void hideProgressDialog();
    void closeKeyboard();
    void exit();
    void showEndDateError(int stringId);
    void setStartDateButtonText(String text);
    void setEndDateButtonText(String text);
    void setStartTimeButtonText(String text);
    void setEndTimeButtonText(String text);
}
