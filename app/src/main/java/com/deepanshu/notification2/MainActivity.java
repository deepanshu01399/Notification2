package com.deepanshu.notification2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button openReplyIntent, openYesNoIntent, openotherActivity,imageNoti,headerNOti,expendedNoti;
    private NotificationManager mManager;
    private final String Channel_id = "Notification";
    String id = "my_package_channel_1"; // The user-visible name of the channel.

    public static int NOTIFICATION_id = 001;
    public static final String TXT_reply = "text_reply";
    String replyLabel = "Enter your reply here";
    private NotificationManager notifManager;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openReplyIntent = findViewById(R.id.openReplyIntent);
        openYesNoIntent = findViewById(R.id.openyesNOinten);
        openotherActivity= findViewById(R.id.openNewIntent);
        imageNoti = findViewById(R.id.ImageNotification);
        headerNOti = findViewById(R.id.HeaderNOtification);;
        expendedNoti = findViewById(R.id.expendNotificaiton);
        openotherActivity.setOnClickListener(this);
        openYesNoIntent.setOnClickListener(this);
        openReplyIntent.setOnClickListener(this);
        imageNoti.setOnClickListener(this);
        headerNOti.setOnClickListener(this);
        expendedNoti.setOnClickListener(this);
        //this send's Notification to a particalar grounp
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Msg subscribe";
                        if (!task.isSuccessful()) {
                            msg = "msg subscribe failed";
                        }
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        // to get the token and send this token to the server
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        // Log and toast
                        String msg = token;
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openyesNOinten:
                dispalayNotificatonForYesNO(v);
                break;
            case R.id.openNewIntent:
                dispalayNotificatonSimple(v);
                break;
            case R.id.openReplyIntent:
                dispalayNotificatonForReply(v);
                break;
            case R.id.ImageNotification:
                CreateImageNotification();
                break;
            case R.id.expendNotificaiton:
                ExpendedContentArea();
                break;
            case R.id.HeaderNOtification:
                createHeaderNotification("High priority message");
                break;
        }
    }

    public void dispalayNotificatonSimple(View View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNOtificationChannerl();
        }
        Intent LandingIntent = new Intent(this, LandingActivity.class);
        // LandingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent landingPendingIntent = PendingIntent.getActivity(this, 0, LandingIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_id);
        builder.setSmallIcon(R.drawable.ic_sms_black_24dp);
        builder.setContentTitle("Simple Notification");
        builder.setContentText("this is a simple notification..");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//to cancep the message form above
        builder.setAutoCancel(true);
        builder.setContentIntent(landingPendingIntent);//to call other activity or simple click to enter into the other activity

        /*  // final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_id);
        builder.setSmallIcon(R.drawable.ic_arrow_downward_black_24dp);
        builder.setContentTitle("Image download");
        builder.setContentText("download in progress ..");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//to cancep the message form above
       // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            remoteInput = new RemoteInput.Builder(TXT_reply).setLabel(replyLabel).build();
            Intent replyInten = new Intent(this, ReviewActivity.class);
            replyInten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent replyPendeingIntent = PendingIntent.getActivity(this, 0, replyInten, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_sms_black_24dp,
                    "Reply", replyPendeingIntent).addRemoteInput(remoteInput)
                    .setAllowGeneratedReplies(true)
            .build();
            builder.addAction(action);
       // }
        /*final int max_progress = 100;
        int current_progress = 0;
        //builder.setProgress(max_progress,current_progress,false);//f for determeinatne
        builder.setProgress(0, current_progress, true);//f for determeinatne
        final NotificationManagerCompat compat = NotificationManagerCompat.from(this);
        compat.notify(NOTIFICATION_id, builder.build());
        Thread thread = new Thread() {
            @Override
            public void run() {
                int count = 0;
                try {
                    while (count <= 100) {
                        count = count + 10;
                        sleep(1000);
                        //builder.setProgress(max_progress, count, false);//for update
                        //compat.notify(NOTIFICATION_id, builder.build());
                    }
                    builder.setContentText("download complete");
                    builder.setProgress(0, 0, false);//
                    //remove the progress from
                    //to update teh otificaiton
                    compat.notify(NOTIFICATION_id, builder.build());

                } catch (Exception e) {

                }

            }
        };
        thread.start();*/
       /* Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.balll);
        //to add the imagae at the thumbnail
        builder.setLargeIcon(bitmap);
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));*///for the image show in notification
        //builder.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.Text)));

        //Create Notification.
        NotificationManager compat = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);//NotificationManagerCompat.from(this);
        compat.notify(NOTIFICATION_id, builder.build());
        builder.setAutoCancel(true);
    }

    public void dispalayNotificatonForYesNO(View View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNOtificationChannerl();
        }

        Intent yesIntent = new Intent(this, YesActivity.class);
        //yesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent yesLandingInten = PendingIntent.getActivity(this, 0, yesIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent NoIntent = new Intent(this, NoActivtity.class);
        //NoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent NoLandingInten = PendingIntent.getActivity(this, 0, NoIntent, PendingIntent.FLAG_ONE_SHOT);

        //time to create notification:
       /* Be aware that Notification.Builder() also has a notification channel id setter method called
         setChannel(String channelId), so you can choose to set the notification channel id either in the
         constructor or using the setter method.
        */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_id);
        builder.setSmallIcon(R.drawable.ic_sms_black_24dp);
        builder.setContentTitle("Yes No Notification");
        builder.setContentText("You will be enters into 2 different activities depends on your selection.... \"YES OR NO\"");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//to cancep the message form above
        builder.setAutoCancel(true);
        // builder.setContentIntent(landingPendingIntent);//to call other activity or simple click to enter into the other activity
        builder.addAction(R.drawable.ic_check_black_24dp, "Yes", yesLandingInten);//these 2 yes no for cancel /or go to spectific setting
        builder.addAction(R.drawable.ic_close_black_24dp, "No", NoLandingInten);
        NotificationManager compat = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);//NotificationManagerCompat.from(this);
        compat.notify(NOTIFICATION_id, builder.build());
        builder.setAutoCancel(true);
    }

    private void dispalayNotificatonForReply(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNOtificationChannerl();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_id);
        builder.setSmallIcon(R.drawable.ic_sms_black_24dp);
        builder.setContentTitle("Important Message");
        builder.setContentText("this is an important message please reply me  ..");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//to cancel the message form above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //1. Add Direct Reply Action
            String replyLabel = "Enter your reply here";
            //Initialise RemoteInput
            RemoteInput remoteInput = new RemoteInput.Builder(TXT_reply).setLabel(replyLabel).build();
            // b. Build your notification action

            Intent replyInten = new Intent(this, ReviewActivity.class);
            // replyInten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent replyPendeingIntent = PendingIntent.getActivity(this, 0, replyInten, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_sms_black_24dp,
                    "Reply", replyPendeingIntent).addRemoteInput(remoteInput)
                    .setAllowGeneratedReplies(true)
                    .build();
            builder.addAction(action);
            NotificationManager compat = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);//NotificationManagerCompat.from(this);
            compat.notify(NOTIFICATION_id, builder.build());
            builder.setAutoCancel(true);
        }
    }

    /*
    private void dispalayNotificatonForReply(View v) {
        String replyLabel = "notif action reply";
        RemoteInput remoteInput = new RemoteInput.Builder("Enter your msg")
                .setLabel(replyLabel)
                .build();
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_sms_black_24dp, replyLabel, getReplyPendingIntent())
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_sms_black_24dp);
        mBuilder.setContentTitle("Important Message");
        mBuilder.setContentText("this is an important message please reply me  ..")
                .setShowWhen(true)
                .addAction(replyAction); // reply action from step b above
        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);
        mNotificationManager.notify(NOTIFICATION_id, mBuilder.build());

    }

    private PendingIntent getReplyPendingIntent() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            // start a
            // (i) broadcast receiver which runs on the UI thread or
            // (ii) service for a background task to b executed , but for the purpose of
            // this codelab, will be doing a broadcast receiver
            intent = new Intent(this, ReviewActivity.class);
            //  intent.setAction(REPLY_ACTION);
            // intent.putExtra(KEY_NOTIFICATION_ID, notificationId);
            //intent.putExtra(KEY_MESSAGE_ID, messageId);
            PendingIntent replyPendeingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return  replyPendeingIntent;
          //  return PendingIntent.getBroadcast(getApplicationContext(), 100, intent,
            //        PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            // start your activity for Android M and below
            intent = new Intent(this, ReviewActivity.class);
            //  intent.setAction(REPLY_ACTION);
            // intent.putExtra(KEY_MESSAGE_ID, messageId);
            //intent.putExtra(KEY_NOTIFICATION_ID, notifyId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return PendingIntent.getActivity(this, 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }*/
    public void CreateImageNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNOtificationChannerl();
        }
        Intent LandingIntent = new Intent(this, LandingActivity.class);
        // LandingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent landingPendingIntent = PendingIntent.getActivity(this, 0, LandingIntent, PendingIntent.FLAG_ONE_SHOT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.google1);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_id);
        builder.setSmallIcon(R.drawable.ic_sms_black_24dp)
                .setContentTitle("Image View in Notification")
                .setContentText("this is our Image view Notification")
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//to cancep the message form above
        builder.setAutoCancel(true);
        builder.setContentIntent(landingPendingIntent);//to call other activity or simple click to enter into the other activity
        NotificationManager compat = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);//NotificationManagerCompat.from(this);
        compat.notify(NOTIFICATION_id, builder.build());
        builder.setAutoCancel(true);

    }

    public void ExpendedContentArea() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNOtificationChannerl();
        }
        String bigtextmsg = "this is our big text message here we use this content to show some information to our user with in the notificiaton";
        Intent LandingIntent = new Intent(this, LandingActivity.class);
        // LandingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent landingPendingIntent = PendingIntent.getActivity(this, 0, LandingIntent, PendingIntent.FLAG_ONE_SHOT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.google1);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_id);
        builder.setSmallIcon(R.drawable.ic_sms_black_24dp)
                .setContentTitle("Expend view Notification")
                .setContentText("please expends thus to see full notification")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigtextmsg));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//to cancep the message form above
        builder.setAutoCancel(true);
        builder.setContentIntent(landingPendingIntent);//to call other activity or simple click to enter into the other activity
        NotificationManager compat = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);//NotificationManagerCompat.from(this);
        compat.notify(NOTIFICATION_id, builder.build());
        builder.setAutoCancel(true);

    }

    public void createHeaderNotification(String aMessage) {

        final int NOTIFY_ID = 1002;
        // There are hardcoding only for show it's just strings
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CreteNotificationChannel1();
            builder = new NotificationCompat.Builder(this, id);

            intent = new Intent(this, MainActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle(aMessage)  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
        // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this,"");//eska chanekl id se koe lena dena nahi h
            intent = new Intent(this, MainActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            builder.setContentTitle(aMessage)                           // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreteNotificationChannel1() {
        String name = "my_package_channel";
        String description = "my_package_first_channel"; // The user-visible description of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = notifManager.getNotificationChannel(id);
        if (mChannel == null) {
            mChannel = new NotificationChannel(id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableVibration(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notifManager.createNotificationChannel(mChannel);

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNOtificationChannerl() {
        CharSequence name = "Personal NOtification";
        String description = "include allthe personal notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        //create notification nchannel
        NotificationChannel notificationChannel = new NotificationChannel(Channel_id, name, importance);
        // Sets whether notifications posted to this channel should display notification lights
        notificationChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        notificationChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        notificationChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationChannel.setDescription(description);
            /*NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);*///it can also be handle like this
        getManager().createNotificationChannel(notificationChannel);

    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;

    }


}
