package com.esgi.virtualclassroom.data.api;

import android.graphics.Bitmap;

import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.Message;
import com.esgi.virtualclassroom.data.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class FirebaseProvider {
    private static FirebaseProvider instance;
    private static final String CLASSROOM_REFERENCE = "classrooms";
    private static final String MESSAGES_REFERENCE = "messages";
    private static final String ATTACHMENTS_REFERENCE = "attachments";
    private static final String VIEWERS_REFERENCE = "viewers";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;
    private StorageReference storageRef;

    private FirebaseProvider() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.dbRef = FirebaseDatabase.getInstance().getReference();
        this.storageRef = FirebaseStorage.getInstance().getReference();
    }

    public static FirebaseProvider getInstance() {
        if (instance == null) {
            instance = new FirebaseProvider();
        }

        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public Task<AuthResult> signIn(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public Task<AuthResult> createUser(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<Void> updateUser(AuthResult authResult, String username) {
        final FirebaseUser user = authResult.getUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
        return user.updateProfile(profileUpdates);
    }

    public Query getClassroomsUpcoming() {
        return this.dbRef.child(CLASSROOM_REFERENCE).orderByChild("start/time").startAt(new Date().getTime());
    }

    public Query getClassroomsPast() {
        return this.dbRef.child(CLASSROOM_REFERENCE).orderByChild("end/time").endAt(new Date().getTime());
    }

    public Query getClassroomsLive() {
        return this.dbRef.child(CLASSROOM_REFERENCE).orderByChild("end/time").startAt(new Date().getTime());
    }

    public DatabaseReference getClassroom(Classroom classroom) {
        return this.dbRef.child(CLASSROOM_REFERENCE).child(classroom.getId());
    }

    public DatabaseReference getMessages(Classroom classroom) {
        return this.dbRef.child(MESSAGES_REFERENCE).child(classroom.getId());
    }

    public DatabaseReference getAttachments(Classroom classroom) {
        return this.dbRef.child(ATTACHMENTS_REFERENCE).child(classroom.getId());
    }

    public Task<Void> postMessage(Classroom classroom, Message message) {
        DatabaseReference messageRef = getMessages(classroom).push();
        // TODO : bug user uid not saved in database
        return messageRef.setValue(message);
    }

    public UploadTask uploadImage(Classroom classroom, String name, Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] data = stream.toByteArray();
        StorageReference imageRef = this.storageRef.child(classroom.getId()).child(name);
        return imageRef.putBytes(data);
    }

    public StorageReference getFile(Classroom classroom, String name) {
        return this.storageRef.child(classroom.getId()).child(name);
    }

    public DatabaseReference getViewers(Classroom classroom) {
        return this.dbRef.child(VIEWERS_REFERENCE).child(classroom.getId());
    }

    public Task<Void> postViewer(Classroom classroom, User user, boolean isViewer) {
        DatabaseReference viewersRef = getViewers(classroom).child(user.getUid());

        if (isViewer) {
           return viewersRef.setValue(isViewer);
        } else {
            return viewersRef.setValue(null);
        }
    }

    public void subscribeClassroom(Classroom classroom, User user) {
        // TODO : subscribe to notifications
    }
}
