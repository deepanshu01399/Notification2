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
    private static NotificationManager notifManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    static void CreteNotificationChannel1(Context context) {
        if (notifManager == null) {
            notifManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        String name = "my_package_channel";
        String description = "my_package_first_channel"; // The user-visible description of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = notifManager.getNotificationChannel(HIGH_PRIORITY_CHANNEL_ID);
        if (mChannel == null) {
            mChannel = new NotificationChannel(HIGH_PRIORITY_CHANNEL_ID, name, importance);
            mChannel.setDescription(description);
            mChannel.enableVibration(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.setShowBadge(true);//to see the notificaiton icon on the app
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notifManager.createNotificationChannel(mChannel);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    static void createSimpleNOtificationChannel(Context context) {
        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        CharSequence name = "Personal NOtification";
        String description = "include allthe personal notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        //create notification nchannel
        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION, name, importance);
        // Sets whether notifications posted to this channel should display notification lights
        notificationChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        notificationChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        notificationChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationChannel.setDescription(description);
        notificationChannel.setShowBadge(false);

            /*NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);*///it can also be handle like this
        notifManager.createNotificationChannel(notificationChannel);

    }


}
