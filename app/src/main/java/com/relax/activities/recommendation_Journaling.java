package com.relax.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.relax.R;
import com.relax.utilities.globalVariables;

public class recommendation_Journaling extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_journaling);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.JournalTitle);

        Button btn = findViewById(R.id.btn_home);
        Intent intent = new Intent();

        if (globalVariables.backURL != null && !globalVariables.backURL.equals("")) {

            if (globalVariables.backURL.equals("Home")) {
                btn.setText(getString(R.string.back_home));
                intent = new Intent(this, Home.class);
            } else if (globalVariables.backURL.equals("manageSession")) {
                btn.setText(getString(R.string.back_chat));
                intent = new Intent(this, chatPage.class);
                globalVariables.backURL = "recommendation_Journaling";
            }

            Intent finalIntent = intent;
            btn.setOnClickListener(view -> startActivity(finalIntent));
        }


        int color = Color.parseColor("#2ba6c8");

        CharSequence t1 = getText(R.string.JournalHealthItem1);
        SpannableString s1 = new SpannableString(t1);
        s1.setSpan(new BulletSpan(15, color, 10), 0, t1.length(), 0);

        CharSequence t2 = getText(R.string.JournalHealthItem2);
        SpannableString s2 = new SpannableString(t2);
        s2.setSpan(new BulletSpan(15, color, 10), 0, t2.length(), 0);

        CharSequence t3 = getText(R.string.JournalHealthItem3);
        SpannableString s3 = new SpannableString(t3);
        s3.setSpan(new BulletSpan(15, color, 10), 0, t3.length(), 0);

        CharSequence t4 = getText(R.string.JournalHealthItem4);
        SpannableString s4 = new SpannableString(t4);
        s4.setSpan(new BulletSpan(15, color, 10), 0, t4.length(), 0);

        TextView txt = findViewById(R.id.JournalHealthItems);
        txt.setText(TextUtils.concat(s1, s2, s3, s4));

        CharSequence t7 = getText(R.string.JournalDrawBack1);
        SpannableString s7 = new SpannableString(t7);
        s7.setSpan(new BulletSpan(15, color, 10), 0, t7.length(), 0);

        CharSequence t8 = getText(R.string.JournalDrawBack2);
        SpannableString s8 = new SpannableString(t8);
        s8.setSpan(new BulletSpan(15, color, 10), 0, t8.length(), 0);

        CharSequence t9 = getText(R.string.JournalDrawBack3);
        SpannableString s9 = new SpannableString(t9);
        s9.setSpan(new BulletSpan(15, color, 10), 0, t9.length(), 0);

        TextView txt2 = findViewById(R.id.JournalDrawBackTxt);
        txt2.setText(TextUtils.concat(s7, s8, s9));

    }
}