package com.relax.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.relax.R;
import com.relax.fragments.timePickerFragment;
import com.relax.utilities.alertReceiver;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class timePicker extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private EditText textView;
    Button pickTimeBtn, cancelTimeBtn, backBtn;
    int userID = globalVariables.userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Notifications Time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.Time);
        String selected_time = getUserNotificationTimeIfExists(globalVariables.userID);
        textView.getText().clear();
        textView.setText(selected_time);

        pickTimeBtn = findViewById(R.id.pickTimeBtn);
        pickTimeBtn.setOnClickListener(v -> showAlarm());

        cancelTimeBtn = findViewById(R.id.stopNotificationBtn);
        cancelTimeBtn.setOnClickListener(v -> cancelAlarm());

        backBtn = findViewById(R.id.backChatBtn);
        Intent intent = null;

        if (globalVariables.backURL.equals("Home")) {
            backBtn.setText(getString(R.string.back_home));
            intent = new Intent(timePicker.this, Home.class);
        } else if (globalVariables.backURL.equals("manageSession")) {
            globalVariables.backURL = "timePicker";
            backBtn.setText(getString(R.string.back_chat));
            intent = new Intent(timePicker.this, chatPage.class);
        }

        Intent finalIntent = intent;
        backBtn.setOnClickListener(view -> startActivity(finalIntent));

    }

    private String getUserNotificationTimeIfExists(int userID) {
        String selected_time = "Notification time is set at: ";
        final dbHelper databaseHelper = new dbHelper(this);
        String result = databaseHelper.getUserNotificationTime(userID);
        if (!result.isEmpty()) {
            selected_time += result;
        } else {
            selected_time = "No notification time has been set yet!";
        }
        return selected_time;
    }

    private void showAlarm() {
        DialogFragment timePicker = new timePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
        setAlarm(c);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "You will get a daily notification at: ";
        String currentTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        timeText += currentTime;
        textView.getText().clear();
        textView.append(timeText);
        globalVariables.notificationTime = timeText;
        final dbHelper databaseHelper = new dbHelper(this);
        databaseHelper.setUserNotificationTime(userID, currentTime);
    }

    private void setAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, alertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent,  PendingIntent.FLAG_IMMUTABLE);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, alertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
        textView.getText().clear();
        String cancelled = "Notification cancelled!";
        textView.append(cancelled);
    }

}