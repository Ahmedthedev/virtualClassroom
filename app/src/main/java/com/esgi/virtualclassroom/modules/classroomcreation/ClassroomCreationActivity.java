package com.esgi.virtualclassroom.modules.classroomcreation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.esgi.virtualclassroom.R;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassroomCreationActivity extends AppCompatActivity implements ClassroomCreationView {
    private ClassroomCreationPresenter presenter;
    private ProgressDialog progressDialog;

    @BindView(R.id.classroom_creation_title_text_edit) EditText titleEditText;
    @BindView(R.id.classroom_creation_description_text_edit) EditText descriptionEditText;

    @OnClick(R.id.classroom_creation_confirm_button)
    void onConfirmButtonClick() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        long start = new Date().getTime();
        long end = start + 3600000;
        presenter.onConfirmButtonClick(title, description, start, end);
    }

    @OnClick(R.id.classroom_creation_confirm_button)
    void onCancelButtonClick() {
        this.presenter.onCancelButtonClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ClassroomCreationPresenter(this);
        setContentView(R.layout.activity_classroom_creation);
        ButterKnife.bind(this);
    }

    @Override
    public void showCreationError(int stringId) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showTitleError(int stringId) {
        titleEditText.setError(getString(stringId));
        titleEditText.requestFocus();
    }

    @Override
    public void resetErrors() {
        titleEditText.setError(null);
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
    public void postClassroomSuccess() {
        finish();
    }

    @Override
    public void exit() {
        finish();
    }
}