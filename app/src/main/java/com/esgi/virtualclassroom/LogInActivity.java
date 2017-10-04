package com.esgi.virtualclassroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.esgi.virtualclassroom.fragments.CreateAccountFragment;
import com.esgi.virtualclassroom.fragments.LogInFragment;
import com.esgi.virtualclassroom.utils.Tools;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LogInActivity extends AppCompatActivity implements
        CreateAccountFragment.OnCreateAccountInteractionListener,
        LogInFragment.OnLoginInteractionListener
{
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        firebaseAuth = FirebaseAuth.getInstance();
        logOut();
        Tools.printDeviceHash(this);
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
                FirebaseUser user = authResult.getUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                updateUser(user, profileUpdates);
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

    private void updateUser(FirebaseUser user, UserProfileChangeRequest profileUpdates) {
        user.updateProfile(profileUpdates).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgressDialog();
                goToHomeScreen();
            }
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

    @Override
    public void goToCreateAccountScreen() {
        switchFragment(CreateAccountFragment.newInstance(), true);
    }

    private void goToLoginScreen() {
        switchFragment(LogInFragment.newInstance(), false);
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