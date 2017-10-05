package com.esgi.virtualclassroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esgi.virtualclassroom.fragments.ChatFragment;
import com.esgi.virtualclassroom.models.Module;
import com.esgi.virtualclassroom.utils.Tools;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassroomActivity extends AppCompatActivity {
    private DatabaseReference dbRef;
    private DatabaseReference moduleRef;
    public static String EXTRA_MODULE_ID = "extra_module_id";
    public String moduleId;
    public Module module;
    public FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        dbRef = FirebaseDatabase.getInstance().getReference();
        moduleId = getIntent().getStringExtra(EXTRA_MODULE_ID);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (moduleId == null || currentUser == null) {
            return;
        }

        Tools.switchFragment(this, R.id.chat_fragment_container, ChatFragment.newInstance(moduleId), true);

        moduleRef = dbRef.child("modules").child(moduleId);
        moduleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                module = dataSnapshot.getValue(Module.class);
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI() {

    }
}
