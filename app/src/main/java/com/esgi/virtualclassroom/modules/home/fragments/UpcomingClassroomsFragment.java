package com.esgi.virtualclassroom.modules.home.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.modules.home.HomePresenter;
import com.esgi.virtualclassroom.modules.home.adapters.ClassroomRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpcomingClassroomsFragment extends Fragment implements ClassroomRecyclerViewAdapter.Listener {
    private HomePresenter presenter;

    @BindView(R.id.fragment_home_recycler_view) RecyclerView recyclerView;

    public UpcomingClassroomsFragment() {}

    public void setPresenter(HomePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> classrooms = new ArrayList<>();
        classrooms.add("Upcoming classroom 1");
        classrooms.add("Upcoming classroom 2");
        classrooms.add("Upcoming classroom 3");
        classrooms.add("Upcoming classroom 4");

        ClassroomRecyclerViewAdapter adapter = new ClassroomRecyclerViewAdapter(classrooms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClassroomClick(String classroom) {
        presenter.goToClassroom(classroom);
    }
}