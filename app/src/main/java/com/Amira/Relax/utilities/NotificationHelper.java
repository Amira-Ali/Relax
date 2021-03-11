package com.Amira.Relax.utilities;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.Amira.Relax.R;
import com.Amira.Relax.activities.ChatPage;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "channelName";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName,
                NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        Intent resultIntent= new Intent(this, ChatPage.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,1,resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.relax);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Hi, " + GlobalVariables.username)
                .setContentText("I'm alone here..care to talk?")
                .setSmallIcon(R.drawable.relaxlogo)
                .setLargeIcon(largeIcon)
                .setColor(getResources().getColor(R.color.dark_turquoise))
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }
}
