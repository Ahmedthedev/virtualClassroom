package com.esgi.virtualclassroom.modules.classroom;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.modules.classroom.fragments.ChatFragment;
import com.esgi.virtualclassroom.modules.classroom.fragments.AttachmentsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassroomActivity extends AppCompatActivity implements ClassroomView {
    private ClassroomPresenter presenter;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @OnClick(R.id.classroom_chat_text_view)
    public void showChatModal() {
        String moduleId = "-KvcZc4hlr8PXpVDzsIj";
        ChatFragment bottomSheetFragment = ChatFragment.newInstance(moduleId);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @OnClick(R.id.classroom_documents_text_view)
    public void showDocumentsModal() {
        String moduleId = "-KvcZc4hlr8PXpVDzsIj";
        AttachmentsFragment bottomSheetFragment = AttachmentsFragment.newInstance(moduleId);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ClassroomPresenter(this);
        setContentView(R.layout.activity_classroom);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Classroom");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
