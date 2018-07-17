package com.esgi.virtualclassroom.data.api;

import android.graphics.Bitmap;
import android.os.Environment;

import com.esgi.virtualclassroom.data.models.Attachment;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

public class FirebaseProvider {
    private static FirebaseProvider instance;
    private static final String CLASSROOMS_REFERENCE = "classrooms";
    private static final String USERS_REFERENCE = "users";
    private static final String MESSAGES_REFERENCE = "messages";
    private static final String ATTACHMENTS_REFERENCE = "attachments";
    private static final String VIEWERS_REFERENCE = "viewers";
    private static final String SUBSCRIPTIONS_REFERENCE = "subscriptions";
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

    public FirebaseUser getCurrentFirebaseUser() {
        return firebaseAuth.getCurrentUser();
    }

    public Task<AuthResult> signIn(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
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
        return dbRef.child(CLASSROOMS_REFERENCE).orderByChild("start").startAt(new Date().getTime());
    }

    public Query getClassroomsPast() {
        return dbRef.child(CLASSROOMS_REFERENCE).orderByChild("end").endAt(new Date().getTime());
    }

    public Query getClassroomsLive() {
        return dbRef.child(CLASSROOMS_REFERENCE).orderByChild("end").startAt(new Date().getTime());
    }

    public DatabaseReference getUser(String id) {
        return dbRef.child(USERS_REFERENCE).child(id);
    }

    public Task<Void> postUser(User user) {
        DatabaseReference userRef = getUser(user.getUid());
        return userRef.setValue(user);
    }

    public DatabaseReference getClassrooms() {
        return dbRef.child(CLASSROOMS_REFERENCE);
    }

    public DatabaseReference getClassroom(Classroom classroom) {
        return dbRef.child(CLASSROOMS_REFERENCE).child(classroom.getId());
    }

    public DatabaseReference getMessages(Classroom classroom) {
        return dbRef.child(MESSAGES_REFERENCE).child(classroom.getId());
    }

    public DatabaseReference getAttachments(Classroom classroom) {
        return dbRef.child(ATTACHMENTS_REFERENCE).child(classroom.getId());
    }

    public DatabaseReference getViewers(Classroom classroom) {
        return dbRef.child(VIEWERS_REFERENCE).child(classroom.getId());
    }

    public DatabaseReference getSubscriptions(Classroom classroom) {
        return dbRef.child(SUBSCRIPTIONS_REFERENCE).child(classroom.getId());
    }

    public Task<Void> postClassroom(Classroom classroom) {
        DatabaseReference classroomRef = getClassrooms().push();
        classroom.setId(classroomRef.getKey());
        return classroomRef.setValue(classroom);
    }

    public Task<Void> postMessage(Classroom classroom, Message message) {
        DatabaseReference messageRef = getMessages(classroom).push();
        return messageRef.setValue(message);
    }

    public Task<Void> postViewer(Classroom classroom, User user) {
        DatabaseReference viewersRef = getViewers(classroom).child(user.getUid());
       return viewersRef.setValue(true);
    }

    public Task<Void> deleteViewer(Classroom classroom, User user) {
        DatabaseReference viewersRef = getViewers(classroom).child(user.getUid());
        return viewersRef.setValue(null);
    }

    public Task<Void> postSubscription(Classroom classroom, User user) {
        DatabaseReference subscriptionsRef = getSubscriptions(classroom).child(user.getUid());
        return subscriptionsRef.setValue(true);
    }

    public Task<Void> deleteSubscription(Classroom classroom, User user) {
        DatabaseReference subscriptionsRef = getSubscriptions(classroom).child(user.getUid());
        return subscriptionsRef.setValue(null);
    }

    public UploadTask uploadImage(Classroom classroom, String name, Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] data = stream.toByteArray();
        StorageReference imageRef = storageRef.child(CLASSROOMS_REFERENCE).child(classroom.getId()).child(name);
        return imageRef.putBytes(data);
    }

    public StorageReference getFileRef(String url) {
        return storageRef.child(url);
    }

    public FileDownloadTask downloadFile(Attachment attachment) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), attachment.getName());
        return storageRef.child(attachment.getUrl()).getFile(file);
    }
}
