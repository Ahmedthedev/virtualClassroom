package com.esgi.virtualclassroom.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.models.Message;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder>{
    private List<Message> messages;

    public ChatRecyclerViewAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerViewAdapter.ViewHolder holder, int position) {
        final Message message = messages.get(position);
        holder.messageText.setText(message.text);
        holder.messageUsername.setText(message.user.name);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String dateString = sdf.format(message.dateCreation);
        holder.messageCreationDate.setText(dateString);
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
            messageText = itemView.findViewById(R.id.message_text);
            messageUsername = itemView.findViewById(R.id.message_user_name);
            messageCreationDate = itemView.findViewById(R.id.message_date);
        }
    }
}
