package com.esgi.virtualclassroom.modules.login;

interface LoginView {
    void showLoginError(String error);
    void showEmailError(String error);
    void showPasswordError(String error);
    void resetErrors();
    void showProgressDialog();
    void hideProgressDialog();
    void closeKeyboard();
    void goToRegisterActivity();
    void goToHomeActivity();
}
