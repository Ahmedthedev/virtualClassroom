package com.esgi.virtualclassroom.modules.classroom;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;

import com.esgi.virtualclassroom.data.AuthenticationProvider;
import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.User;
import com.esgi.virtualclassroom.modules.attachments.AttachmentsFragment;
import com.esgi.virtualclassroom.modules.chat.ChatFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import static com.esgi.virtualclassroom.modules.classroom.ClassroomActivity.REQUEST_RECORD_AUDIO_PERMISSION;

public class ClassroomPresenter {
    private ClassroomView view;
    private Classroom classroom;
    private AuthenticationProvider authenticationProvider;
    private FirebaseProvider firebaseProvider;
    private boolean isSpeaking;

    ClassroomPresenter(ClassroomView view, Classroom classroom) {
        this.view = view;
        this.classroom = classroom;
        this.authenticationProvider = AuthenticationProvider.getInstance();
        this.firebaseProvider = FirebaseProvider.getInstance();
        this.isSpeaking = false;
        this.init();
    }

    public void onResume() {
        firebaseProvider.getClassroom(classroom).addValueEventListener(listener);
    }

    public void onPause() {
        firebaseProvider.getClassroom(classroom).removeEventListener(listener);
    }

    public void onStart() {
        User user = authenticationProvider.getCurrentUser();
        firebaseProvider.postViewer(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public void onStop() {
        User user = authenticationProvider.getCurrentUser();
        firebaseProvider.deleteViewer(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    private void init() {
        firebaseProvider.getClassroom(classroom).addValueEventListener(listener);
    }

    public void onChatItemClick() {
        ChatFragment fragment = ChatFragment.newInstance(classroom);
        view.showChatFragment(fragment);
    }

    public void onAttachmentsItemClick() {
        AttachmentsFragment fragment = AttachmentsFragment.newInstance(classroom);
        view.showAttachmentFragment(fragment);
    }

    public void onSendDrawingClick(Bitmap bitmap) {
        final String name = new Date().getTime() + ".jpg";
        firebaseProvider.uploadImage(classroom, name, bitmap).addOnFailureListener(Throwable::printStackTrace);
        view.clearDrawing();
    }

    public void onClearDrawingClick() {
        this.view.clearDrawing();
    }

    public void onSpeechButtonClick() {
        if (!isSpeaking) {
            view.startSpeech();
        } else {
            view.stopSpeech();
        }

        isSpeaking = !isSpeaking;
    }

    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.view.exit();
                }

                break;
        }
    }

    public void updateSpeechText(Bundle bundle) {
        ArrayList<String> voiceText = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (voiceText == null) {
            return;
        }

        String speechText = voiceText.get(0);

        firebaseProvider.getClassroom(classroom).child("speechText").setValue(speechText)
                .addOnSuccessListener(aVoid -> view.updateView(classroom))
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public boolean isClassroomTeacher() {
        User user = authenticationProvider.getCurrentUser();
        return user != null && classroom.getTeacher().getUid().equals(user.getUid());
    }

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            classroom = dataSnapshot.getValue(Classroom.class);
            view.updateView(classroom);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };
}
