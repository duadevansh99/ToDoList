package com.example.de.todolist;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import java.util.*;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ALARM="alarm channel";
    @Override
    public void onReceive(Context context, Intent intent) {

        String alarmItem=intent.getStringExtra(MainActivity.ALARM_ITEM);
        String alarmDesc=intent.getStringExtra(MainActivity.ALARM_DESC);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1=new NotificationChannel(CHANNEL_ALARM,"HIGH ALARM CHANNEL",NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel1);
        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,CHANNEL_ALARM);
        builder.setContentTitle(alarmItem);
        builder.setContentText(alarmDesc);
        builder.setSmallIcon(R.drawable.ic_todo);
        builder.setAutoCancel(true);

        Intent intent1=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context,8,intent1,0);
        builder.setContentIntent(pendingIntent);

        Notification notification=builder.build();
        manager.notify(4,notification);

    }
}
