package com.esgi.virtualclassroom.modules.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.models.Classroom;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatFragment extends BottomSheetDialogFragment implements ChatView {
    public static String EXTRA_CLASSROOM = "extra_classroom";
    private ChatPresenter presenter;
    private RecyclerView.Adapter adapter;

    @BindView(R.id.message_edit_text) EditText msgEditText;
    @BindView(R.id.chat_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.send_button) Button sendMsgButton;

    @OnClick(R.id.send_button)
    public void onSendMessageButtonClick() {
        String text = msgEditText.getText().toString();
        presenter.onSendMessageButtonClick(text);
    }

    public ChatFragment() {}

    public static ChatFragment newInstance(Classroom classroom) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CLASSROOM, classroom);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            presenter = new ChatPresenter(this, getArguments().getParcelable(EXTRA_CLASSROOM));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendMsgButton.setFocusable(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatRecyclerViewAdapter(presenter.getMessagesList());
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
    public void updateMessagesList() {
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(presenter.getMessagesList().size() - 1);
    }

    @Override
    public void sendMessageComplete() {
        recyclerView.scrollToPosition(presenter.getMessagesList().size() - 1);
        msgEditText.setText(null);
    }
}
