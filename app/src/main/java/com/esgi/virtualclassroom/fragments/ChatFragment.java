package com.esgi.virtualclassroom.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.adapters.ChatRecyclerViewAdapter;
import com.esgi.virtualclassroom.models.Message;
import com.esgi.virtualclassroom.models.User;
import com.esgi.virtualclassroom.utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment {
    public static String EXTRA_MODULE_ID = "extra_module_id";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Message> messages = new ArrayList<>();
    private String moduleId;
    private DatabaseReference dbRef;
    private DatabaseReference messagesRef;
    private Button sendMsgButton;
    private EditText msgEditText;

    public static ChatFragment newInstance(String moduleId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MODULE_ID, moduleId);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbRef = FirebaseDatabase.getInstance().getReference();
        moduleId = getArguments().getString(EXTRA_MODULE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        msgEditText = view.findViewById(R.id.message_edit_text);
        msgEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Tools.closeKeyboard(getActivity());
                }
            }
        });

        sendMsgButton = view.findViewById(R.id.send_button);
        sendMsgButton.setFocusable(false);
        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatRecyclerViewAdapter(messages);
        recyclerView.setAdapter(adapter);

        messagesRef = dbRef.child("messages").child(moduleId);
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();

                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    messages.add(messageSnapshot.getValue(Message.class));
                }

                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        String text = msgEditText.getText().toString();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null || text.isEmpty()) {
            return;
        }

        User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName());
        Message message = new Message(text, new Date(), user);
        DatabaseReference newMessageRef = messagesRef.push();
        newMessageRef.setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                recyclerView.scrollToPosition(messages.size() - 1);
            }
        });

        msgEditText.setText(null);
        Tools.closeKeyboard(getActivity());
    }
}
