package com.esgi.virtualclassroom.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esgi.virtualclassroom.R;

public class LogInFragment extends Fragment {
    private OnLoginInteractionListener listener;
    private EditText emailEditText;
    private EditText passwordEditText;

    public LogInFragment() {

    }

    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = view.findViewById(R.id.email_edit_text);
        passwordEditText = view.findViewById(R.id.password_edit_text);

        Button loginButton = view.findViewById(R.id.btn_log_in);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        Button noAccountButton = view.findViewById(R.id.btn_no_account);
        noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.goToCreateAccountScreen();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginInteractionListener) {
            listener = (OnLoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnLoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void logIn() {
        if (!validateForm()) {
            return;
        }

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        listener.logInWithEmailPassword(email, password);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email) || email.length() < 6) {
            emailEditText.setError(getString(R.string.fragment_log_in_email_invalid_error_msg));
            emailEditText.requestFocus();
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordEditText.setError(getString(R.string.fragment_log_in_password_invalid_error_msg));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }

    public interface OnLoginInteractionListener {
        void logInWithEmailPassword(String email, String password);
        void goToCreateAccountScreen();
    }
}
