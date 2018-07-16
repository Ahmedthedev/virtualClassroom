package com.esgi.virtualclassroom.data;

import com.esgi.virtualclassroom.data.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationProvider {
    private static AuthenticationProvider instance;
    private static User _currentUser;
    private FirebaseAuth firebaseAuth;

    private AuthenticationProvider() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public static AuthenticationProvider getInstance() {
        if (instance == null) {
            instance = new AuthenticationProvider();
        }

        return instance;
    }

    public User getCurrentUser() {
        return _currentUser;
    }

    public void setCurrentUser(User currentUser) {
        _currentUser = currentUser;
    }

    public void signOut() {
        this.firebaseAuth.signOut();
        _currentUser = null;
    }
}
