package com.deepanshu.notification2;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    public static int NOTIFICATION_id = 001;
    private final String Channel_id = "Channel_Id";


    //jab hum fiebase ke though notification push karte hain tab ye method call hota h
    //firebase se bheja hua msg yaha ayega
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
       // remoteMessage.getNotification()....se puri information aa jayegi msg ki
        String refreshToken=FirebaseInstanceId.getInstance().getToken();
        showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    @SuppressLint("LongLogTag")
    private void showNotification(String title, String body) {
        //notifaction builder ke though notificaioton ka object define karte hain
        //kis tarah ki notifeictj ogi

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_id);
        builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//to cancep the message form above
        builder.setAutoCancel(true);
        //builder.setContentIntent(landingPendingIntent);//to call other activity or simple click to enter into the other activity
        //builder.addAction(R.drawable.ic_sms_black_24dp, "Yes", yesLandingInten);//these 2 yes no for cancel /or go to spectific setting
       // builder.addAction(R.drawable.ic_sms_black_24dp, "No", NoLandingInten);
        NotificationManager compat = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);//NotificationManagerCompat.from(this);
        compat.notify(NOTIFICATION_id, builder.build());
        builder.setAutoCancel(true);
       /* PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = Build.VERSION.SDK_INT >= 20 ? pm.isInteractive() : pm.isScreenOn(); // check if screen is on
        if (!isScreenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
            wl.acquire(2000); //set your time in milliseconds
        }
*/
        PowerManager pm = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        Log.e("screen on.................................", ""+isScreenOn);
        if(isScreenOn==false)
        {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
            wl.acquire(10000);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }


    }

}
