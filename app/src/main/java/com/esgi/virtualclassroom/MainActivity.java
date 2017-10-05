package com.esgi.virtualclassroom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esgi.virtualclassroom.fragments.HomeFragment;
import com.esgi.virtualclassroom.fragments.RecorderFragment;
import com.esgi.virtualclassroom.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.esgi.virtualclassroom.utils.Tools;

public class MainActivity extends AppCompatActivity {
    DatabaseReference dbRef;
    DatabaseReference userRef;
    DatabaseReference moduleRef;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                switchFragment(HomeFragment.newInstance(), true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void switchFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        fragment.setRetainInstance(true);
        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
        Tools.closeKeyboard(this);
    }
}