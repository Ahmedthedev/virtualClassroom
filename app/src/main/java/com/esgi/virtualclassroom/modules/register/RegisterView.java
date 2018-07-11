package com.esgi.virtualclassroom.modules.register;

interface RegisterView {
    void showRegisterError(int stringId);
    void showUsernameError(int stringId);
    void showEmailError(int stringId);
    void showPassword1Error(int stringId);
    void showPassword2Error(int stringId);
    void resetErrors();
    void showProgressDialog();
    void hideProgressDialog();
    void closeKeyboard();
    void goToLoginActivity();
}
