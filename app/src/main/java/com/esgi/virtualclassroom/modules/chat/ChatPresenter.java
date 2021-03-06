package com.esgi.virtualclassroom.modules.chat;

import android.support.annotation.NonNull;

import com.esgi.virtualclassroom.data.AuthenticationProvider;
import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.Message;
import com.esgi.virtualclassroom.data.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatPresenter {
    private ChatView view;
    private AuthenticationProvider authenticationProvider;
    private FirebaseProvider firebaseProvider;
    private Classroom classroom;
    private List<Message> messages;

    ChatPresenter(ChatView view, Classroom classroom) {
        this.view = view;
        this.messages = new ArrayList<>();
        this.classroom = classroom;
        this.authenticationProvider = AuthenticationProvider.getInstance();
        this.firebaseProvider = FirebaseProvider.getInstance();
    }

    public void onResume() {
        firebaseProvider.getMessages(classroom).addValueEventListener(listener);
    }

    public void onPause() {
        firebaseProvider.getMessages(classroom).removeEventListener(listener);
    }

    public List<Message> getMessagesList() {
        return messages;
    }

    public void onSendMessageButtonClick(String text) {
        User user = authenticationProvider.getCurrentUser();
        Message message = new Message(text, new Date(), user);

        firebaseProvider.postMessage(classroom, message)
                .addOnSuccessListener(task -> view.sendMessageComplete())
                .addOnFailureListener(Throwable::printStackTrace);
    }

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            messages.clear();

            for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                messages.add(messageSnapshot.getValue(Message.class));
            }

            view.updateMessagesList();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };
}
