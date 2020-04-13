package com.deepanshu.notification2;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.deepanshu.notification2.StaticValue.NOTIFICATION_id;
import static com.deepanshu.notification2.StaticValue.TXT_reply;

public class ReviewActivity extends AppCompatActivity {
private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        textView=findViewById(R.id.texview);
        Bundle remoteReply= RemoteInput.getResultsFromIntent(getIntent());
        if(remoteReply!=null){
            String message=remoteReply.getCharSequence(TXT_reply).toString();
            textView.setText(message);
        }
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_id);
    }
}
