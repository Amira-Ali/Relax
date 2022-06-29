package com.relax.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.relax.R;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;

public class Feedback extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    dbHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        dbHelper = new dbHelper(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Feedback");

        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final TextView user_feedback = findViewById(R.id.user_feedback);

        Button sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(v -> {
            float ratingValue = ratingBar.getRating();
            String feedback = user_feedback.getText().toString();
            long row_id = dbHelper.insertRateFeedback(globalVariables.userID, String.valueOf(ratingValue), feedback);
            if (row_id != -1) {
                sendEmail(ratingValue, feedback);
            }

        });
        dbHelper.close();
    }

    private void sendEmail(float ratingValue, String feedback) {
        String recipient = "ashlibek@noc.ly";
        String[] recipients = new String[1];
        recipients[0] = recipient;
        String subject = "Feedback from User: " + globalVariables.userName;
        String message = "Rating: " + ratingValue + "\n" + "Feedback: " + feedback;

        Intent sendEmail = new Intent(Intent.ACTION_SEND);
        sendEmail.putExtra(Intent.EXTRA_EMAIL, recipients);
        sendEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendEmail.putExtra(Intent.EXTRA_TEXT, message);

        sendEmail.setType("message/rfc822");
        sendEmail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user
        // returns to your app, your app is displayed, instead of the email app.
        try {
            startActivity(Intent.createChooser(sendEmail, "Send Mail: "));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }
}
