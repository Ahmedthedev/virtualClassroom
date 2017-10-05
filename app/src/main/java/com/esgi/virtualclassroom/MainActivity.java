package com.esgi.virtualclassroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esgi.virtualclassroom.fragments.HomeFragment;
import com.esgi.virtualclassroom.models.Message;
import com.esgi.virtualclassroom.fragments.RecorderFragment;
import com.esgi.virtualclassroom.models.User;
import com.esgi.virtualclassroom.utils.Tools;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DatabaseReference dbRef;
    DatabaseReference userRef;
    User currentUser;
    AppCompatActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        dbRef = FirebaseDatabase.getInstance().getReference();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            return;
        }

        userRef = dbRef.child("users").child(firebaseUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                Tools.switchFragment(activity, R.id.fragment_container, HomeFragment.newInstance(), true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
}