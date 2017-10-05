package com.esgi.virtualclassroom.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.adapters.ChatRecyclerViewAdapter;
import com.esgi.virtualclassroom.models.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    public static String EXTRA_MODULE_ID = "extra_module_id";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Message> messages = new ArrayList<>();
    private String moduleId;
    private DatabaseReference dbRef;
    private DatabaseReference messagesRef;

    public static ChatFragment newInstance(String moduleId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MODULE_ID, moduleId);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_futur_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbRef = FirebaseDatabase.getInstance().getReference();
        moduleId = getArguments().getString(EXTRA_MODULE_ID);

        if (getView() == null || moduleId == null) {
            return;
        }

        recyclerView = getView().findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatRecyclerViewAdapter(messages);
        recyclerView.setAdapter(adapter);

        messagesRef = dbRef.child("messages").child(moduleId);
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    messages.add(messageSnapshot.getValue(Message.class));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
