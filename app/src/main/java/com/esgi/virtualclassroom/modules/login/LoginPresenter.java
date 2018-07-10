package com.esgi.virtualclassroom.modules.login;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

class LoginPresenter {
    private LoginView view;
    private FirebaseAuth firebaseAuth;

    LoginPresenter(LoginView view) {
        this.view = view;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    void onRegisterButtonClick() {
        view.goToRegisterActivity();
    }

    void onLoginAttempt(String email, String password) {
        view.resetErrors();

        boolean valid = isFormValid(email, password);
        if (!valid) {
            return;
        }

        view.closeKeyboard();
        view.showProgressDialog();
        signInWithEmailAndPassword(email, password);
    }

    void checkUserLogged() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
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

    private void signInWithEmailAndPassword(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            view.hideProgressDialog();
            view.goToHomeActivity();
        }).addOnFailureListener(e -> {
            view.showLoginError("An error has occurred during the Email authentication process.");
            firebaseAuth.signOut();
            view.hideProgressDialog();
        });
    }
}
