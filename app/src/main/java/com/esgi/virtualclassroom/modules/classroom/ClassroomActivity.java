package com.esgi.virtualclassroom.modules.classroom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.AuthenticationProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.User;
import com.esgi.virtualclassroom.modules.attachments.AttachmentsFragment;
import com.esgi.virtualclassroom.modules.chat.ChatFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassroomActivity extends AppCompatActivity implements ClassroomView, RecognitionListener {
    public static final String EXTRA_CLASSROOM = "extra_classroom";
    public static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private ClassroomPresenter presenter;
    private DrawingView drawingView;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private Classroom classroom;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.speech_scroll_view) ScrollView speechScrollView;
    @BindView(R.id.speech_text_view) TextView speechTextView;
    @BindView(R.id.layout_drawing_container) FrameLayout layoutDrawingContainer;
    @BindView(R.id.speech_button) FloatingActionButton speechButton;
    @BindView(R.id.send_drawing_button) Button sendDrawingButton;
    @BindView(R.id.clear_drawing_button) Button clearDrawingButton;

    @OnClick(R.id.speech_button)
    public void onSpeechButtonClick() {
        this.presenter.onSpeechButtonClick();
    }

    @OnClick(R.id.send_drawing_button)
    public void onSendDrawingClick() {
        this.presenter.onSendDrawingClick(this.drawingView.getBitmap());
    }

    @OnClick(R.id.clear_drawing_button)
    public void onClearDrawingClick() {
        this.presenter.onClearDrawingClick();
    }

    @OnClick(R.id.fabChat)
    public void onChatItemClick() {
        this.presenter.onChatItemClick();
    }

    @OnClick(R.id.fabAttachment)
    public void onAttachmentsItemClick() {
        this.presenter.onAttachmentsItemClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        ButterKnife.bind(this);

        classroom = getIntent().getParcelableExtra(EXTRA_CLASSROOM);
        presenter = new ClassroomPresenter(this, classroom);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(classroom.getTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }

        this.init();
    }

    private void init() {
        drawingView = new DrawingView(this);
        layoutDrawingContainer.addView(drawingView);

        User user = AuthenticationProvider.getCurrentUser();
        if (user != null && classroom.getTeacher().getUid().equals(user.getUid())) {
            layoutDrawingContainer.setVisibility(View.VISIBLE);
            speechButton.setVisibility(View.VISIBLE);
            sendDrawingButton.setVisibility(View.VISIBLE);
            clearDrawingButton.setVisibility(View.VISIBLE);
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 60000);
        recognizerIntent.putExtra("android.speech.extra.DICTATION_MODE", true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.presenter.onRequestPermissionsResult(requestCode, grantResults);
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

    @Override
    public void showChatFragment(ChatFragment fragment) {
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    @Override
    public void showAttachmentFragment(AttachmentsFragment fragment) {
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    @Override
    public void updateView(Classroom classroom) {
        speechTextView.setText(classroom.getSpeechText());
        speechScrollView.scrollTo(0, speechScrollView.getBottom());
    }

    @Override
    protected void onStop() {
        this.presenter.onStop();
        this.speechRecognizer.destroy();
        super.onStop();
    }

    @Override
    public void clearDrawing() {
        drawingView.clear();
    }

    @Override
    public void startSpeech() {
        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    public void stopSpeech() {
        speechRecognizer.stopListening();
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.i("SPEECH", "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i("SPEECH", "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float v) {
        Log.i("SPEECH", "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.i("SPEECH", "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.i("SPEECH", "onEndOfSpeech");
    }

    @Override
    public void onError(int i) {
        switch (i) {
            case SpeechRecognizer.ERROR_AUDIO:
                Log.i("SPEECH ERROR", "Error speech tiemout");

                break;
            case SpeechRecognizer.ERROR_CLIENT:
                Log.i("SPEECH ERROR", "Error client");

                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                Log.i("SPEECH ERROR", "Error insufficient permissions");

                break;
            case SpeechRecognizer.ERROR_NETWORK:
                Log.i("SPEECH ERROR", "Error network");

                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                Log.i("SPEECH ERROR", "Error network timeout");

                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                Log.i("SPEECH ERROR", "Error no match");

                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                Log.i("SPEECH ERROR", "Error recognizer busy");

                break;
            case SpeechRecognizer.ERROR_SERVER:
                Log.i("SPEECH ERROR", "Error server");

                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                Log.i("SPEECH ERROR", "Error speech tiemout");
                break;
        }
    }

    @Override
    public void onResults(Bundle bundle) {
        Log.i("SPEECH", "onResults");
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        Log.i("SPEECH", "onPartialResults");

        ArrayList<String> voiceText = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (voiceText == null) {
            return;
        }

        this.presenter.updateSpeechText(voiceText.get(0));
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.i("SPEECH", "onEvent");
    }
}
