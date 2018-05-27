package com.esgi.virtualclassroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esgi.virtualclassroom.data.models.Module;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassroomActivity extends AppCompatActivity {
    public static String EXTRA_MODULE_ID = "extra_module_id";
    public static String EXTRA_IS_PROF = "extra_is_prof";
    public String moduleId;
    public boolean isProf;
    public Module module;
    public FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        moduleId = getIntent().getStringExtra(EXTRA_MODULE_ID);
        isProf = getIntent().getBooleanExtra(EXTRA_IS_PROF, false);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (moduleId == null || currentUser == null) {
            return;
        }

        DatabaseReference moduleRef = dbRef.child("modules").child(moduleId);
        moduleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                module = dataSnapshot.getValue(Module.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
