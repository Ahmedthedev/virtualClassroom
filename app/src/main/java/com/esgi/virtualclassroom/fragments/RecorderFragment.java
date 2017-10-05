package com.esgi.virtualclassroom.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;

public class RecorderFragment extends Fragment implements RecognitionListener {
    private String currentSpeech;
    private TextView speechTextView;
    private ImageButton btnSpeak;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private boolean isListening;

    public static RecorderFragment newInstance() {
        return new RecorderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voice_recorder, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechTextView = view.findViewById(R.id.txtSpeechInput);
        btnSpeak = view.findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListening) {
                    speechRecognizer.stopListening();
                    isListening = false;
                } else {
                    speechRecognizer.startListening(recognizerIntent);
                    isListening = true;
                }
            }
        });
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
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.i("SPEECH", "onEvent");
    }
}
