package com.esgi.virtualclassroom.modules.register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.esgi.virtualclassroom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends AppCompatActivity implements RegisterView {
    private RegisterPresenter presenter;
    private ProgressDialog progressDialog;

    @BindView(R.id.register_username_edit_text) EditText usernameEditText;
    @BindView(R.id.register_email_edit_text) EditText emailEditText;
    @BindView(R.id.register_password1_edit_text) EditText password1EditText;
    @BindView(R.id.register_password2_edit_text) EditText password2EditText;

    @OnClick(R.id.register_validate_button)
    void onRegisterValidateButtonClick() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password1 = password1EditText.getText().toString();
        String password2 = password2EditText.getText().toString();
        presenter.onRegisterAttempt(username, email, password1, password2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RegisterPresenter(this);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @Override
    public void showRegisterError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showUsernameError(String error) {
        usernameEditText.setError(error);
        usernameEditText.requestFocus();
    }

    @Override
    public void showEmailError(String error) {
        emailEditText.setError(error);
        emailEditText.requestFocus();
    }

    @Override
    public void showPassword1Error(String error) {
        password1EditText.setError(error);
        password1EditText.requestFocus();
    }

    @Override
    public void showPassword2Error(String error) {
        password2EditText.setError(error);
        password2EditText.requestFocus();
    }

    @Override
    public void resetErrors() {
        usernameEditText.setError(null);
        emailEditText.setError(null);
        password1EditText.setError(null);
        password2EditText.setError(null);
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.fragment_log_in_loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    @Override
    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    @Override
    public void goToLoginActivity() {
        finish();
    }
}