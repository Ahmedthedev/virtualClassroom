package com.esgi.virtualclassroom.modules.classroom;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.User;
import com.esgi.virtualclassroom.modules.attachments.AttachmentsFragment;
import com.esgi.virtualclassroom.modules.chat.ChatFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private boolean permissionToRecordAccepted;

    ClassroomPresenter(ClassroomView view, Classroom classroom) {
        this.view = view;
        this.classroom = classroom;
        this.permissionToRecordAccepted = false;
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

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }

        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName());
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
        this.view.clearDrawing();
        final String name = new Date().getTime() + ".jpg";
        this.firebaseProvider.uploadImage(classroom, name, bitmap).addOnFailureListener(Throwable::printStackTrace);
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
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }

        if (!permissionToRecordAccepted) {
            this.view.exit();
        } else {
            this.view.startSpeech();
        }
    }

    public void updateSpeechText(String speechText) {
        this.firebaseProvider.getClassroom(classroom).child("speechText").setValue(speechText)
                .addOnSuccessListener(aVoid -> view.updateView(classroom))
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public void onStop() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }

        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName());
        this.firebaseProvider.deleteViewer(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }
}
