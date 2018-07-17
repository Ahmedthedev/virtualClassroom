package com.esgi.virtualclassroom.modules.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.AuthenticationProvider;
import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.User;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class LoginPresenter {
    private LoginView view;
    private AuthenticationProvider authenticationProvider;
    private FirebaseProvider firebaseProvider;

    LoginPresenter(LoginView view) {
        this.view = view;
        this.authenticationProvider = AuthenticationProvider.getInstance();
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

    private void checkUserLogged() {
        FirebaseUser firebaseUser = firebaseProvider.getCurrentFirebaseUser();
        if (firebaseUser != null) {
            createUserAndGoToHome(firebaseUser);
        }
    }

    public void onResume() {
        checkUserLogged();
    }

    private boolean isFormValid(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(password) || password.length() < 5) {
            view.showPasswordError(R.string.error_invalid_password);
            valid = false;
        }

        if (TextUtils.isEmpty(email) || email.length() < 5 || !email.contains("@")) {
            view.showEmailError(R.string.error_invalid_email);
            valid = false;
        }

        return valid;
    }

    private void signIn(String email, String password) {
        firebaseProvider.signIn(email, password)
                .addOnSuccessListener(this::onSignInSuccess)
                .addOnFailureListener(this::onSignInFailure);
    }

    private void onSignInSuccess(AuthResult authResult) {
        createUserAndGoToHome(authResult.getUser());
    }

    private void onSignInFailure(Exception exception) {
        exception.printStackTrace();

        view.showLoginError(R.string.error_auth);
        authenticationProvider.signOut();
        view.hideProgressDialog();
    }

    private void createUserAndGoToHome(FirebaseUser firebaseUser) {
        view.showProgressDialog();
        firebaseProvider.getUser(firebaseUser.getUid()).addValueEventListener(listener);
    }

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            if (user == null) {
                return;
            }

            user.setUid(dataSnapshot.getKey());
            authenticationProvider.setCurrentUser(user);
            view.hideProgressDialog();
            view.goToHomeActivity();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            view.hideProgressDialog();
        }
    };
}
