package com.deepanshu.notification2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import static com.deepanshu.notification2.StaticValue.HIGH_PRIORITY_CHANNEL_ID;
import static com.deepanshu.notification2.StaticValue.NOTIFICATION;


public class ChannelUtil {
    private static NotificationManager notificationManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    static void createSimpleNOtificationChannel(Context context) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        CharSequence name = "Personal Notification";
        String description = "include allthe personal notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION, name, importance);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationChannel.setDescription(description);
        notificationChannel.setShowBadge(false);
        notificationManager.createNotificationChannel(notificationChannel);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static void HighprioriytNotificationChannel(Context context) {
        if (notificationManager == null) {
            notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        String name = "High priorty Notification";
        String description = "include High prioriy notification"; // The user-visible description of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(HIGH_PRIORITY_CHANNEL_ID, name, importance);
        notificationChannel.setDescription(description);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.setShowBadge(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(notificationChannel);

    }

}
