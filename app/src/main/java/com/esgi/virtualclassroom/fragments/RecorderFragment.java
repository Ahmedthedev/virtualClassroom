package com.esgi.virtualclassroom.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.models.Module;
import com.esgi.virtualclassroom.views.DrawingView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecorderFragment extends Fragment implements RecognitionListener {
    public static String EXTRA_MODULE_ID = "extra_module_id";
    private String moduleId;
    private Module module;
    private DatabaseReference dbRef;
    private DatabaseReference moduleRef;
    private TextView speechTextView;
    private ImageButton btnSpeak;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private boolean isListening;
    private FrameLayout drawingViewContainer;
    private Button sendImage;

    DrawingView dv ;
    private Paint mPaint;

    public static RecorderFragment newInstance(String moduleId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MODULE_ID, moduleId);
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
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
        dv = new DrawingView(this.getContext(),mPaint);
        drawingViewContainer.addView(dv);
        Button done = v.findViewById(R.id.draw);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dv.uploadFile();
                System.out.println("1-touch btn");
            }
        });
        Button clear = v.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dv.clear();
                System.out.println("1-touch clear");
            }
        });
        //dv.uploadFile();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbRef = FirebaseDatabase.getInstance().getReference();
        moduleId = getArguments().getString(EXTRA_MODULE_ID);
       // drawingViewContainer = view.findViewById(R.id.view_drawing_container);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        moduleRef = dbRef.child("modules").child(moduleId);
        moduleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                module = dataSnapshot.getValue(Module.class);
                //updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);



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

    private void updateUI() {
        speechTextView.setText(module.speechText);
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
