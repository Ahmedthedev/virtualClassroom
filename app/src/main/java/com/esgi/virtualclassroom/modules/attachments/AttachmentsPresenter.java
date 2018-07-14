package com.esgi.virtualclassroom.modules.attachments;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;

import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Attachment;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.esgi.virtualclassroom.modules.attachments.AttachmentsFragment.REQUEST_WRITE_EXTERNAL_STORAGE;

public class AttachmentsPresenter implements AttachmentsRecyclerViewAdapter.Listener {
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
        this.firebaseProvider.getAttachments(this.classroom).orderByChild("createdAt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attachments.clear();

                for (DataSnapshot attachmentSnapshot: dataSnapshot.getChildren()) {
                    Attachment attachment = attachmentSnapshot.getValue(Attachment.class);

                    if (attachment == null) {
                        continue;
                    }

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + attachment.getName());
                    attachment.setDownloaded(file.exists());
                    attachments.add(attachment);
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

    @Override
    public void onAttachmentDownloadButtonClick(Attachment attachment, AttachmentsRecyclerViewAdapter.ViewHolder viewHolder) {
        viewHolder.attachmentOpenButton.setVisibility(View.GONE);
        viewHolder.attachmentDownloadButton.setVisibility(View.GONE);
        viewHolder.attachmentDownloadProgress.setVisibility(View.VISIBLE);

        this.firebaseProvider.downloadFile(attachment)
                .addOnFailureListener(exception -> {
                    attachment.setDownloaded(false);
                    viewHolder.attachmentOpenButton.setVisibility(attachment.isDownloaded() ? View.VISIBLE : View.GONE);
                    viewHolder.attachmentDownloadButton.setVisibility(attachment.isDownloaded() ? View.GONE : View.VISIBLE);
                    viewHolder.attachmentDownloadProgress.setVisibility(View.GONE);
                    this.view.updateAttachmentsList();
                })
                .addOnSuccessListener(taskSnapshot -> {
                    attachment.setDownloaded(true);
                    viewHolder.attachmentOpenButton.setVisibility(attachment.isDownloaded() ? View.VISIBLE : View.GONE);
                    viewHolder.attachmentDownloadButton.setVisibility(attachment.isDownloaded() ? View.GONE : View.VISIBLE);
                    viewHolder.attachmentDownloadProgress.setVisibility(View.GONE);
                    this.view.updateAttachmentsList();
                });
    }

    @Override
    public void onAttachmentOpenButtonClick(Attachment attachment) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + attachment.getName());

        if (file.exists()) {
            openFile(file);
        }
    }

    private void openFile(File file) {
        this.view.openFile(file);
    }

    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        switch (requestCode){
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.view.exit();
                }

                break;
        }
    }
}
