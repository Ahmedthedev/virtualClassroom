package com.esgi.virtualclassroom.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.adapters.DocumentRecyclerViewAdapter;
import com.esgi.virtualclassroom.adapters.HomeViewPagerAdapter;
import com.esgi.virtualclassroom.models.Document;
import com.esgi.virtualclassroom.models.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentFragment extends Fragment {

    public static String EXTRA_MODULE_ID = "extra_module_id";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Map<String, Document> documents = new HashMap<>();
    private String moduleId;
    private DatabaseReference dbRef;
    private DatabaseReference documentRef;

    public static DocumentFragment newInstance(String moduleId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MODULE_ID, moduleId);
        DocumentFragment fragment = new DocumentFragment();
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

        recyclerView = view.findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DocumentRecyclerViewAdapter(documents);
        recyclerView.setAdapter(adapter);

        documentRef = dbRef.child("modules").child(moduleId).child("documents");
        documentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                documents.clear();

                for (DataSnapshot documentSnapshot : dataSnapshot.getChildren()) {
                    documents.put(documentSnapshot.getKey(), documentSnapshot.getValue(Document.class));
                }

                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(documents.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
