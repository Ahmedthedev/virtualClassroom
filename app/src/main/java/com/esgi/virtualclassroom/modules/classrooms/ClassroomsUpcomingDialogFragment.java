package com.esgi.virtualclassroom.modules.classrooms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.models.Classroom;

import static com.esgi.virtualclassroom.modules.classroom.ClassroomActivity.EXTRA_CLASSROOM;

public class ClassroomsUpcomingDialogFragment extends DialogFragment {
    private Listener listener;
    private Classroom classroom;

    public static ClassroomsUpcomingDialogFragment newInstance(Classroom classroom) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CLASSROOM, classroom);
        ClassroomsUpcomingDialogFragment fragment = new ClassroomsUpcomingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
           classroom = getArguments().getParcelable(EXTRA_CLASSROOM);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setTitle(getString(R.string.upcoming_classroom_dialog_title, classroom.getTitle()))
                .setMessage(getString(R.string.upcoming_classroom_dialog_text))
                .setNegativeButton(R.string.upcoming_classroom_dialog_decline, (dialog, id) -> listener.onDeclineClick(classroom))
                .setPositiveButton(R.string.upcoming_classroom_dialog_accept, (dialog, id) -> listener.onAcceptClick(classroom));

        return builder.create();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onAcceptClick(Classroom classroom);
        void onDeclineClick(Classroom classroom);
    }
}
