package com.relax.utilities;

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

import com.relax.R;
import com.relax.activities.chatPage;

public class notificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "channelName";
    Bitmap largeIcon;
    com.relax.utilities.dbHelper dbHelper;
    Context context;

    private NotificationManager manager;

    public notificationHelper(Context base) {
        super(base);
        this.context = base;
        largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.relax);
        dbHelper = new dbHelper(this);
        createChannel();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
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
        NotificationCompat.Builder builder;

        Intent resultIntent = new Intent(this, chatPage.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE);

        int prediction = dbHelper.ifUserNotifyTimeIsNowGetPrediction(globalVariables.userID);
        globalVariables.botPrediction = prediction;

        //alert user based on analysis score
        if (prediction == 0 || prediction == 1) {//Very Negative - Negative
            builder = negative(resultPendingIntent);
        } else {
            if (prediction == 3 || prediction == 4) {//Very Positive - Positive
                builder = positive(resultPendingIntent);
            } else {//neutral 2, like global warming, google it
                builder = neutral(resultPendingIntent);
            }
        }

        return builder;
    }

    public NotificationCompat.Builder negative(PendingIntent resultPendingIntent) {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Hi, " + globalVariables.userName)
                .setContentText("You seemed upset early, do you wanna talk about it?")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIcon)
                .setColor(getResources().getColor(R.color.dark_turquoise, getTheme()))
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }

    public NotificationCompat.Builder positive(PendingIntent resultPendingIntent) {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Hi, " + globalVariables.userName)
                .setContentText("your mode is at ease today, enjoy the rest of your day!")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIcon)
                .setColor(getResources().getColor(R.color.dark_turquoise, getTheme()))
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }

    public NotificationCompat.Builder neutral(PendingIntent resultPendingIntent) {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Hi, " + globalVariables.userName)
                .setContentText("I'm alone here..care to talk?")
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(largeIcon)
                .setColor(getResources().getColor(R.color.dark_turquoise, getTheme()))
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);
    }
}
