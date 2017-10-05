package com.esgi.virtualclassroom.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.virtualclassroom.Adapters.HomeRecyclerViewAdapter;
import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.models.Module;
import com.esgi.virtualclassroom.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OffertEventFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Module> arraylistItems = new ArrayList<>();

    DatabaseReference dbRef;
    DatabaseReference moduleRef;

    public static OffertEventFragment newInstance() {
        return new OffertEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offert_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = (RecyclerView) getView().findViewById(R.id.offerRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter = new HomeRecyclerViewAdapter(this.getContext(),arraylistItems);
        recyclerView.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference();
        moduleRef = dbRef.child("modules");
        moduleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot moduleSnapshot: dataSnapshot.getChildren()) {
                    arraylistItems.add(moduleSnapshot.getValue(Module.class));
                }

                adapter.notifyDataSetChanged();

               // currentUser = dataSnapshot.getValue(User.class);

                // switchFragment(RecorderFragment.newInstance(), true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

    }



}
