package com.esgi.virtualclassroom.modules.classrooms;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.AuthenticationProvider;
import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.User;
import com.esgi.virtualclassroom.notifications.NotificationPublisher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ClassroomsPresenter implements ClassroomsRecyclerViewAdapter.Listener, ClassroomsUpcomingDialogFragment.Listener {
    private static final String PERIOD_LIVE = "live";
    private static final String PERIOD_UPCOMING = "upcoming";
    private static final String PERIOD_PAST = "past";
    private ClassroomsView view;
    private Context context;
    private ArrayList<Classroom> classrooms;
    private String classroomsPeriod;
    private FirebaseProvider firebaseProvider;

    ClassroomsPresenter(Context context, ClassroomsView view, String classroomsPeriod) {
        this.view = view;
        this.context = context;
        this.classrooms = new ArrayList<>();
        this.classroomsPeriod = classroomsPeriod;
        this.firebaseProvider = FirebaseProvider.getInstance();
        this.init();
    }

    public ArrayList<Classroom> getClassroomsList() {
        return classrooms;
    }

    private void init() {
        this.getClassrooms(this.classroomsPeriod).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classrooms.clear();

                for (DataSnapshot classroomSnapshot: dataSnapshot.getChildren()) {
                    Classroom classroom = classroomSnapshot.getValue(Classroom.class);

                    if (classroom == null) {
                        continue;
                    }

                    classroom.setId(classroomSnapshot.getKey());

                    if (!classroomsPeriod.toLowerCase().equals(PERIOD_LIVE) || classroom.getStart() < new Date().getTime()) {
                        classrooms.add(classroom);
                    }

                    firebaseProvider.getSubscriptions(classroom).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot subscriptionsSnapshot: dataSnapshot.getChildren()) {
                                String userId = subscriptionsSnapshot.getKey();
                                classroom.getSubscriptions().add(userId);
                            }

                            view.updateClassroomsList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private Query getClassrooms(String classroomsPeriod) {
        switch (classroomsPeriod.toLowerCase()) {
            case PERIOD_LIVE:
                return this.firebaseProvider.getClassroomsLive();
            case PERIOD_UPCOMING:
                return this.firebaseProvider.getClassroomsUpcoming();
            case PERIOD_PAST:
                return this.firebaseProvider.getClassroomsPast();
            default:
                return this.firebaseProvider.getClassroomsLive();
        }
    }

    @Override
    public void onClassroomClick(Classroom classroom) {
        switch (classroomsPeriod.toLowerCase()) {
            case PERIOD_LIVE:
                view.goToClassroom(classroom);
                break;
            case PERIOD_UPCOMING:
                onUpcomingClassroomClick(classroom);
                break;
            case PERIOD_PAST:
                view.goToClassroom(classroom);
                break;
        }
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        this.view.loadImage(this.firebaseProvider.getFileRef(url), imageView);
    }

    @Override
    public void postSubscription(Classroom classroom) {
        User user = AuthenticationProvider.getCurrentUser();
        this.firebaseProvider.postSubscription(classroom, user)
                .addOnSuccessListener(aVoid -> scheduleNotification(context, classroom.getStart(), 50));
    }

    @Override
    public void deleteSubscription(Classroom classroom) {
        User user = AuthenticationProvider.getCurrentUser();
        this.firebaseProvider.deleteSubscription(classroom, user);
    }

    public void scheduleNotification(Context context, long notificationDate, int notificationId) {
        BitmapDrawable largeIconDrawable = (BitmapDrawable) ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
        if (largeIconDrawable == null) {
            return;
        }

        Bitmap largeIcon = largeIconDrawable.getBitmap();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "666")
                .setContentTitle("Bases du java dans 30 minutes!")
                .setContentText("Ne manquez pas le cours Bases du java dans 30 minutes!")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(largeIcon)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent intent = new Intent(context, Classroom.class);
        PendingIntent activity = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(activity);

        Notification notification = builder.build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        long future = SystemClock.elapsedRealtime() + 60000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, future, pendingIntent);
        }
    }

    private void onUpcomingClassroomClick(Classroom classroom) {
        view.showPopupUpcomingClassroom(classroom);
    }

    @Override
    public void onAcceptClick(Classroom classroom) {
        User user = AuthenticationProvider.getCurrentUser();
        this.firebaseProvider.postSubscription(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    @Override
    public void onDeclineClick(Classroom classroom) {
        User user = AuthenticationProvider.getCurrentUser();
        this.firebaseProvider.deleteSubscription(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }
}
