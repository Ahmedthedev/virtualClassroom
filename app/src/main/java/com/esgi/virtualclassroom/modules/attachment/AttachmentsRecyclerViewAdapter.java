package com.esgi.virtualclassroom.modules.attachment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.models.Attachment;

import java.util.ArrayList;
import java.util.Map;


public class AttachmentsRecyclerViewAdapter extends RecyclerView.Adapter<AttachmentsRecyclerViewAdapter.ViewHolder>{
    private Map<String, Attachment> attachments;

    AttachmentsRecyclerViewAdapter(Map<String, Attachment> attachments) {
        this.attachments = attachments;
    }

    @NonNull
    @Override
    public AttachmentsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_attachments_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentsRecyclerViewAdapter.ViewHolder holder, int position) {
        String key = new ArrayList<>(attachments.keySet()).get(position);
        final Attachment attachment = attachments.get(key);
        holder.attachmentName.setText(attachment.getName());
        holder.attachmentUrl.setText(attachment.getUrl());
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView attachmentName;
        TextView attachmentUrl;

        ViewHolder(View itemView) {
            super(itemView);
            attachmentName = itemView.findViewById(R.id.attachment_name);
            attachmentUrl = itemView.findViewById(R.id.attachment_url);
        }
    }
}
