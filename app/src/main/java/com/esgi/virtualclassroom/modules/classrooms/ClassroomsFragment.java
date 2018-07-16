package com.esgi.virtualclassroom.modules.classrooms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.modules.classroom.ClassroomActivity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassroomsFragment extends Fragment implements ClassroomsView {
    public static String EXTRA_CLASSROOMS_PERIOD = "extra_classrooms_period";
    private ClassroomsPresenter presenter;
    private ClassroomsRecyclerViewAdapter adapter;

    @BindView(R.id.fragment_home_recycler_view) RecyclerView recyclerView;

    public ClassroomsFragment() {}

    public static ClassroomsFragment newInstance(String classroomsPeriod) {
        Bundle args = new Bundle();
        args.putString(EXTRA_CLASSROOMS_PERIOD, classroomsPeriod);
        ClassroomsFragment fragment = new ClassroomsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            presenter = new ClassroomsPresenter(getActivity(), this, getArguments().getString(EXTRA_CLASSROOMS_PERIOD));
        }
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

        adapter = new ClassroomsRecyclerViewAdapter(presenter.getClassroomsList());
        adapter.setListener(presenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void goToClassroom(Classroom classroom) {
        Intent intent = new Intent(getActivity(), ClassroomActivity.class);
        intent.putExtra(ClassroomActivity.EXTRA_CLASSROOM, classroom);
        startActivity(intent);
    }

    @Override
    public void updateClassroomsList() {
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(presenter.getClassroomsList().size() - 1);
    }

    @Override
    public void showPopupUpcomingClassroom(Classroom classroom) {
        ClassroomsUpcomingDialogFragment upcomingClassroomModal = ClassroomsUpcomingDialogFragment.newInstance(classroom);
        upcomingClassroomModal.setListener(presenter);

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            upcomingClassroomModal.show(getFragmentManager(), upcomingClassroomModal.getTag());
        }
    }

    @Override
    public void loadImage(StorageReference ref, ImageView imageView) {
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(ref)
                .into(imageView);
    }
}
