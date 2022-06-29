package com.relax.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class alertReceiver extends BroadcastReceiver {

    dbHelper dbHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        notificationHelper notificationHelper = new notificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
        dbHelper = new dbHelper(context);
        dbHelper.updateNoteAnalysis(globalVariables.userID);

    }
}
