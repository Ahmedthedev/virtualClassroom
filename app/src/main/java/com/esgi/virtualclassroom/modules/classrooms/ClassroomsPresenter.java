package com.esgi.virtualclassroom.modules.classrooms;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;

import com.esgi.virtualclassroom.R;
import com.esgi.virtualclassroom.data.AuthenticationProvider;
import com.esgi.virtualclassroom.data.api.FirebaseProvider;
import com.esgi.virtualclassroom.data.models.Classroom;
import com.esgi.virtualclassroom.data.models.User;
import com.esgi.virtualclassroom.modules.classroom.ClassroomActivity;
import com.esgi.virtualclassroom.notifications.NotificationPublisher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import static com.esgi.virtualclassroom.modules.classroom.ClassroomActivity.EXTRA_CLASSROOM;
import static com.esgi.virtualclassroom.notifications.NotificationPublisher.CHANNEL_ID;
import static com.esgi.virtualclassroom.notifications.NotificationPublisher.CLASSROOM_NOTIFICATIONS_ID;

public class ClassroomsPresenter implements ClassroomsRecyclerViewAdapter.Listener, ClassroomsUpcomingDialogFragment.Listener {
    private static final String PERIOD_LIVE = "live";
    private static final String PERIOD_UPCOMING = "upcoming";
    private static final String PERIOD_PAST = "past";
    private ClassroomsView view;
    private Context context;
    private ArrayList<Classroom> classrooms;
    private String classroomsPeriod;
    private AuthenticationProvider authenticationProvider;
    private FirebaseProvider firebaseProvider;

    ClassroomsPresenter(Context context, ClassroomsView view, String classroomsPeriod) {
        this.view = view;
        this.context = context;
        this.classrooms = new ArrayList<>();
        this.classroomsPeriod = classroomsPeriod;
        this.authenticationProvider = AuthenticationProvider.getInstance();
        this.firebaseProvider = FirebaseProvider.getInstance();
    }

    public ArrayList<Classroom> getClassroomsList() {
        return classrooms;
    }

    private Query getClassrooms(String classroomsPeriod) {
        switch (classroomsPeriod.toLowerCase()) {
            case PERIOD_LIVE:
                return firebaseProvider.getClassroomsLive();
            case PERIOD_UPCOMING:
                return firebaseProvider.getClassroomsUpcoming();
            case PERIOD_PAST:
                return firebaseProvider.getClassroomsPast();
            default:
                return firebaseProvider.getClassroomsLive();
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
        view.loadImage(firebaseProvider.getFileRef(url), imageView);
    }

    @Override
    public void postSubscription(Classroom classroom) {
        User user = authenticationProvider.getCurrentUser();
        firebaseProvider.postSubscription(classroom, user)
                .addOnSuccessListener(aVoid -> {
                    scheduleUpcomingClassroomStartNotification(classroom, CLASSROOM_NOTIFICATIONS_ID);
                    scheduleClassroomEndNotification(classroom, CLASSROOM_NOTIFICATIONS_ID);
                });
    }

    @Override
    public void deleteSubscription(Classroom classroom) {
        User user = authenticationProvider.getCurrentUser();
        firebaseProvider.deleteSubscription(classroom, user);
    }

    private void scheduleUpcomingClassroomStartNotification(Classroom classroom, int notificationId) {
        String title = context.getString(R.string.upcoming_classroom_start_notification_content_title, classroom.getTitle());
        String contentText = classroom.getDescription();
        long time = classroom.getStart() - 600000;

        scheduleFutureClassroomNotification(classroom, title, contentText, time, notificationId);
    }

    private void scheduleClassroomEndNotification(Classroom classroom, int notificationId) {
        String title = context.getString(R.string.upcoming_classroom_end_notification_content_title, classroom.getTitle());
        String contentText = classroom.getDescription();
        long time = classroom.getEnd();

        scheduleFutureClassroomNotification(classroom, title, contentText, time, notificationId);
    }

    // TODO : wrong place?
    private void scheduleFutureClassroomNotification(Classroom classroom, String title, String contentText, long time, int notificationId) {
        Intent intent = new Intent(context, ClassroomActivity.class);
        intent.putExtra(EXTRA_CLASSROOM, classroom);
        PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.EXTRA_NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(NotificationPublisher.EXTRA_NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

    private void onUpcomingClassroomClick(Classroom classroom) {
        view.showPopupUpcomingClassroom(classroom);
    }

    @Override
    public void onAcceptClick(Classroom classroom) {
        User user = authenticationProvider.getCurrentUser();
        firebaseProvider.postSubscription(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    @Override
    public void onDeclineClick(Classroom classroom) {
        User user = authenticationProvider.getCurrentUser();
        firebaseProvider.deleteSubscription(classroom, user)
                .addOnFailureListener(Throwable::printStackTrace);
    }

    public void onResume() {
        getClassrooms(classroomsPeriod).addValueEventListener(listener);
    }

    public void onPause() {
        getClassrooms(classroomsPeriod).removeEventListener(listener);
    }

    // TODO : this is a mess!
    private ValueEventListener listener = new ValueEventListener() {
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
    };
}
