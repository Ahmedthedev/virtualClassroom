package com.esgi.virtualclassroom.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.virtualclassroom.Adapters.HomeRecyclerViewAdapter;
import com.esgi.virtualclassroom.Adapters.HomeViewPagerAdapter;
import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.models.Module;
import com.esgi.virtualclassroom.models.User;

import java.util.ArrayList;
import java.util.List;

public class PastEventFragment extends Fragment {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Module> arraylistItems = new ArrayList<>();


    public static PastEventFragment newInstance() {
        return new PastEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_past_event, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.pastRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        arraylistItems = new ArrayList<>();


        adapter = new HomeRecyclerViewAdapter(this.getContext(),arraylistItems);

        recyclerView.setAdapter(adapter);
    }

}
