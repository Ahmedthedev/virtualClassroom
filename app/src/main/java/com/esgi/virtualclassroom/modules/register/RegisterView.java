package com.esgi.virtualclassroom.modules.register;

interface RegisterView {
    void showRegisterError(String error);
    void showUsernameError(String error);
    void showEmailError(String error);
    void showPassword1Error(String error);
    void showPassword2Error(String error);
    void resetErrors();
    void showProgressDialog();
    void hideProgressDialog();
    void closeKeyboard();
    void goToHomeActivity();
}
