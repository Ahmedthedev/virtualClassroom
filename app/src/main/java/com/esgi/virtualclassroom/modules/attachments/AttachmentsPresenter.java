package com.esgi.virtualclassroom.modules.attachments;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Attachment;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttachmentsPresenter {
    private AttachmentsView view;
    private FirebaseProvider firebaseProvider;
    private Classroom classroom;
    private List<Attachment> attachments;

    AttachmentsPresenter(AttachmentsView view, Classroom classroom) {
        this.view = view;
        this.attachments = new ArrayList<>();
        this.classroom = classroom;
        this.firebaseProvider = FirebaseProvider.getInstance();
        this.init();
    }

    private void init() {
        this.firebaseProvider.getAttachments(this.classroom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attachments.clear();

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    attachments.add(messageSnapshot.getValue(Attachment.class));
                }

                view.updateAttachmentsList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<Attachment> getAttachmentsList() {
        return attachments;
    }

    public void onSendAttachmentButtonClick(String name, Bitmap file) {
        this.firebaseProvider.uploadImage(classroom, name, file)
                .addOnFailureListener(Throwable::printStackTrace);
    }
}
