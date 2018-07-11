package com.esgi.virtualclassroom.modules.login;

interface LoginView {
    void showLoginError(int stringId);
    void showEmailError(int stringId);
    void showPasswordError(int stringId);
    void resetErrors();
    void showProgressDialog();
    void hideProgressDialog();
    void closeKeyboard();
    void goToRegisterActivity();
    void goToHomeActivity();
}
