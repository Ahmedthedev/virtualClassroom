package com.esgi.virtualclassroom.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.esgi.virtualclassroom.R;

public class CreateAccountFragment extends Fragment {
    private OnCreateAccountInteractionListener listener;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private EditText nameEditText;

    public CreateAccountFragment() {

    }

    public static CreateAccountFragment newInstance() {
        return new CreateAccountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEditText = view.findViewById(R.id.email_edit_text);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        passwordConfirmEditText = view.findViewById(R.id.password_confirm_edit_text);
        nameEditText = view.findViewById(R.id.name_edit_text);

        view.findViewById(R.id.btn_create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        if (!validateForm()) {
            return;
        }

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();
        listener.createUserWithEmailAndPassword(email, password, name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateAccountInteractionListener) {
            listener = (OnCreateAccountInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCreateAccountInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = nameEditText.getText().toString();
        if (TextUtils.isEmpty(name) || name.length() < 6) {
            nameEditText.setError(getString(R.string.fragment_log_in_name_invalid_error_msg));
            valid = false;
        } else {
            nameEditText.setError(null);
        }

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

            String passwordConfirm = passwordConfirmEditText.getText().toString();
            if (!passwordConfirm.equals(password)) {
                passwordConfirmEditText.setError(getString(R.string.fragment_log_in_password_confirm_invalid_error_msg));
                valid = false;
            } else {
                passwordConfirmEditText.setError(null);
            }
        }

        return valid;
    }

    public interface OnCreateAccountInteractionListener {
        void createUserWithEmailAndPassword(String email, String password, String name);
    }
}
