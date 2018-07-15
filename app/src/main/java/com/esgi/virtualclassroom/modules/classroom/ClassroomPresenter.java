package com.esgi.virtualclassroom.modules.classroom;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import java.util.Date;

import static com.esgi.virtualclassroom.modules.classroom.ClassroomActivity.REQUEST_RECORD_AUDIO_PERMISSION;

public class ClassroomPresenter {
    private ClassroomView view;
    private Classroom classroom;
    private FirebaseProvider firebaseProvider;
    private boolean isSpeaking;

    ClassroomPresenter(ClassroomView view, Classroom classroom) {
        this.view = view;
        this.classroom = classroom;
        this.firebaseProvider = FirebaseProvider.getInstance();
        this.isSpeaking = false;
        this.init();
    }

    private void init() {
        firebaseProvider.getClassroom(classroom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classroom = dataSnapshot.getValue(Classroom.class);
                view.updateView(classroom);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        User user = AuthenticationProvider.getCurrentUser();
        this.firebaseProvider.postViewer(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public void onChatItemClick() {
        ChatFragment fragment = ChatFragment.newInstance(classroom);
        this.view.showChatFragment(fragment);
    }

    public void onAttachmentsItemClick() {
        AttachmentsFragment fragment = AttachmentsFragment.newInstance(classroom);
        this.view.showAttachmentFragment(fragment);
    }

    public void onSendDrawingClick(Bitmap bitmap) {
        final String name = new Date().getTime() + ".jpg";
        this.firebaseProvider.uploadImage(classroom, name, bitmap).addOnFailureListener(Throwable::printStackTrace);
        this.view.clearDrawing();
    }

    public void onClearDrawingClick() {
        this.view.clearDrawing();
    }

    public void onSpeechButtonClick() {
        if (!isSpeaking) {
            this.view.startSpeech();
        } else {
            this.view.stopSpeech();
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

    public void updateSpeechText(String speechText) {
        this.firebaseProvider.getClassroom(classroom).child("speechText").setValue(speechText)
                .addOnSuccessListener(aVoid -> view.updateView(classroom))
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public void onStop() {
        User user = AuthenticationProvider.getCurrentUser();
        this.firebaseProvider.deleteViewer(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }
}
