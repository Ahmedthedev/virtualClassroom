package com.esgi.virtualclassroom.modules.attachments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.models.Attachment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttachmentsRecyclerViewAdapter extends RecyclerView.Adapter<AttachmentsRecyclerViewAdapter.ViewHolder>{
    private List<Attachment> attachments;

    AttachmentsRecyclerViewAdapter(List<Attachment> attachments) {
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
        holder.bind(attachments.get(position));
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.attachment_name) TextView attachmentName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Attachment attachment) {
            attachmentName.setText(attachment.getName());
        }
    }
}
