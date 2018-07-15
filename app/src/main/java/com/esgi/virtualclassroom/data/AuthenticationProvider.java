package com.esgi.virtualclassroom.data;

import com.esgi.virtualclassroom.data.models.User;

public class AuthenticationProvider {
    private static AuthenticationProvider instance;
    private static User _currentUser;

    private AuthenticationProvider() {}

    public static AuthenticationProvider getInstance() {
        if (instance == null) {
            instance = new AuthenticationProvider();
        }

        return instance;
    }

    public static User getCurrentUser() {
        return _currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        _currentUser = currentUser;
    }
}
