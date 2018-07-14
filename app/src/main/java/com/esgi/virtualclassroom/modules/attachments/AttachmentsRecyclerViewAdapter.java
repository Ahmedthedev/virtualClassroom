package com.esgi.virtualclassroom.modules.attachments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.models.Attachment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttachmentsRecyclerViewAdapter extends RecyclerView.Adapter<AttachmentsRecyclerViewAdapter.ViewHolder>{
    private List<Attachment> attachments;
    private Listener listener;

    AttachmentsRecyclerViewAdapter(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AttachmentsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_attachments_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(attachments.get(position));
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.attachment_name) TextView attachmentName;
        @BindView(R.id.attachment_download_button) ImageView attachmentDownloadButton;
        @BindView(R.id.attachment_download_progress) ProgressBar attachmentDownloadProgress;
        @BindView(R.id.attachment_open_button) Button attachmentOpenButton;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Attachment attachment) {
            attachmentName.setText(attachment.getName());

            attachmentDownloadButton.setVisibility(attachment.isDownloaded() ? View.GONE : View.VISIBLE);
            attachmentDownloadButton.setOnClickListener(view -> listener.onAttachmentDownloadButtonClick(attachment, this));

            attachmentOpenButton.setVisibility(attachment.isDownloaded() ? View.VISIBLE : View.GONE);
            attachmentOpenButton.setOnClickListener(view -> listener.onAttachmentOpenButtonClick(attachment));
        }
    }

    public interface Listener {
        void onAttachmentOpenButtonClick(Attachment attachment);
        void onAttachmentDownloadButtonClick(Attachment attachment, ViewHolder viewHolder);
    }
}
