package com.esgi.virtualclassroom.modules.classroomcreation;

import android.text.TextUtils;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.AuthenticationProvider;
import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

class ClassroomCreationPresenter {
    private ClassroomCreationView view;
    private AuthenticationProvider authenticationProvider;
    private FirebaseProvider firebaseProvider;
    private SimpleDateFormat dateFormatter;
    private long selectedStartDate;
    private long selectedEndDate;
    private long selectedStartTime;
    private long selectedEndTime;

    ClassroomCreationPresenter(ClassroomCreationView view) {
        this.view = view;
        this.authenticationProvider = AuthenticationProvider.getInstance();
        this.firebaseProvider = FirebaseProvider.getInstance();
        this.dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        this.selectedStartDate = new Date().getTime();
        this.selectedEndDate = this.selectedStartDate + 3600000;
        this.init();
    }

    private void init() {
        Calendar calendar = new GregorianCalendar();

        Date startDate = new Date(selectedStartDate);
        calendar.setTime(startDate);
        onStartDateSelected(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        onStartTimeSelected(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        Date endDate = new Date(selectedEndDate);
        calendar.setTime(endDate);
        onEndDateSelected(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        onEndTimeSelected(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    private boolean isFormValid(String title, long start, long end) {
        boolean valid = true;

        if (TextUtils.isEmpty(title) || title.length() < 2) {
            view.showTitleError(R.string.error_invalid_title);
            valid = false;
        }

        if (end <= start) {
            view.showEndDateError(R.string.error_invalid_end_date);
            valid = false;
        }

        return valid;
    }

    public void onConfirmButtonClick(String title, String description) {
        view.resetErrors();

        long classroomStartDate = selectedStartDate + selectedStartTime;
        long classroomEndDate = selectedEndDate + selectedEndTime;

        boolean valid = isFormValid(title, classroomStartDate, classroomEndDate);
        if (!valid) {
            return;
        }

        view.closeKeyboard();
        view.showProgressDialog();
        postClassroom(title, description, classroomStartDate, classroomEndDate);
    }

    private void postClassroom(String title, String description, long start, long end) {
        User teacher = authenticationProvider.getCurrentUser();
        Classroom classroom = new Classroom(title, description, start, end, teacher);

        firebaseProvider.postClassroom(classroom)
                .addOnSuccessListener(aVoid -> view.exit())
                .addOnFailureListener(e -> view.showCreationError(R.string.error_auth))
                .addOnCompleteListener(task -> view.hideProgressDialog());
    }

    public void onStartDateSelected(int year, int month, int dayOfMonth) {
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        selectedStartDate = date.getTime();

        String text = dateFormatter.format(date);
        view.setStartDateButtonText(text);
    }

    public void onEndDateSelected(int year, int month, int dayOfMonth) {
        Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        selectedEndDate = date.getTime();

        String text = dateFormatter.format(date);
        view.setEndDateButtonText(text);
    }

    public void onStartTimeSelected(int hourOfDay, int minutes) {
        selectedStartTime = (hourOfDay * 3600 * 1000) + (minutes * 60 * 1000);

        String text = String.format(Locale.getDefault(), "%02d", hourOfDay) + ":" + String.format(Locale.getDefault(), "%02d", minutes);
        view.setStartTimeButtonText(text);
    }

    public void onEndTimeSelected(int hourOfDay, int minutes) {
        selectedEndTime = (hourOfDay * 3600 * 1000) + (minutes * 60 * 1000);

        String text = String.format(Locale.getDefault(), "%02d", hourOfDay) + ":" + String.format(Locale.getDefault(), "%02d", minutes);
        view.setEndTimeButtonText(text);
    }
}
