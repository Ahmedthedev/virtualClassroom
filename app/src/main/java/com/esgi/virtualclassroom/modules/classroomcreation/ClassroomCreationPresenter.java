package com.esgi.virtualclassroom.modules.classroomcreation;

import android.text.TextUtils;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.AuthenticationProvider;
import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.User;

class ClassroomCreationPresenter {
    private ClassroomCreationView view;
    private FirebaseProvider firebaseProvider;

    ClassroomCreationPresenter(ClassroomCreationView view) {
        this.view = view;
        this.firebaseProvider = FirebaseProvider.getInstance();
    }

    private boolean isFormValid(String title, String description, long start, long end) {
        boolean valid = true;

        if (TextUtils.isEmpty(title) || title.length() < 2) {
            view.showTitleError(R.string.error_invalid_title);
            valid = false;
        }

        return valid;
    }

    public void onConfirmButtonClick(String title, String description, long start, long end) {
        view.resetErrors();

        boolean valid = isFormValid(title, description, start, end);
        if (!valid) {
            return;
        }

        view.closeKeyboard();
        view.showProgressDialog();
        postClassroom(title, description, start, end);
    }

    private void postClassroom(String title, String description, long start, long end) {
        User teacher = AuthenticationProvider.getCurrentUser();
        Classroom classroom = new Classroom(title, description, start, end, teacher);

        this.firebaseProvider.postClassroom(classroom)
                .addOnSuccessListener(aVoid -> view.postClassroomSuccess())
                .addOnFailureListener(e -> view.showCreationError(R.string.error_auth))
                .addOnCompleteListener(task -> view.hideProgressDialog());
    }

    public void onCancelButtonClick() {
        this.view.exit();
    }
}
