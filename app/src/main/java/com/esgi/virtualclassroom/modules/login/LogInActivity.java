package com.esgi.virtualclassroom.modules.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.modules.home.HomeActivity;
import com.esgi.virtualclassroom.modules.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity implements LoginView {
    private LoginPresenter presenter;
    private ProgressDialog progressDialog;

    @BindView(R.id.log_in_email_edit_text) EditText emailEditText;
    @BindView(R.id.log_in_password_edit_text) EditText passwordEditText;

    @OnClick(R.id.log_in_register_button)
    void onRegisterButtonClick() {
        presenter.onRegisterButtonClick();
    }

    @OnClick(R.id.log_in_button)
    void onLoginButtonClick() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        presenter.onLoginButtonClick(email, password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenter(this);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkUserLogged();
    }

    @Override
    public void showLoginError(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmailError(int stringId) {
        emailEditText.setError(getString(stringId));
        emailEditText.requestFocus();
    }

    @Override
    public void showPasswordError(int stringId) {
        passwordEditText.setError(getString(stringId));
        passwordEditText.requestFocus();
    }

    @Override
    public void resetErrors() {
        emailEditText.setError(null);
        passwordEditText.setError(null);
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
            progressDialog = null;
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
    public void goToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}