package com.esgi.virtualclassroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.esgi.virtualclassroom.fragments.CreateAccountFragment;
import com.esgi.virtualclassroom.fragments.LogInFragment;
import com.esgi.virtualclassroom.models.User;
import com.esgi.virtualclassroom.utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity implements
        CreateAccountFragment.OnCreateAccountInteractionListener,
        LogInFragment.OnLoginInteractionListener
{
    private DatabaseReference dbRef;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        dbRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            goToHomeScreen();
        } else {
            goToLoginScreen();
        }
    }

    @Override
    public void logInWithEmailPassword(String email, String password) {
        Tools.closeKeyboard(this);
        showProgressDialog();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                hideProgressDialog();
                goToHomeScreen();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getApplicationContext(), getString(R.string.fragment_log_in_auth_conflict_msg), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.fragment_log_in_email_error_msg), Toast.LENGTH_SHORT).show();
                }

                logOut();
                hideProgressDialog();
            }
        });
    }

    @Override
    public void createUserWithEmailAndPassword(String email, String password, final String name) {
        Tools.closeKeyboard(this);
        showProgressDialog();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                final FirebaseUser user = authResult.getUser();
                User profile = new User(name, user.getEmail(), null, false);
                dbRef.child("users").child(user.getUid()).setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hideProgressDialog();
                        goToHomeScreen();
                    }
                });
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(getApplicationContext(), getString(R.string.fragment_log_in_auth_conflict_msg), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.fragment_log_in_email_error_msg), Toast.LENGTH_SHORT).show();
                }

                logOut();
                hideProgressDialog();
            }
        });
    }

    @Override
    public void goToCreateAccountScreen() {
        Tools.switchFragment(this, R.id.fragment_container, CreateAccountFragment.newInstance(), true);
    }

    private void goToLoginScreen() {
        Tools.switchFragment(this, R.id.fragment_container, LogInFragment.newInstance(), false);
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void logOut() {
        firebaseAuth.signOut();
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.fragment_log_in_loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
}