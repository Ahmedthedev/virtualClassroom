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

import java.util.ArrayList;
import java.util.List;

public class OffertEventFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Module> arraylistItems;

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

        arraylistItems = new ArrayList<>();

        for (int i=0; i < 10; i++) {
            User user = new User("nameOffert","email",true);
            Module listModule = new Module("Module"+i,"date start"+i,"date end"+i,user);
            arraylistItems.add(listModule);
        }
        adapter = new HomeRecyclerViewAdapter(this.getContext(),arraylistItems);

        recyclerView.setAdapter(adapter);
    }



}
