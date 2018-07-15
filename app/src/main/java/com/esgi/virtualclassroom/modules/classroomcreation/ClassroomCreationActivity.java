package com.esgi.virtualclassroom.modules.classroomcreation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esgi.virtualclassroom.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassroomCreationActivity extends AppCompatActivity implements ClassroomCreationView {
    private ClassroomCreationPresenter presenter;
    private ProgressDialog progressDialog;
    private Calendar calendar;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.classroom_creation_title_text_edit) EditText titleEditText;
    @BindView(R.id.classroom_creation_description_text_edit) EditText descriptionEditText;
    @BindView(R.id.classroom_creation_start_date_picker_button) Button classroomStartDateButton;
    @BindView(R.id.classroom_creation_end_date_picker_button) Button classroomEndDateButton;
    @BindView(R.id.classroom_creation_start_time_picker_button) Button classroomStartTimeButton;
    @BindView(R.id.classroom_creation_end_time_picker_button) Button classroomEndTimeButton;

    DatePickerDialog.OnDateSetListener classroomStartDatePicker = (view, year, month, dayOfMonth) -> {
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM. yyyy", Locale.getDefault());
        String format = formatter.format(date);
        classroomStartDateButton.setText(format);
    };

    DatePickerDialog.OnDateSetListener classroomEndDatePicker = (view, year, month, dayOfMonth) -> {
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM. yyyy", Locale.getDefault());
        String format = formatter.format(date);
        classroomEndDateButton.setText(format);
    };

    TimePickerDialog.OnTimeSetListener classroomStartTimePicker = (view, hourOfDay, minutes) -> {
        String hourString = String.format(Locale.getDefault(), "%02d", hourOfDay) + ":" + String.format(Locale.getDefault(), "%02d", minutes);
        classroomStartTimeButton.setText(hourString);
    };

    TimePickerDialog.OnTimeSetListener classroomEndTimePicker = (view, hourOfDay, minutes) -> {
        String hourString = String.format(Locale.getDefault(), "%02d", hourOfDay) + ":" + String.format(Locale.getDefault(), "%02d", minutes);
        classroomEndTimeButton.setText(hourString);
    };

    @OnClick(R.id.classroom_creation_start_date_picker_button)
    void onStartDatePickerButtonClick() {
        showDatePicker(classroomStartDatePicker);
    }

    @OnClick(R.id.classroom_creation_end_date_picker_button)
    void onEndDatePickerButtonClick() {
        showDatePicker(classroomEndDatePicker);
    }

    @OnClick(R.id.classroom_creation_start_time_picker_button)
    void onStartTimePickerButtonClick() {
        showTimePicker(classroomStartTimePicker);
    }

    @OnClick(R.id.classroom_creation_end_time_picker_button)
    void onEndTimePickerButtonClick() {
        showTimePicker(classroomEndTimePicker);
    }

    @OnClick(R.id.classroom_creation_confirm_button)
    void onConfirmButtonClick() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        long start = new Date().getTime();
        long end = start + 3600000;
        presenter.onConfirmButtonClick(title, description, start, end);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ClassroomCreationPresenter(this);
        calendar = Calendar.getInstance(Locale.getDefault());
        setContentView(R.layout.activity_classroom_creation);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

    private void showDatePicker(DatePickerDialog.OnDateSetListener datePickerListener) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePicker(TimePickerDialog.OnTimeSetListener timePickerListener) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timePickerListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }
}