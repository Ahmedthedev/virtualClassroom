package com.esgi.virtualclassroom.modules.classroom.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.modules.classroom.adapters.AttachmentsRecyclerViewAdapter;
import com.esgi.virtualclassroom.data.models.Attachment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AttachmentsFragment extends BottomSheetDialogFragment {

    public static String EXTRA_MODULE_ID = "extra_module_id";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Map<String, Attachment> attachments = new HashMap<>();
    private String moduleId;
    private DatabaseReference dbRef;
    private DatabaseReference attachmentsRef;

    public static AttachmentsFragment newInstance(String moduleId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MODULE_ID, moduleId);
        AttachmentsFragment fragment = new AttachmentsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_attachments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.attachments_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AttachmentsRecyclerViewAdapter(attachments);
        recyclerView.setAdapter(adapter);

        attachmentsRef = dbRef.child("attachments").child(moduleId);
        attachmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attachments.clear();

                for (DataSnapshot documentSnapshot : dataSnapshot.getChildren()) {
                    attachments.put(documentSnapshot.getKey(), documentSnapshot.getValue(Attachment.class));
                }

                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(attachments.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
