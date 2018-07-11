package com.esgi.virtualclassroom.modules.register;

import android.text.TextUtils;

import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

class RegisterPresenter {
    private RegisterView view;
    private FirebaseProvider firebaseProvider;

    RegisterPresenter(RegisterView view) {
        this.view = view;
        this.firebaseProvider = FirebaseProvider.getInstance();
    }

    void onRegisterAttempt(String username, String email, String password1, String password2) {
        view.resetErrors();

        boolean valid = isFormValid(username, email, password1, password2);
        if (!valid) {
            return;
        }

        view.closeKeyboard();
        view.showProgressDialog();
        createUser(email, password1, username);
    }

    private boolean isFormValid(String username, String email, String password1, String password2) {
        boolean valid = true;

        if (TextUtils.isEmpty(password2) || password2.length() < 5) {
            view.showPassword2Error("Invalid password");
            valid = false;
        }

        if (TextUtils.isEmpty(password1) || password1.length() < 5) {
            view.showPassword1Error("Invalid password");
            valid = false;
        }

        if (TextUtils.isEmpty(email) || email.length() < 5 || !email.contains("@")) {
            view.showEmailError("Invalid e-mail");
            valid = false;
        }

        if (TextUtils.isEmpty(username)) {
            view.showUsernameError("Invalid username");
            valid = false;
        }

        return valid;
    }

    private void createUser(String email, String password, final String username) {
        this.firebaseProvider.createUser(email, password).addOnSuccessListener(authResult ->
            this.firebaseProvider.updateUser(authResult, username).addOnSuccessListener(e -> {
                view.hideProgressDialog();
                view.goToLoginActivity();
        })).addOnFailureListener(e -> {
            if (e instanceof FirebaseAuthUserCollisionException) {
                view.showRegisterError("You already have an existing account.\\nPlease try another authentication provider.");
            } else {
                view.showRegisterError("An error has occurred during the Email authentication process.");
            }

            this.firebaseProvider.signOut();
            view.hideProgressDialog();
        });
    }
}
