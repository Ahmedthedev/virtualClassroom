package com.esgi.virtualclassroom.modules.attachments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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
    private Context context;
    private FirebaseProvider firebaseProvider;
    private Classroom classroom;
    private List<Attachment> attachments;

    AttachmentsPresenter(AttachmentsView view, Context context, Classroom classroom) {
        this.view = view;
        this.context = context;
        this.attachments = new ArrayList<>();
        this.classroom = classroom;
        this.firebaseProvider = FirebaseProvider.getInstance();
    }

    public void onResume() {
        firebaseProvider.getAttachments(classroom).orderByChild("createdAt").addValueEventListener(listener);
    }

    public void onPause() {
        firebaseProvider.getAttachments(classroom).orderByChild("createdAt").removeEventListener(listener);
    }

    private ValueEventListener listener = new ValueEventListener() {
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
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    public List<Attachment> getAttachmentsList() {
        return attachments;
    }

    @Override
    public void onAttachmentDownloadButtonClick(Attachment attachment, AttachmentsRecyclerViewAdapter.ViewHolder viewHolder) {
        viewHolder.attachmentOpenButton.setVisibility(View.GONE);
        viewHolder.attachmentDownloadButton.setVisibility(View.GONE);
        viewHolder.attachmentDownloadProgress.setVisibility(View.VISIBLE);

        firebaseProvider.downloadFile(attachment)
                .addOnFailureListener(exception -> {
                    attachment.setDownloaded(false);
                    viewHolder.attachmentOpenButton.setVisibility(attachment.isDownloaded() ? View.VISIBLE : View.GONE);
                    viewHolder.attachmentDownloadButton.setVisibility(attachment.isDownloaded() ? View.GONE : View.VISIBLE);
                    viewHolder.attachmentDownloadProgress.setVisibility(View.GONE);
                    view.updateAttachmentsList();
                })
                .addOnSuccessListener(taskSnapshot -> {
                    attachment.setDownloaded(true);
                    viewHolder.attachmentOpenButton.setVisibility(attachment.isDownloaded() ? View.VISIBLE : View.GONE);
                    viewHolder.attachmentDownloadButton.setVisibility(attachment.isDownloaded() ? View.GONE : View.VISIBLE);
                    viewHolder.attachmentDownloadProgress.setVisibility(View.GONE);
                    view.updateAttachmentsList();
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
        if (context == null) {
            return;
        }

        Uri uri = FileProvider.getUriForFile(context, "com.esgi.virtualclassroom", file);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent.createChooser(intent, "Choose an application");

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        switch (requestCode){
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    view.exit();
                }

                break;
        }
    }
}
