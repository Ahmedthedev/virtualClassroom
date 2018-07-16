package com.esgi.virtualclassroom.modules.register;

import android.text.TextUtils;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.AuthenticationProvider;
import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

class RegisterPresenter {
    private RegisterView view;
    private AuthenticationProvider authenticationProvider;
    private FirebaseProvider firebaseProvider;

    RegisterPresenter(RegisterView view) {
        this.view = view;
        this.authenticationProvider = AuthenticationProvider.getInstance();
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
            view.showPassword2Error(R.string.error_invalid_password);
            valid = false;
        }

        if (TextUtils.isEmpty(password1) || password1.length() < 5) {
            view.showPassword1Error(R.string.error_invalid_password);
            valid = false;
        }

        if (TextUtils.isEmpty(email) || email.length() < 5 || !email.contains("@")) {
            view.showEmailError(R.string.error_invalid_email);
            valid = false;
        }

        if (TextUtils.isEmpty(username)) {
            view.showUsernameError(R.string.error_invalid_username);
            valid = false;
        }

        return valid;
    }

    private void createUser(String email, String password, final String username) {
        this.firebaseProvider.createUser(email, password)
                .addOnSuccessListener(authResult -> this.firebaseProvider.updateUser(authResult, username).addOnSuccessListener(this::onUpdateUserSuccess))
                .addOnFailureListener(this::onCreateUserFailure);
    }

    private void onUpdateUserSuccess(Void aVoid) {
        view.hideProgressDialog();
        view.goToLoginActivity();
    }

    private void onCreateUserFailure(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            view.showRegisterError(R.string.error_already_member);
        } else {
            view.showRegisterError(R.string.error_auth);
        }

        this.authenticationProvider.signOut();
        view.hideProgressDialog();
    }
}
