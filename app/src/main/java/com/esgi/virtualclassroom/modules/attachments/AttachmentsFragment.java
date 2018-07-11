package com.esgi.virtualclassroom.modules.attachments;

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
import com.esgi.virtualclassroom.data.models.Classroom;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttachmentsFragment extends BottomSheetDialogFragment implements AttachmentsView {
    public static String EXTRA_CLASSROOM = "extra_classroom";
    private AttachmentsPresenter presenter;
    private RecyclerView.Adapter adapter;

    @BindView(R.id.attachments_recycler_view) RecyclerView recyclerView;

    public AttachmentsFragment() {}

    public static AttachmentsFragment newInstance(Classroom classroom) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CLASSROOM, classroom);
        AttachmentsFragment fragment = new AttachmentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            presenter = new AttachmentsPresenter(this, getArguments().getParcelable(EXTRA_CLASSROOM));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attachments, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AttachmentsRecyclerViewAdapter(presenter.getAttachmentsList());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateAttachmentsList() {
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(presenter.getAttachmentsList().size() - 1);
    }
}
