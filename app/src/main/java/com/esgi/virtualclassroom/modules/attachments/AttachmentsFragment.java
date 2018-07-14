package com.esgi.virtualclassroom.modules.attachments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.models.Classroom;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttachmentsFragment extends BottomSheetDialogFragment implements AttachmentsView {
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 201;
    public static String EXTRA_CLASSROOM = "extra_classroom";
    private AttachmentsPresenter presenter;
    private AttachmentsRecyclerViewAdapter adapter;

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

        if (this.getActivity() == null) {
            return;
        }

        if (this.getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
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
        adapter.setListener(this.presenter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateAttachmentsList() {
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(presenter.getAttachmentsList().size() - 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.presenter.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void openFile(File file) {
        if (getActivity() == null) {
            return;
        }

        Uri uri = FileProvider.getUriForFile(this.getActivity(), "com.esgi.virtualclassroom", file);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent.createChooser(intent, "Choose an application");

        if (intent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void exit() {
        this.dismiss();
    }
}
