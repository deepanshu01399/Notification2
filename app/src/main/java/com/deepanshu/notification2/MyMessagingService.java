package com.deepanshu.notification2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static com.deepanshu.notification2.StaticValue.HIGH_PRIORITY_CHANNEL_ID;
import static com.deepanshu.notification2.StaticValue.NOTIFICATION_id;

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotifications(remoteMessage);
    }

    //TOdo if we want to send using FCM
   /* private void showNotifications(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, HIGH_PRIORITY_CHANNEL_ID)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_sms_black_24dp)
                .setContentIntent(pendingIntent);
        if (remoteMessage.getNotification().getImageUrl() != null) {
            Bitmap bitmap = getBitmapfromUrl(remoteMessage.getNotification().getImageUrl().toString());
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null)).setLargeIcon(bitmap);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_id, builder.build());
    }*/

    private void showNotifications(RemoteMessage remoteMessage) {
        //Todo if we send the notification from the postmen or server
        Date now = new Date();
        long DiffNOTIFICATION_id = now.getTime();//use date to generate an unique id to differentiate the notifications.
        String classType = remoteMessage.getData().get("classtype");
        String channelID = remoteMessage.getData().get("channelid");
        //it is use to choose the activity in which we want to go after click notification
        Intent intent = new Intent(this, getClassfromType(classType));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body")).setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_sms_black_24dp)
                .setContentIntent(pendingIntent);
        if (remoteMessage.getData().get("image") != null) {
            Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("image").toString());
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null)).setLargeIcon(bitmap);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) DiffNOTIFICATION_id, builder.build());
    }

    private Class<?> getClassfromType(String classType) {
        if (!(classType == null)) {
            //here we provide the activities in which we want the user enter after click the notification
            if (classType.equals("Landing"))
                return LandingActivity.class;
            else if (classType.equals("reply"))
                return ReviewActivity.class;
            else return MainActivity.class;
        }
        return MainActivity.class;
    }

    private Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }

    }

}
