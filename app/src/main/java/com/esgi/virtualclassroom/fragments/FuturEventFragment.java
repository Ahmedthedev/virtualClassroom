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
import com.esgi.virtualclassroom.adapters.HomeRecyclerViewAdapter;
import com.esgi.virtualclassroom.models.Module;
import com.esgi.virtualclassroom.models.User;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class FuturEventFragment extends Fragment {
    DatabaseReference dbRef;
    DatabaseReference userRef;
    DatabaseReference moduleRef;
    User currentUser;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Module> arraylistItems = new ArrayList<>();

    public static FuturEventFragment newInstance() {
        return new FuturEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_futur_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() == null) {
            return;
        }

        recyclerView = getView().findViewById(R.id.futurRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new HomeRecyclerViewAdapter(getActivity(), arraylistItems);
        recyclerView.setAdapter(adapter);
    }
}