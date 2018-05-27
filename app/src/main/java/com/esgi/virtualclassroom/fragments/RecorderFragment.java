package com.esgi.virtualclassroom.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.models.Module;
import com.esgi.virtualclassroom.views.DrawingView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecorderFragment extends Fragment implements RecognitionListener {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public static String EXTRA_MODULE_ID = "extra_module_id";
    public static String EXTRA_IS_PROF = "extra_is_prof";
    private String moduleId;
    private boolean isProf;
    private Module module;
    private DatabaseReference dbRef;
    private DatabaseReference moduleRef;
    private String moduleName;
    private TextView speechTextView;
    private ImageButton btnSpeak;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private String currentSpeech;
    private boolean isListening;
    private FrameLayout drawingViewContainer;
    private Button sendImage;

    DrawingView dv ;
    private Paint mPaint;
    private ScrollView scrollView;
    private LinearLayout micLayout;

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    public static RecorderFragment newInstance(String moduleId, boolean isProf) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MODULE_ID, moduleId);
        args.putBoolean(EXTRA_IS_PROF, isProf);
        RecorderFragment fragment = new RecorderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_voice_recorder, container, false);
        drawingViewContainer = v.findViewById(R.id.layout_drawing_container);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        moduleName = getArguments().getString(EXTRA_MODULE_ID);
        dv = new DrawingView(this.getContext(),mPaint,getArguments().getString(EXTRA_MODULE_ID),module);
        drawingViewContainer.addView(dv);
        Button done = v.findViewById(R.id.draw);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dv.clear();
                dv.uploadFile();
            }
        });

        Button clear = v.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dv.clear();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbRef = FirebaseDatabase.getInstance().getReference();
        moduleId = getArguments().getString(EXTRA_MODULE_ID);
        isProf = getArguments().getBoolean(EXTRA_IS_PROF);

        requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        moduleRef = dbRef.child("modules").child(moduleId);
        moduleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                module = dataSnapshot.getValue(Module.class);
                speechTextView.setText(module.speechText);
                scrollView.scrollTo(0, scrollView.getBottom());
                //updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        scrollView = view.findViewById(R.id.speech_scroll_view);
        micLayout = view.findViewById(R.id.layout_mic_button);
        micLayout.setVisibility(isProf ? View.VISIBLE : View.GONE);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR");

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }

        if (!permissionToRecordAccepted ) {
            getActivity().finish();
        }
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
        currentSpeech = module.speechText;
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
        ArrayList<String> voiceText = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (voiceText == null) {
            return;
        }

        moduleRef.child("speechText").setValue(voiceText.get(0));
        scrollView.scrollTo(0, scrollView.getBottom());
        Log.i("SPEECH", "onPartialResults");
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.i("SPEECH", "onEvent");
    }
}
