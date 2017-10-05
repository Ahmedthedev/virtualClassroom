package com.esgi.virtualclassroom.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.models.Message;

import java.util.List;


public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder>{
    private List<Message> messages;

    public ChatRecyclerViewAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public ChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatRecyclerViewAdapter.ViewHolder holder, int position) {
        final Message message = messages.get(position);
        holder.messageText.setText(message.text);
        holder.messageUsername.setText(message.username);
        holder.messageCreationDate.setText(message.dateCreation.toString());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView messageUsername;
        TextView messageCreationDate;

        ViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.moduleName);
            messageUsername = itemView.findViewById(R.id.teacherName);
//            messageCreationDate = itemView.findViewById(R.id.ad_image_view);
        }
    }
}
