package com.deepanshu.notification2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.deepanshu.notification2.StaticValue.HIGH_PRIORITY_CHANNEL_ID;
import static com.deepanshu.notification2.StaticValue.NOTIFICATION_id;

public class MyMessagingService extends FirebaseMessagingService {
    //jab hum fiebase ke though notification push karte hain tab ye method call hota h
    //firebase se bheja hua msg yaha ayega
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotifications(remoteMessage);
        // remoteMessage.getNotification()....se puri information aa jayegi msg ki
       // String refreshToken = FirebaseInstanceId.getInstance().getToken();
        // RemoteMessage.Notification notificaitondatat=remoteMessage.getNotification();
        // showNotification(notificaitondatat.getTitle(),notificaitondatat.getBody(),remoteMessage);
    }
        private void showNotifications(RemoteMessage remoteMessage) {
        Log.d("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, HIGH_PRIORITY_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_sms_black_24dp).setContentIntent(pendingIntent);
            if (remoteMessage.getNotification().getImageUrl() != null) {
                //Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("image"));//it is used to send the extra data
              Bitmap bitmap= getBitmapfromUrl( remoteMessage.getNotification().getImageUrl().toString());
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null)).setLargeIcon(bitmap);
            }
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               ChannelUtil.CreteNotificationChannel1(this);
            }
            manager.notify(0, builder.build());
        }


    private void showNotification(String title, String body, RemoteMessage remoteMessage)
    {
        //notifaction builder ke though notificaioton ka object define karte hain
        //kis tarah ki notification oayegi
        // Toast.makeText(this, "notification type  "+notification_type, Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, HIGH_PRIORITY_CHANNEL_ID);
        String msgimage_url = remoteMessage.getData().get("image");
        String channelId = null;
        if (remoteMessage.getNotification().getChannelId() != null) {
            channelId = remoteMessage.getNotification().getChannelId();
        }
        Toast.makeText(this, "chanel :" + channelId + "\n msgimageUrl " + msgimage_url, Toast.LENGTH_SHORT).show();
        if (remoteMessage.getNotification().getImageUrl() != null) {
            Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("image-url"));//image-url kam nahi kar raha
            builder.setStyle(
                    new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .bigLargeIcon(null)
            ).setLargeIcon(bitmap);
        }
        builder.setSmallIcon(R.drawable.ic_sms_black_24dp);
        String title1 = remoteMessage.getData().get("title1");
        builder.setContentTitle(title1);
        builder.setContentText(body);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//to cancel the message form above
        builder.setAutoCancel(true);
        NotificationManager compat = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);//NotificationManagerCompat.from(this);
        compat.notify(NOTIFICATION_id, builder.build());
        builder.setAutoCancel(true);
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
