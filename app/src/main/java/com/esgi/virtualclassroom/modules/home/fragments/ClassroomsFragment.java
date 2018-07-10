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
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.modules.home.HomePresenter;
import com.esgi.virtualclassroom.modules.home.adapters.ClassroomsRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassroomsFragment extends Fragment implements ClassroomsRecyclerViewAdapter.Listener {
    private HomePresenter presenter;

    @BindView(R.id.fragment_home_recycler_view) RecyclerView recyclerView;

    public ClassroomsFragment() {}

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

        ArrayList<Classroom> classrooms = new ArrayList<>();

        Classroom classroom1 = new Classroom();
        Classroom classroom2 = new Classroom();

        classroom1.setTitle("Current classroom 1");
        classroom2.setTitle("Current classroom 2");
        classroom1.setDescription("Current classroom 1 description bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla ");
        classroom2.setDescription("Current classroom 2 description bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla ");
        classrooms.add(classroom1);
        classrooms.add(classroom2);

        ClassroomsRecyclerViewAdapter adapter = new ClassroomsRecyclerViewAdapter(classrooms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClassroomClick(String classroom) {
        presenter.onClassroomItemClick(classroom);
    }
}
