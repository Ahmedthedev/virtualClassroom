package com.esgi.virtualclassroom.modules.register;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

class RegisterPresenter {
    private RegisterView view;
    private FirebaseAuth firebaseAuth;

    RegisterPresenter(RegisterView view) {
        this.view = view;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    void onRegisterAttempt(String username, String email, String password1, String password2) {
        view.resetErrors();

        boolean valid = isFormValid(username, email, password1, password2);
        if (!valid) {
            return;
        }

        view.closeKeyboard();
        view.showProgressDialog();
        createUserWithEmailAndPassword(email, password1, username);
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

    private void createUserWithEmailAndPassword(String email, String password, final String username) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            final FirebaseUser user = authResult.getUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
            user.updateProfile(profileUpdates);
            view.hideProgressDialog();
            view.goToLoginActivity();
        }).addOnFailureListener(e -> {
            if (e instanceof FirebaseAuthUserCollisionException) {
                view.showRegisterError("You already have an existing account.\\nPlease try another authentication provider.");
            } else {
                view.showRegisterError("An error has occurred during the Email authentication process.");
            }

            firebaseAuth.signOut();
            view.hideProgressDialog();
        });
    }
}
