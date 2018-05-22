package com.esgi.virtualclassroom.modules.register;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.esgi.virtualclassroom.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class RegisterPresenter {
    private RegisterView view;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;

    RegisterPresenter(RegisterView view) {
        this.view = view;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.dbRef = FirebaseDatabase.getInstance().getReference();
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
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                final FirebaseUser user = authResult.getUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                user.updateProfile(profileUpdates);

                User profile = new User(username, user.getEmail(), null, false);
                dbRef.child("users").child(user.getUid()).setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        view.hideProgressDialog();
                        view.goToHomeActivity();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException) {
                    view.showRegisterError("You already have an existing account.\\nPlease try another authentication provider.");
                } else {
                    view.showRegisterError("An error has occurred during the Email authentication process.");
                }

                firebaseAuth.signOut();
                view.hideProgressDialog();
            }
        });
    }
}
