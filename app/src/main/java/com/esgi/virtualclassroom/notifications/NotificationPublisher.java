package com.esgi.virtualclassroom.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

public class NotificationPublisher extends BroadcastReceiver {
    public static final String CHANNEL_ID = "virtual_classroom_channel";
    public static final int CLASSROOM_NOTIFICATIONS_ID = 50;
    public static final String EXTRA_NOTIFICATION = "notification";
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Notification notification = intent.getParcelableExtra(EXTRA_NOTIFICATION);
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager == null) {
             return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Upcoming classrooms notifications", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Warning about the upcoming classrooms you subscribed to");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 400, 400, 500, 400, 400, 200, 500});

            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(notificationId, notification);
    }
}
