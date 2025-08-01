package com.learnPlanner.ui;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import android.app.Notification;

import com.learnPlanner.R;

public class Receiver extends BroadcastReceiver {
    String channel_id = "test";
    private int notificationId;

    @Override
    public void onReceive(Context context, Intent intent) {
        // When the BroadcastReceiver is receiving is when intent broadcast is called
        Toast.makeText(context, intent.getStringExtra("key"), Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel_id);

        Notification notification = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(intent.getStringExtra("key"))
                .setContentTitle("Learn Planner").build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId++, notification);
    }

    private void createNotificationChannel(Context context, String channel_id) {
        CharSequence name = "learnPlanner";
        String description = "learnPlannerDescription";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channel_id, name, importance);

        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}