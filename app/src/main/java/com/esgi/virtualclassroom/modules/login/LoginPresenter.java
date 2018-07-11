package com.esgi.virtualclassroom.modules.login;

import android.text.TextUtils;

import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.google.firebase.auth.FirebaseUser;

class LoginPresenter {
    private LoginView view;
    private FirebaseProvider firebaseProvider;

    LoginPresenter(LoginView view) {
        this.view = view;
        this.firebaseProvider = FirebaseProvider.getInstance();
    }

    void onRegisterButtonClick() {
        view.goToRegisterActivity();
    }

    void onLoginButtonClick(String email, String password) {
        view.resetErrors();

        boolean valid = isFormValid(email, password);
        if (!valid) {
            return;
        }

        view.closeKeyboard();
        view.showProgressDialog();
        signIn(email, password);
    }

    void checkUserLogged() {
        FirebaseUser firebaseUser = this.firebaseProvider.getCurrentUser();
        if (firebaseUser != null) {
            view.goToHomeActivity();
        }
    }

    private boolean isFormValid(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(password) || password.length() < 5) {
            view.showPasswordError("Invalid password");
            valid = false;
        }

        if (TextUtils.isEmpty(email) || email.length() < 5 || !email.contains("@")) {
            view.showEmailError("Invalid e-mail");
            valid = false;
        }

        return valid;
    }

    private void signIn(String email, String password) {
        this.firebaseProvider.signIn(email, password).addOnSuccessListener(authResult -> {
            view.hideProgressDialog();
            view.goToHomeActivity();
        }).addOnFailureListener(e -> {
            view.showLoginError("An error has occurred during the Email authentication process.");
            this.firebaseProvider.signOut();
            view.hideProgressDialog();
        });
    }
}
